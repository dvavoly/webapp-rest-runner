package org.example.service;

import org.example.dto.UserDto;
import org.example.repository.UserRepository;
import org.example.repository.impl.UserRepositoryImpl;

import java.util.List;

public class UserService {

    private static final UserService INSTANCE = new UserService();

    private final UserRepository userRepository = UserRepositoryImpl.getInstance();

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .userName(user.getUserName())
                        .build()
                ).toList();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
