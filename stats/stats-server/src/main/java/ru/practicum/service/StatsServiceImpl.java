package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exceptions.ValidationRequestException;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.repository.StatsRepository;
import ru.practicum.model.EndpointHit;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public EndpointHitDto addNewHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
        log.info("Добавили новый hit с параметром body={}", endpointHit);
        return EndpointHitMapper.toEndpointHitDto(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), FORMATTER);

        if (startTime.isAfter(endTime)) {
            throw new ValidationRequestException("Значение старта должно быть раньше значения конца");
        }

        if (unique) {
            return (uris != null) ? statsRepository.getViewStatsWithUriAndUnique(startTime, endTime, uris)
                    : statsRepository.getViewStatsUnique(startTime, endTime);
        } else {
            return (uris != null) ? statsRepository.getViewStatsWithUris(startTime, endTime, uris)
                    : statsRepository.getViewStats(startTime, endTime);
        }
    }
}
