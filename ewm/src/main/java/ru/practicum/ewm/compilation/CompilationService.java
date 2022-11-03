package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(long compId);

    void deleteEventFromCompilation(long eventId, long compId);

    void addEventToCompilation(long eventId, long compId);

    void pin(long compId, boolean pin);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(long compId);
}
