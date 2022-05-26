package org.example.service;

import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.repository.impl.UserRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class UserService {

    private static final UserService INSTANCE = new UserService();

    private final UserRepository repository = UserRepositoryImpl.getInstance();

    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .userName(user.getUserName())
                        .build()
                ).toList();
    }

    public UserDto createUser(String userName) {
        var savedUser = repository.save(User.builder()
                .userName(userName)
                .build());
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .build();
    }

    public Optional<UserDto> getUserByUserName(String userName) {
        return repository.findAll().stream()
                .filter(user -> user.getUserName().equals(userName))
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .userName(user.getUserName())
                        .build())
                .findFirst()
                .or(Optional::empty);
    }

    public UserDto updateUser(Integer id, String userName) {
        var savedUser = repository.update(User.builder()
                .id(id)
                .userName(userName)
                .build());
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .build();
    }

    public void deleteUser(Integer id) {
        repository.deleteById(id);
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
