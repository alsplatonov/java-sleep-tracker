package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.TotalSessionsAnalysis;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalSessionsAnalysisTest {

    private final TotalSessionsAnalysis analysis = new TotalSessionsAnalysis();

    @Test
    void shouldReturn0WhenSessionsNotFound() {
        SleepAnalysisResult result = analysis.apply(List.of());

        assertEquals(0, result.getValue());
    }

    @Test
    void shouldCountAllSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(9),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(6),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = analysis.apply(sessions);

        assertEquals(2, result.getValue());
    }
}
