package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import static ru.yandex.practicum.sleeptracker.model.SleepSessionUtilities.isNightSleep;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class UserChronotypeAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", Chronotype.PIGEON);
        }

        // Фильтруем только ночные сессии
        List<SleepingSession> nightSessions = sessions.stream()
                .filter(session -> isNightSleep(session))
                .collect(Collectors.toList());

        Map<String, Long> counts = nightSessions.stream()
                .collect(Collectors.groupingBy(session -> {
                    if (isOwl(session)) return "owl";
                    else if (isLark(session)) return "lark";
                    else return "pigeon";
                }, Collectors.counting()));

        long owls = counts.getOrDefault("owl", 0L);
        long larks = counts.getOrDefault("lark", 0L);
        long pigeons = counts.getOrDefault("pigeon", 0L);

        // Определяем наиболее часто встречающийся тип
        Chronotype chronotype = (owls > larks && owls > pigeons) ? Chronotype.OWL
                : (larks > owls && larks > pigeons) ? Chronotype.LARK
                : Chronotype.PIGEON;

        return new SleepAnalysisResult("Хронотип пользователя", chronotype);
    }

    private boolean isOwl(SleepingSession session) {
        LocalTime start = session.getStart().toLocalTime();
        LocalTime end = session.getEnd().toLocalTime();

        return start.isAfter(LocalTime.of(23, 0))
                && end.isAfter(LocalTime.of(9, 0));
    }

    private boolean isLark(SleepingSession session) {
        LocalTime start = session.getStart().toLocalTime();
        LocalTime end = session.getEnd().toLocalTime();

        return start.isBefore(LocalTime.of(22, 0))
                && end.isBefore(LocalTime.of(7, 0))
                // исключаем ночи после полуночи
                && session.getStart().toLocalDate()
                .equals(session.getEnd().toLocalDate());
    }
}

