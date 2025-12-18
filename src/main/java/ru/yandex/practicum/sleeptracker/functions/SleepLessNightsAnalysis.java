package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 Анализ: количество бессонных ночей
 */
public class SleepLessNightsAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        //Границы анализа сессий сна
        var firstStart = sessions.get(0).getStart();
        var lastEnd = sessions.get(sessions.size() - 1).getEnd();

        //Определяем первую ночь (если первая сессия началась после 12:00, берём следующую ночь)
        LocalDate firstNight = firstStart.getHour() >= 12
                ? firstStart.toLocalDate().plusDays(1)
                : firstStart.toLocalDate();

        //Определяем последнюю ночь
        LocalDate lastNight = lastEnd.toLocalDate();

        //Общее количество ночей
        int totalNights = Period.between(firstNight, lastNight).getDays();

        //Ночи, в которые был сон
        List<LocalDate> nightsWithSleep = sessions.stream()
                .filter(session -> isNightSleep(session))
                .map(session -> nightDate(session))
                .collect(Collectors.toList());


        int insomniaNights = Math.max(totalNights - nightsWithSleep.size(), 0);

        return new SleepAnalysisResult(
                "Количество бессонных ночей",
                insomniaNights
        );
    }

    //Проверяет, была ли сессия ночной
    /*
    Сессия ночная, если:
        сон начался в один день, а закончился в другой
    ИЛИ
        начался после 12:00 и закончился до 06:00
    */
    private boolean isNightSleep(SleepingSession session) {
        return session.getStart().toLocalDate()
                .isBefore(session.getEnd().toLocalDate())
                || (session.getStart().getHour() >= 12
                && session.getEnd().getHour() < 6);
    }

    //Возвращает дату ночи, к которой относится сессия
    private LocalDate nightDate(SleepingSession session) {
        return session.getEnd().toLocalDate();
    }
}
