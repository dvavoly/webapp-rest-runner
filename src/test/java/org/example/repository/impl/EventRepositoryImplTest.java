package org.example.repository.impl;

import org.example.entity.Event;
import org.example.repository.EventRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventRepositoryImplTest {

    private final EventRepository repository = EventRepositoryImpl.getInstance();

    @Test
    void findAll() {
        var events = repository.findAll();
        events.forEach(System.out::println);
    }

    @Test
    void findById() {
        int id = 12;
        var byId = repository.findById(id);
        assertEquals(id, byId.get().getId());
    }

    @Test
    void save() {
        int uId = 4;
        int fId = 15;
        LocalDateTime time = LocalDateTime.now();
        var event = Event.builder()
                .userId(uId)
                .fileId(fId)
                .uploadTime(time)
                .build();
        var savedEvent = repository.save(event);
        assertNotNull(savedEvent.getId());
        assertEquals(fId, savedEvent.getFileId());
        assertEquals(uId, savedEvent.getUserId());
        assertEquals(time, savedEvent.getUploadTime());
    }

    @Test
    void update() {
        var uploadTime = LocalDateTime.now();
        var event = Event.builder()
                .id(12)
                .userId(5)
                .fileId(13)
                .uploadTime(uploadTime)
                .build();
        var updatedEvent = repository.update(event);
        assertEquals(event.getId(), updatedEvent.getId());
        assertEquals(event.getUserId(), updatedEvent.getUserId());
        assertEquals(event.getFileId(), updatedEvent.getFileId());
    }

    @Test
    void deleteById() {
        var expected = repository.findAll().size() - 1;
        repository.deleteById(repository.findAll().get(0).getId());
        var actual = repository.findAll().size();
        assertEquals(expected, actual);
    }
}