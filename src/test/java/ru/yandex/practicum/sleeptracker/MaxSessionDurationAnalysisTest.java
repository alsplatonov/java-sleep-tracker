package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.MaxSessionDurationAnalysis;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxSessionDurationAnalysisTest {

    @Test
    void maxDurationListIsEmpty() {
        SleepAnalysisResult result =
                new MaxSessionDurationAnalysis().apply(List.of());

        assertEquals(0, result.getValue());
    }

    @Test
    void maxDurationCalculatedCorrectly() {
        List<SleepingSession> sessions = List.of(
                session(240),
                session(75)
        );

        SleepAnalysisResult result =
                new MaxSessionDurationAnalysis().apply(sessions);

        assertEquals(240, result.getValue());
    }

    private SleepingSession session(int durationMinutes) {
        var start = java.time.LocalDateTime.of(2025, 12, 16, 23, 0);
        var end = start.plusMinutes(durationMinutes);

        return new SleepingSession(start, end, SleepQuality.GOOD);
    }

}
