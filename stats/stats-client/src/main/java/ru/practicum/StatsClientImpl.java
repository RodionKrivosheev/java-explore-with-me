package ru.practicum;


import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@PropertySource("classpath:stats-application.properties")
public class StatsClientImpl implements StatsClient {

    private final WebClient webClient;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClientImpl(@Value("${stats-server.url}") String serverUrl) {
        webClient = WebClient.builder()
                .baseUrl(serverUrl)
                .build();
    }

    @Override
    public void saveEndpoint(String app, String uri, String ip) {
        LocalDateTime localDateTime = LocalDateTime.now();
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(localDateTime.format(dateTimeFormatter))
                .build();
        webClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(endpointHitDto))
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/stats");

        uriBuilder.queryParam("start", start.format(dateTimeFormatter));
        uriBuilder.queryParam("end", end.format(dateTimeFormatter));

        if (uris != null && !uris.isEmpty()) {
            String urisParam = StringUtils.join(uris, ',');
            uriBuilder.queryParam("uris", urisParam);
        }
        if (unique != null) {
            uriBuilder.queryParam("unique", unique);
        }
        URI uri = uriBuilder.build().toUri();
        return webClient.get()
                .uri(uri.toASCIIString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ViewStatsDto.class)
                .collectList()
                .block();
    }
}
