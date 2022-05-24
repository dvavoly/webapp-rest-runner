package org.example.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.FileDto;
import org.example.dto.UserDto;
import org.example.entity.File;
import org.example.repository.FileRepository;
import org.example.repository.impl.FileRepositoryImpl;
import org.example.util.Constant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    public FileDto saveFileToDatabase(FileDto file) {
        var savedFile = fileRepository.save(File.builder()
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .fileDownloadUri(file.getFileDownloadUri())
                .build());
        
        return FileDto.builder()
                .id(savedFile.getId())
                .fileName(savedFile.getFileName())
                .fileType(savedFile.getFileType())
                .fileDownloadUri(savedFile.getFileDownloadUri())
                .build();
    }

    public FileDto persistFileToStorage(HttpServletRequest req, UserDto user) {
        FileDto result;
        try {
            var file = req.getPart("file");
            createFolderIfNotExist(Path.of(Constant.STORAGE_FOLDER, user.getUserName()));
            var savePath = Path.of(Constant.STORAGE_FOLDER, user.getUserName(), file.getSubmittedFileName());
            try (var inputStream = file.getInputStream()) {
                Files.copy(inputStream, savePath, StandardCopyOption.REPLACE_EXISTING);
            }

            result = FileDto.builder()
                    .fileName(file.getSubmittedFileName())
                    .fileType(Files.probeContentType(savePath))
                    .fileDownloadUri(savePath.toUri().toString())
                    .build();

        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void createFolderIfNotExist(Path storageFolder) {
        if (!Files.exists(storageFolder)) {
            try {
                Files.createDirectories(storageFolder);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public static FileService getInstance() {
        return INSTANCE;
    }
}
