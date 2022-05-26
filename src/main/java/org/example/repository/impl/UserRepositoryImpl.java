package org.example.repository.impl;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private static final UserRepository INSTANCE = new UserRepositoryImpl();

    private UserRepositoryImpl() {
    }

    @Override
    public List<User> findAll() {
        try (var session = HibernateUtil.openSession()) {
            return session.createQuery("select u from User u", User.class).list();
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Id cannot be null or negative number");
        }
        try (var session = HibernateUtil.openSession()) {
            var query = session.createQuery("select u from User u where u.id=:id", User.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        try (var session = HibernateUtil.openSession()) {
            var tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        }
        return user;
    }

    @Override
    public User update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User or user id cannot be null");
        }
        try (var session = HibernateUtil.openSession()) {
            var tx = session.beginTransaction();
            var foundUser = session.find(User.class, user.getId());
            if (foundUser != null) {
                foundUser.setUserName(user.getUserName());
                foundUser.setEventEntities(user.getEventEntities());
                session.persist(foundUser);
                tx.commit();
                return foundUser;
            }
            return user;
        }
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        try (var session = HibernateUtil.openSession()) {
            var tx = session.beginTransaction();
            var foundUser = findById(id);
            foundUser.ifPresent(session::remove);
            tx.commit();
        }
    }

    public static UserRepository getInstance() {
        return INSTANCE;
    }
}
