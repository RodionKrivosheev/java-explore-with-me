package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto saveEndpoint(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Запрос POST для '/hit' endpoint с параметрами body={}", endpointHitDto.toString());
        return statsService.addNewHit(endpointHitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    private List<ViewStatsDto> getStats(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Запрос GET для '/stats' с параметрами start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }
}
