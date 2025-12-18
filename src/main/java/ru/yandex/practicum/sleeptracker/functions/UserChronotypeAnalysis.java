package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalTime;
import java.util.List;
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
                .filter(session ->
                        !session.getStart().toLocalTime().isAfter(LocalTime.of(12, 0))
                        || session.getStart().toLocalDate().isBefore(session.getEnd().toLocalDate()))
                .collect(Collectors.toList());

        // Подсчёт каждой категории
        int owls = (int) nightSessions.stream()
                .filter(session -> {
                    LocalTime start = session.getStart().toLocalTime();
                    LocalTime end = session.getEnd().toLocalTime();
                    return start.isAfter(LocalTime.of(23, 0))
                            && end.isAfter(LocalTime.of(9, 0));
                })
                .count();

        int larks = (int) nightSessions.stream()
                .filter(session -> {
                    LocalTime start = session.getStart().toLocalTime();
                    LocalTime end = session.getEnd().toLocalTime();
                    return start.isBefore(LocalTime.of(22, 0))
                            && end.isBefore(LocalTime.of(7, 0))
                            //исключить ночи после полуночи
                            && session.getStart().toLocalDate().equals(session.getEnd().toLocalDate());
                })
                .count();

        // Остальные ночи считаем голубями
        int pigeons = nightSessions.size() - owls - larks;

        // Определяем наиболее часто встречающийся тип
        Chronotype chronotype = (owls > larks && owls > pigeons) ? Chronotype.OWL
                : (larks > owls && larks > pigeons) ? Chronotype.LARK
                : Chronotype.PIGEON;

        return new SleepAnalysisResult("Хронотип пользователя", chronotype);
    }
}

