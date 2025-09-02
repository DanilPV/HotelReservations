package org.skillbox.repository.mongo;

import org.skillbox.model.Statistic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatisticRepository extends MongoRepository<Statistic, String> {

    List<Statistic> findByEventType(String eventType);
    List<Statistic> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
}