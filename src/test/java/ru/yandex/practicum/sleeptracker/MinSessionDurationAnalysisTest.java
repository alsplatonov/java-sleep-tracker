package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.MinSessionDurationAnalysis;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinSessionDurationAnalysisTest {

    @Test
    void minDurationListIsEmpty() {
        SleepAnalysisResult result =
                new MinSessionDurationAnalysis().apply(List.of());

        assertEquals(0, result.getValue());
    }

    @Test
    void minDurationCalculatedCorrectly() {
        List<SleepingSession> sessions = List.of(
                session(240),
                session(75)
        );

        SleepAnalysisResult result =
                new MinSessionDurationAnalysis().apply(sessions);

        assertEquals(75, result.getValue());
    }

    private SleepingSession session(int durationMinutes) {
        LocalDateTime start = java.time.LocalDateTime.of(2025, 12, 16, 23, 0);
        LocalDateTime end = start.plusMinutes(durationMinutes);

        return new SleepingSession(start, end, SleepQuality.GOOD);
    }

}
