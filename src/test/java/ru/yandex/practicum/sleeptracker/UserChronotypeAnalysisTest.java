package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.UserChronotypeAnalysis;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserChronotypeAnalysisTest {

    private final UserChronotypeAnalysis analysis = new UserChronotypeAnalysis();

    @Test
    void allOwls() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 10, 0)),
                session(LocalDateTime.of(2025, 10, 2, 23, 45),
                        LocalDateTime.of(2025, 10, 3, 9, 30))
        );

        SleepAnalysisResult result = analysis.apply(sessions);
        assertEquals(Chronotype.OWL, result.getValue());
    }

    @Test
    void mixedSessionsWithPigeonExcellence() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 7, 30)), // Pigeon
                session(LocalDateTime.of(2025, 10, 2, 21, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 30)), // Lark
                session(LocalDateTime.of(2025, 10, 3, 0, 30),
                        LocalDateTime.of(2025, 10, 3, 6, 30))  // Pigeon
        );

        SleepAnalysisResult result = analysis.apply(sessions);
        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void equalTypesResultsInPigeon() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 8, 30)), // Pigeon
                session(LocalDateTime.of(2025, 10, 2, 23, 30),
                        LocalDateTime.of(2025, 10, 3, 10, 0)) // Owl
        );

        SleepAnalysisResult result = analysis.apply(sessions);
        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    private SleepingSession session(LocalDateTime start, LocalDateTime end) {
        return new SleepingSession(start, end, SleepQuality.GOOD);
    }
}