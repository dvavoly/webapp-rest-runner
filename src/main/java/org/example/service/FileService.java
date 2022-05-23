package org.example.service;

import org.example.dto.FileDto;
import org.example.repository.FileRepository;
import org.example.repository.impl.FileRepositoryImpl;

import java.util.List;

public class FileService {

    private static final FileService INSTANCE = new FileService();
    private final FileRepository fileRepository = FileRepositoryImpl.getInstance();

    private FileService() {
    }

    public List<FileDto> getAllFiles() {
        return fileRepository.findAll().stream()
                .map(file -> FileDto.builder()
                        .id(file.getId())
                        .fileName(file.getFileName())
                        .fileType(file.getFileType())
                        .fileDownloadUri(file.getFileDownloadUri())
                        .build())
                .toList();
    }

    public static FileService getInstance() {
        return INSTANCE;
    }
}
