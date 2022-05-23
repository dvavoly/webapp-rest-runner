package org.example.service;

import org.example.dto.EventDto;
import org.example.repository.EventRepository;
import org.example.repository.impl.EventRepositoryImpl;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventService {
    private static final EventService INSTANCE = new EventService();

    private final EventRepository eventRepository = EventRepositoryImpl.getInstance();
    private EventService() {
    }

    public List<EventDto> getAllEvents() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return eventRepository.findAll().stream()
                .map(event -> EventDto.builder()
                        .id(event.getId())
                        .uploadTime(event.getUploadTime().format(formatter))
                        .build())
                .toList();
    }

    public static EventService getInstance() {
        return INSTANCE;
    }
}
