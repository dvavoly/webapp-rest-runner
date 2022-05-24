package org.example.service;

import org.example.dto.EventDto;
import org.example.entity.Event;
import org.example.repository.EventRepository;
import org.example.repository.impl.EventRepositoryImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventService {
    private static final EventService INSTANCE = new EventService();

    private final EventRepository repository = EventRepositoryImpl.getInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private EventService() {
    }

    public List<EventDto> getAllEvents() {
        return repository.findAll().stream()
                .map(event -> EventDto.builder()
                        .id(event.getId())
                        .uploadTime(event.getUploadTime().format(formatter))
                        .build())
                .toList();
    }

    public static EventService getInstance() {
        return INSTANCE;
    }

    public EventDto saveEvent(LocalDateTime time, Integer userId, Integer fileId) {
        var event = repository.save(Event.builder()
                .uploadTime(time)
                .userId(userId)
                .fileId(fileId)
                .build());
        return EventDto.builder()
                .id(event.getId())
                .uploadTime(event.getUploadTime().format(formatter))
                .build();
    }
}
