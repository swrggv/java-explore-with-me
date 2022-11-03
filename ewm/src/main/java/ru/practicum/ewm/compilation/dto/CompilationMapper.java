package ru.practicum.ewm.compilation.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {
    public static Compilation toCompilationFromNewCompilationDto(NewCompilationDto compilationDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(compilationDto.isPinned());
        compilation.setTitle(compilationDto.getTitle());
        return compilation;
    }

    public static CompilationDto toDtoFromCompilation(Compilation compilation, List<EventShortDto> events) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setPinned(compilation.isPinned());
        dto.setEvents(events);
        dto.setTitle(compilation.getTitle());
        return dto;
    }
}
