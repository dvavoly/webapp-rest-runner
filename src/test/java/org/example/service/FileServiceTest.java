package org.example.service;

import org.example.repository.FileRepository;
import org.example.repository.impl.FileRepositoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileServiceTest {

    final FileRepository fileRepository = FileRepositoryImpl.getInstance();
    final FileService fileService = FileService.getInstance();

    @Test
    void givenFile_whenInvokeMethod_thanReturnUserName() {
        String userName = "User2";
        Integer userId = 2;
        assertEquals(userName, fileService.getUserNameByFileId(userId));
    }
}