package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsClientImpl implements StatsClient {

    private final String url = "http://localhost:9090";

    private final WebClient webClient = WebClient.builder()
            .baseUrl(url)
            .build();

    @Override
    public void saveEndpoint(String app, String uri, String ip, LocalDateTime timestamp) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
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
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/stats");
        if (start != null && !start.isEmpty()) {
            uriBuilder.queryParam("start", start);
        }
        if (end != null && !end.isEmpty()) {
            uriBuilder.queryParam("end", end);
        }

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
