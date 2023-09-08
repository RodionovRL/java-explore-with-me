import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.practicum.client.api.StatsClientApi;
import ru.practicum.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class StatsClientTest {
    StatsClientApi statsClient;

    String app = "ewm-main-service.test";
    String uri = "/events/test";
    String ip = "121.0.0.1";
    LocalDateTime timestamp =
            LocalDateTime.parse("2023-09-07 13:13:55", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    LocalDateTime start =
            LocalDateTime.parse("2022-09-07 13:13:55", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    LocalDateTime end =
            LocalDateTime.parse("2024-09-07 13:13:55", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    EndpointHitDto endpointHitDto = EndpointHitDto.builder()
            .app(app)
            .uri(uri)
            .ip(ip)
            .timestamp(timestamp)
            .build();

    @Test
    void testPostAndGetStats(){
        ResponseEntity<Object> objectResponseEntity = statsClient.addEndpointHit(endpointHitDto);
        Object viewStats = statsClient.getViewStats(start, end, List.of(uri), false);
    }



}
