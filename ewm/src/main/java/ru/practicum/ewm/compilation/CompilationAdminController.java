package ru.practicum.ewm.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

@RestController
@Validated
@RequestMapping("/admin/compilations")
@Slf4j
public class CompilationAdminController {
    private final CompilationService compilationService;

    @Autowired
    public CompilationAdminController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto addCompilation(@RequestBody NewCompilationDto compilationDto) {
        log.info("Add new compilation {}", compilationDto);
        return compilationService.addCompilation(compilationDto);
    }

    @DeleteMapping("{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        log.info("Delete compilation {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable long compId,
                                           @PathVariable long eventId) {
        log.info("Delete event {} from compilation {}", eventId, compId);
        compilationService.deleteEventFromCompilation(eventId, compId);
    }

    @PatchMapping("{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable long compId,
                                      @PathVariable long eventId) {
        log.info("Add event {} to compilation {}", eventId, compId);
        compilationService.addEventToCompilation(eventId, compId);
    }

    @DeleteMapping("{compId}/pin")
    public void deletePinCompilation(@PathVariable long compId) {
        log.info("Pin compilation {}", compId);
        compilationService.pin(compId, false);
    }

    @PatchMapping("{compId}/pin")
    public void addPinCompilation(@PathVariable long compId) {
        log.info("Delete pin of compilation {}", compId);
        compilationService.pin(compId, true);
    }
}
