package ru.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.maper.StatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {
    @InjectMocks
    StatsServiceImpl statsService;
    @Mock
    StatsRepository statsRepository;
    @Mock
    StatsMapper statsMapper;
    @Captor
    private ArgumentCaptor<EndpointHit> endpointHitArgumentCaptor;

    Long id = 0L;
    String app = "ewm-main-service";
    String uri = "/events";
    String ip = "121.0.0.1";
    LocalDateTime timestamp =
            LocalDateTime.parse("2023-09-07 13:13:55", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    EndpointHitDto endpointHitDto = EndpointHitDto.builder()
            .app(app)
            .uri(uri)
            .ip(ip)
            .timestamp(timestamp)
            .build();

    EndpointHit endpointHit = EndpointHit.builder()
            .id(id)
            .app(app)
            .uri(uri)
            .ip(ip)
            .timestamp(timestamp)
            .build();

    List<ViewStats> viewStats1 = List.of(ViewStats.builder()
            .app(app)
            .uri(uri)
            .hits(1L)
            .build()
    );
    List<ViewStats> viewStats2 = List.of(ViewStats.builder()
            .app(app)
            .uri(uri)
            .hits(2L)
            .build()
    );
    List<ViewStats> viewStats3 = List.of(ViewStats.builder()
            .app(app)
            .uri(uri)
            .hits(3L)
            .build()
    );
    List<ViewStats> viewStats4 = List.of(ViewStats.builder()
            .app(app)
            .uri(uri)
            .hits(4L)
            .build()
    );
    List<ViewStatsDto> viewStatsDto1 = List.of(ViewStatsDto.builder()
            .app(app)
            .uri(uri)
            .hits(1L)
            .build()
    );
    List<ViewStatsDto> viewStatsDto2 = List.of(ViewStatsDto.builder()
            .app(app)
            .uri(uri)
            .hits(2L)
            .build()
    );
    List<ViewStatsDto> viewStatsDto3 = List.of(ViewStatsDto.builder()
            .app(app)
            .uri(uri)
            .hits(3L)
            .build()
    );
    List<ViewStatsDto> viewStatsDto4 = List.of(ViewStatsDto.builder()
            .app(app)
            .uri(uri)
            .hits(4L)
            .build()
    );

    @Test
    void hitStats_whenAddHitStats_hitStatsIsAdd() {
        when(statsMapper.toEndpointHit(endpointHitDto)).thenReturn(endpointHit);

        statsService.addHitStats(endpointHitDto);

        verify(statsRepository).save(endpointHitArgumentCaptor.capture());
        EndpointHit endpointHitForSave = endpointHitArgumentCaptor.getValue();

        InOrder inOrder = inOrder(statsMapper, statsRepository);
        assertAll(
                () -> inOrder.verify(statsMapper).toEndpointHit(endpointHitDto),
                () -> inOrder.verify(statsRepository).save(endpointHit),
                () -> assertEquals(endpointHit, endpointHitForSave),
                () -> assertEquals(endpointHitDto.getApp(), endpointHitForSave.getApp()),
                () -> assertEquals(endpointHitDto.getUri(), endpointHitForSave.getUri()),
                () -> assertEquals(endpointHitDto.getIp(), endpointHitForSave.getIp())
        );
    }

    @Test
    void getViewStats_whenUrisIsNotEmptyAndUniqIsTrue_thenReturnResultFindStatsByUrisUniqueIp() {
        when(statsRepository.findStatsByUrisUniqueIp(
                eq(List.of(uri)),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(viewStats1);
        when(statsMapper.toViewStatsDtoList(viewStats1)).thenReturn(viewStatsDto1);

        List<ViewStatsDto> resultViewStatsDto = statsService.getViewStats(
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(uri),
                true
        );

        InOrder inOrder = inOrder(statsRepository, statsMapper);
        assertAll(
                () -> inOrder.verify(statsRepository).findStatsByUrisUniqueIp(
                        eq(List.of(uri)),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ),
                () -> inOrder.verify(statsMapper).toViewStatsDtoList(viewStats1),
                () -> verify(statsRepository, never()).findStatsByUrisNotUniqueIp(any(), any(), any()),
                () -> verify(statsRepository, never()).findStatsAllUrisUniqueIp(any(), any()),
                () -> verify(statsRepository, never()).findStatsAllUrisNotUniqueIp(any(), any()),
                () -> assertEquals(viewStatsDto1, resultViewStatsDto)
        );
    }

    @Test
    void getViewStats_whenUrisIsNotEmptyAndUniqIsFalse_thenReturnResultFindStatsByUrisNotUniqueIp() {
        when(statsRepository.findStatsByUrisNotUniqueIp(
                eq(List.of(uri)),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(viewStats2);
        when(statsMapper.toViewStatsDtoList(viewStats2)).thenReturn(viewStatsDto2);

        List<ViewStatsDto> resultViewStatsDto = statsService.getViewStats(
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(uri),
                false
        );

        InOrder inOrder = inOrder(statsRepository, statsMapper);
        assertAll(
                () -> inOrder.verify(statsRepository).findStatsByUrisNotUniqueIp(
                        eq(List.of(uri)),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ),
                () -> inOrder.verify(statsMapper).toViewStatsDtoList(viewStats2),
                () -> verify(statsRepository, never()).findStatsByUrisUniqueIp(any(), any(), any()),
                () -> verify(statsRepository, never()).findStatsAllUrisUniqueIp(any(), any()),
                () -> verify(statsRepository, never()).findStatsAllUrisNotUniqueIp(any(), any()),
                () -> assertEquals(viewStatsDto2, resultViewStatsDto)
        );
    }

    @Test
    void getViewStats_whenUrisIsEmptyAndUniqIsTrue_thenReturnResultFindAllStatsUniqueIp() {
        when(statsRepository.findStatsAllUrisUniqueIp(
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(viewStats3);
        when(statsMapper.toViewStatsDtoList(viewStats3)).thenReturn(viewStatsDto3);

        List<ViewStatsDto> resultViewStatsDto = statsService.getViewStats(
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                true
        );

        InOrder inOrder = inOrder(statsRepository, statsMapper);
        assertAll(
                () -> inOrder.verify(statsRepository).findStatsAllUrisUniqueIp(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ),
                () -> inOrder.verify(statsMapper).toViewStatsDtoList(viewStats3),
                () -> verify(statsRepository, never()).findStatsByUrisUniqueIp(any(), any(), any()),
                () -> verify(statsRepository, never()).findStatsByUrisNotUniqueIp(any(), any(), any()),
                () -> verify(statsRepository, never()).findStatsAllUrisNotUniqueIp(any(), any()),
                () -> assertEquals(viewStatsDto3, resultViewStatsDto)
        );
    }

    @Test
    void getViewStats_whenUrisIsEmptyAndUniqIsFalse_thenReturnResultFindAllStatsNotUniqueIp() {
        when(statsRepository.findStatsAllUrisNotUniqueIp(
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(viewStats4);
        when(statsMapper.toViewStatsDtoList(viewStats4)).thenReturn(viewStatsDto4);

        List<ViewStatsDto> resultViewStatsDto = statsService.getViewStats(
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                false
        );

        InOrder inOrder = inOrder(statsRepository, statsMapper);
        assertAll(
                () -> inOrder.verify(statsRepository).findStatsAllUrisNotUniqueIp(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ),
                () -> inOrder.verify(statsMapper).toViewStatsDtoList(viewStats4),
                () -> verify(statsRepository, never()).findStatsByUrisUniqueIp(any(), any(), any()),
                () -> verify(statsRepository, never()).findStatsAllUrisUniqueIp(any(), any()),
                () -> verify(statsRepository, never()).findStatsByUrisNotUniqueIp(any(), any(), any()),
                () -> assertEquals(viewStatsDto4, resultViewStatsDto)
        );
    }
}
