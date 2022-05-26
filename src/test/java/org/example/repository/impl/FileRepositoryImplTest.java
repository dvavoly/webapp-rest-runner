package org.example.repository.impl;

import org.example.entity.File;
import org.example.repository.FileRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileRepositoryImplTest {

    private final FileRepository repository = FileRepositoryImpl.getInstance();

    private final File newFile = File.builder()
            .id(777)
            .fileName("TextFile.txt")
            .fileType("text/plain")
            .fileDownloadUri("http://storage.TextFile.txt")
            .build();

    @Test
    void getAllFiles() {
        files.forEach(System.out::println);
        var files = repository.findAll();
    }

    @Test
    void getFileById() {
        var foundFileById = repository.findById(11);
        if (foundFileById.isPresent()) {
            System.out.println("File found: " + foundFileById.get());
        } else {
            System.out.println("File not found");
        }
    }

    @Test
    void saveNewFile() {
        var newFile = File.builder()
                .fileName("TextFile.txt")
                .fileType("text/plain")
                .fileDownloadUri("http://storage.TextFile.txt")
                .build();
        var savedFile = repository.save(newFile);
        assertNotNull(savedFile.getId());
    }

    @Test
    void updateFile() {
        var updatedFile = repository.update(newFile);
    }

    @Test
    void deleteFileById() {
        repository.deleteById(15);
    }
}