package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

/*
 Анализ: минимальная продолжительность сессии сна
 */
public class MaxSessionDurationAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {

    //Возвращает минимальную длительность сессии сна в минутах
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        int maxDuration = sessions.stream()
                .mapToInt(SleepingSession::getDurationMinutes)
                .max()
                .orElse(0);

        return new SleepAnalysisResult(
                "Максимальная продолжительность сессии (мин)",
                maxDuration
        );
    }
}