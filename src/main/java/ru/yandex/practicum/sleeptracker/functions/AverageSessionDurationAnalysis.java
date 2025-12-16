package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

/*
 Анализ: минимальная продолжительность сессии сна
 */
public class AverageSessionDurationAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {

    //Возвращает минимальную длительность сессии сна в минутах
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        double AverageDuration = sessions.stream()
                .mapToInt(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0.0);

        return new SleepAnalysisResult(
                "Средняя продолжительность сессии (мин)",
                Math.round(AverageDuration)
        );
    }
}