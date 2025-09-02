package org.skillbox.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "statistics")
@Data
public class Statistic {

    @Id
    private String id;

    @Field("event_type")
    private String eventType;

    @Field("user_id")
    private Long userId;

    @Field("event_data")
    private Object eventData;

    @Field("event_date")
    private LocalDate eventDate;

    @Field("created_at")
    private LocalDate createdAt;
}