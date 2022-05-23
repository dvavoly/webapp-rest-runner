package org.example.repository.impl;

import org.example.entity.Event;
import org.example.repository.EventRepository;
import org.example.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class EventRepositoryImpl implements EventRepository {

    private static final EventRepository INSTANCE = new EventRepositoryImpl();

    private EventRepositoryImpl() {
    }

    @Override
    public List<Event> findAll() {
        try (var session = HibernateUtil.openSession()) {
            return session.createQuery("select e from Event e", Event.class).list();
        }
    }

    @Override
    public Optional<Event> findById(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Id cannot be null or negative number");
        }
        try (var session = HibernateUtil.openSession()) {
            var query = session.createQuery("select e from Event e where e.id=:id", Event.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public Event save(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        try (var session = HibernateUtil.openSession()) {
            var tx = session.beginTransaction();
            session.persist(event);
            tx.commit();
            return event;
        }
    }

    @Override
    public Event update(Event event) {
        if (event == null || event.getId() == null) {
            throw new IllegalArgumentException("Event or event id cannot be null");
        }
        try (var session = HibernateUtil.openSession()) {
            var tx = session.beginTransaction();
            var foundEvent = session.find(Event.class, event.getId());
            if (foundEvent != null) {
                foundEvent.setFileId(event.getFileId());
                foundEvent.setUserId(event.getUserId());
                foundEvent.setUploadTime(event.getUploadTime());
                session.persist(foundEvent);
                tx.commit();
                return foundEvent;
            }
        }
        return event;
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        try (var session = HibernateUtil.openSession()) {
            var tx = session.beginTransaction();
            var foundEvent = findById(id);
            foundEvent.ifPresent(session::remove);
            tx.commit();
        }
    }

    public static EventRepository getInstance() {
        return INSTANCE;
    }
}
