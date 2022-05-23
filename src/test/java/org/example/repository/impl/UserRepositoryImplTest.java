package org.example.repository.impl;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserRepositoryImplTest {

    private final UserRepository repository = UserRepositoryImpl.getInstance();

    @Test
    void findAll() {
        var users = repository.findAll();
        users.forEach(System.out::println);
    }

    @Test
    void findById() {
        var byId = repository.findById(5);
        byId.ifPresent(System.out::println);
    }

    @Test
    void save() {
        var user = User.builder()
                .userName("user777")
                .build();
        var savedUser = repository.save(user);
        assertNotNull(savedUser.getId());
    }

    @Test
    void update() {
        int id = 12;
        var newUserName = "NewUser12Name";
        var user = User.builder()
                .id(id)
                .userName(newUserName)
                .build();

        var updatedUser = repository.update(user);
        assertEquals(id, updatedUser.getId());
        assertEquals(newUserName, updatedUser.getUserName());
    }

    @Test
    void deleteById() {
    }
}