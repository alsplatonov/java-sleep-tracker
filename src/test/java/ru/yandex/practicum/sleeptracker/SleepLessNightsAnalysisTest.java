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
                session(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0))
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

    @Test
    void sessionsAcrossMonths() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 9, 30, 23, 30),
                        LocalDateTime.of(2025, 10, 1, 6, 30)),
                session(LocalDateTime.of(2025, 10, 31, 23, 50),
                        LocalDateTime.of(2025, 11, 1, 6, 30))
        );

        SleepAnalysisResult result = analysis.apply(sessions);

        // Общее количество ночей: 30.09 -> 01.11 = 32 ночи
        // Ночи с сном: 2 ночи
        // Бессонные ночи = 32 - 2 = 30
        assertEquals(30, result.getValue());
    }

    @Test
    void firstSessionAfterMidnight() {
        List<SleepingSession> sessions = List.of(
                // Первая сессия начинается после полуночи
                session(LocalDateTime.of(2025, 10, 1, 1, 0),
                        LocalDateTime.of(2025, 10, 1, 6, 0)),
                session(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0))
        );

        SleepAnalysisResult result = analysis.apply(sessions);

        // Интервал ночей: 01.10 -> 03.10 = 3 ночи
        // Ночи с сном: 2 ночи
        // Бессонные ночи = 3 - 2 = 1
        assertEquals(1, result.getValue());
    }

    private SleepingSession session(LocalDateTime start, LocalDateTime end) {
        return new SleepingSession(start, end, SleepQuality.GOOD);
    }
}
