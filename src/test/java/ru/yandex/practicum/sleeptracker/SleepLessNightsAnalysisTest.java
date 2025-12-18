package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.SleepLessNightsAnalysis;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepLessNightsAnalysisTest {
    private final SleepLessNightsAnalysis analysis = new SleepLessNightsAnalysis();

    @Test
    void zeroSessions() {
        SleepAnalysisResult result = analysis.apply(List.of());
        assertEquals(0, result.getValue());
    }

    @Test
    void zeroSleepLessNights() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0)),
                session(LocalDateTime.of(2025, 10, 2, 23, 30),
                        LocalDateTime.of(2025, 10, 3, 6, 30))
        );

        SleepAnalysisResult result = analysis.apply(sessions);
        assertEquals(0, result.getValue());
    }

    @Test
    void oneSleepLessNight() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 7, 0),
                        LocalDateTime.of(2025, 10, 1, 11, 0)),
                session(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0))
        );

        SleepAnalysisResult result = analysis.apply(sessions);
        assertEquals(1, result.getValue());
    }

    @Test
    void nightAfterMidnight() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 2, 0),
                        LocalDateTime.of(2025, 10, 1, 5, 0))
        );

        SleepAnalysisResult result = analysis.apply(sessions);
        assertEquals(0, result.getValue());
    }

    private SleepingSession session(LocalDateTime start, LocalDateTime end) {
        return new SleepingSession(start, end, SleepQuality.GOOD);
    }
}
