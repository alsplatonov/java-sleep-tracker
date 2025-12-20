package ru.yandex.practicum.sleeptracker.model;

import java.time.LocalDateTime;

public class SleepSessionUtilities {
    // Границы ночного сна
    private static final int NIGHT_START_HOUR = 12;
    private static final int NIGHT_END_HOUR = 6;

    //Проверяет, была ли сессия ночной
    public static boolean isNightSleep(SleepingSession session) {
        LocalDateTime start = session.getStart();
        LocalDateTime end = session.getEnd();

        //Пересекает полночь → ночь
        if (start.toLocalDate().isBefore(end.toLocalDate())) {
            return true;
        }

        //Началась после полуночи и до конца ночи
        return start.getHour() < NIGHT_END_HOUR;
    }
}
