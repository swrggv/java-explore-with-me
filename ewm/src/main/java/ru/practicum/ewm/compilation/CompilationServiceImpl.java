package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.ClientService;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.ModelNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final ClientService clientService;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilationFromNewCompilationDto(compilationDto, events);
        compilation = compilationRepository.save(compilation);
        return CompilationMapper.toDtoFromCompilation(compilation, transferToListEventShortDto(events));
    }

    @Override
    @Transactional
    public void deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(long eventId, long compId) {
        Compilation compilation = fromOptionalToCompilation(compId);
        Event event = fromOptionalToEvent(eventId);
        List<Event> events = compilation.getEvents();
        if (events.contains(event)) {
            events.remove(event);
        } else {
            throw new ModelNotFoundException("Impossible to remove event",
                    String.format("Event %d not found", eventId));
        }
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void addEventToCompilation(long eventId, long compId) {
        Event event = fromOptionalToEvent(eventId);
        Compilation compilation = fromOptionalToCompilation(compId);
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void pin(long compId, boolean pin) {
        Compilation compilation = fromOptionalToCompilation(compId);
        compilation.setPinned(pin);
        compilationRepository.save(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        int page = pageNumber(from, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, PageRequest.of(page, size));
        return transferToListCompilationDto(compilations);
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        Compilation compilation = fromOptionalToCompilation(compId);
        List<Event> events = compilation.getEvents();
        return CompilationMapper.toDtoFromCompilation(compilation, transferToListEventShortDto(events));
    }

    private List<EventShortDto> transferToListEventShortDto(List<Event> events) {
        return events.stream()
                .map(x -> EventMapper.toShortEventDtoFromEvent(x, clientService.getStats("events/" + x.getId())))
                .collect(Collectors.toList());
    }

    private List<CompilationDto> transferToListCompilationDto(List<Compilation> compilations) {
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<Event> events = compilation.getEvents();
            List<EventShortDto> eventShortDtos = events.stream()
                    .map(x -> EventMapper.toShortEventDtoFromEvent(x, clientService.getStats("events/" + x.getId())))
                    .collect(Collectors.toList());
            result.add(CompilationMapper.toDtoFromCompilation(compilation, eventShortDtos));
        }
        return result;
    }

    private int pageNumber(int from, int size) {
        return from / size;
    }

    private Compilation fromOptionalToCompilation(long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new ModelNotFoundException("Compilation not found",
                        String.format("Compilation %d not found", compId)));
    }

    private Event fromOptionalToEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new ModelNotFoundException("Compilation not found",
                        String.format("Compilation %d not found", eventId)));
    }
}
