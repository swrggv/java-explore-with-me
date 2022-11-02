package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationPublicController {
    public final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Get compilations");
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.info("Get compilation {}", compId);
        return compilationService.getCompilation(compId);
    }
}
