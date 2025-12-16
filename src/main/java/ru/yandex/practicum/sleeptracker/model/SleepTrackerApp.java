package ru.yandex.practicum.sleeptracker.model;
import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> analyses =
            List.of(
                    new TotalSessionsAnalysis(),  //всего сессий сна
                    new MinSessionDurationAnalysis(),  //мин. продолжительность сна
                    new MaxSessionDurationAnalysis(),  //макс. продолжительность сна
                    new AverageSessionDurationAnalysis(),  //средняя продолжительность сна
                    new BadQualitySessionsAnalysis() //кол-во сессий с плохим качеством сна
            );

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Укажите путь к файлу с логом сна");
            return;
        }

        SleepTrackerApp app = new SleepTrackerApp();
        List<SleepingSession> sessions;
        try {
            sessions = app.readSessions(args[0]);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            return;
        }

        app.analyses.stream()
                .map(analysis -> analysis.apply(sessions))
                .forEach(result ->
                        System.out.println(
                                result.getDescription() + ": " + result.getValue()
                        )
                );
    }

    /*
     Загружает файл с логом сна, используя BufferedReader и try-with-resources
     */
    private List<SleepingSession> readSessions(String path) throws IOException {

        try (BufferedReader reader =
                     Files.newBufferedReader(Path.of(path))) {

            return reader.lines()
                    .map(this::parseLine)
                    .toList();
        }
    }

    /*
     Преобразует строку файла в объект SleepingSession
     */
    private SleepingSession parseLine(String line) {

        String[] parts = line.split(";");

        return new SleepingSession(
                LocalDateTime.parse(parts[0], FORMATTER),
                LocalDateTime.parse(parts[1], FORMATTER),
                SleepQuality.valueOf(parts[2])
        );
    }
}
