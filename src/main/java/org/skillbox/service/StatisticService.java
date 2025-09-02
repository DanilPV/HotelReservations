package org.skillbox.service;

import lombok.RequiredArgsConstructor;
import org.skillbox.model.Statistic;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.skillbox.repository.mongo.StatisticRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;

    public List<Statistic> getAllStatistics() {
        return statisticRepository.findAll();
    }

    public List<Statistic> getStatisticsByEventType(String eventType) {
        return statisticRepository.findByEventType(eventType);
    }

    public List<Statistic> getStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
        return statisticRepository.findByEventDateBetween(startDate, endDate);
    }

    public ByteArrayInputStream exportStatisticsToCsv() {
        List<Statistic> statistics = statisticRepository.findAll();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT)) {

            csvPrinter.printRecord("ID", "Event Type", "User ID", "Event Date", "Created At", "Event Data");

            for (Statistic stat : statistics) {
                csvPrinter.printRecord(
                        stat.getId(),
                        stat.getEventType(),
                        stat.getUserId(),
                        stat.getEventDate(),
                        stat.getCreatedAt(),
                        stat.getEventData().toString()
                );
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(" Не удалось экспортировать статистику в CSV", e);
        }
    }
}