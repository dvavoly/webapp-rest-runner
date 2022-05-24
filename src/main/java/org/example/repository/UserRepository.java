package org.example.repository;

import org.example.entity.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<Integer, User> {
    Optional<User> findByName(String name);
}
