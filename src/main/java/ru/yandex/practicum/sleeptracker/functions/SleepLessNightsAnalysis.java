package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import static ru.yandex.practicum.sleeptracker.model.SleepSessionUtilities.isNightSleep;

/*
 Анализ: количество бессонных ночей
 */
public class SleepLessNightsAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final int NIGHT_START_HOUR = 12;
    private static final int NIGHT_END_HOUR = 6;

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        //Границы анализа сессий сна
        LocalDateTime firstStart = sessions.get(0).getStart();
        LocalDateTime  lastEnd = sessions.get(sessions.size() - 1).getEnd();

        //Определяем первую ночь (если первая сессия началась после NIGHT_START_HOUR, берём следующую ночь)
        LocalDate firstNight = firstStart.getHour() >= NIGHT_START_HOUR
                ? firstStart.toLocalDate().plusDays(1)
                : firstStart.toLocalDate();

        //Определяем последнюю ночь
        LocalDate lastNight = lastEnd.toLocalDate();  // включаем последнюю ночь;

        //Общее количество ночей
        //int totalNights = Period.between(firstNight, lastNight).getDays(); //корректно работает только в рамках 1 мес.
        int totalNights = (int) (ChronoUnit.DAYS.between(firstNight, lastNight) + 1); // +1, чтобы включить последнюю ночь

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

    //Возвращает дату ночи, к которой относится сессия
    private LocalDate nightDate(SleepingSession session) {
        LocalDate start = session.getStart().toLocalDate();
        LocalDate end = session.getEnd().toLocalDate();

        // если сессия пересекает полночь
        if (start.isBefore(end)) return start;

        // если сессия началась после полудня (NIGHT_START_HOUR), относим её к этой ночи
        if (session.getStart().getHour() >= NIGHT_START_HOUR) return start;

        // иначе, если сессия началась до полудня и до NIGHT_END_HOUR, относим её к предыдущей ночи
        if (session.getStart().getHour() < NIGHT_END_HOUR) return start.minusDays(1);

        return start;
    }
}
