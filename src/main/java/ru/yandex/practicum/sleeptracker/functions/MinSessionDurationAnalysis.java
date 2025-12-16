package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

/*
 Анализ: минимальная продолжительность сессии сна
 */
public class MinSessionDurationAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {


    //Возвращает минимальную длительность сессии сна в минутах
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        int minDuration = sessions.stream()
                .mapToInt(SleepingSession::getDurationMinutes)
                .min()
                .orElse(0);

        return new SleepAnalysisResult(
                "Минимальная продолжительность сессии (мин)",
                minDuration
        );
    }
}