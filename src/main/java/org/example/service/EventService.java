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

    private final EventRepository eventRepository = EventRepositoryImpl.getInstance();
    private final FileService fileService = FileService.getInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private EventService() {
    }

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> EventDto.builder()
                        .id(event.getId())
                        .uploadTime(event.getUploadTime().format(formatter))
                        .fileName(fileService.getFileNameByFileId(event.getFileId()))
                        .build())
                .toList();
    }

    public EventDto updateEvent(Integer eventId, String uploadTime) {

        var foundEvent = eventRepository.findById(eventId);
        if (foundEvent.isEmpty()) {
            return EventDto.builder().build();
        }

        var updatedEvent = eventRepository.update(Event.builder()
                .id(foundEvent.get().getId())
                .uploadTime(LocalDateTime.parse(uploadTime, formatter))
                .userId(foundEvent.get().getUserId())
                .fileId(foundEvent.get().getFileId())
                .build());

        return EventDto.builder()
                .id(updatedEvent.getId())
                .uploadTime(updatedEvent.getUploadTime().format(formatter))
                .fileName(fileService.getFileNameByFileId(updatedEvent.getFileId()))
                .build();
    }

    public void saveEvent(LocalDateTime time, Integer userId, Integer fileId) {

        var event = eventRepository.save(Event.builder()
                .uploadTime(time)
                .userId(userId)
                .fileId(fileId)
                .build());
    }

    public void deleteEvent(Integer eventId) {
        eventRepository.deleteById(eventId);
    }

    public static EventService getInstance() {
        return INSTANCE;
    }
}
