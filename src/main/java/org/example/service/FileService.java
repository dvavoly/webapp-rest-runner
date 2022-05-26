package org.example.service;

import org.example.dto.FileDto;
import org.example.dto.UserDto;
import org.example.entity.File;
import org.example.repository.FileRepository;
import org.example.repository.impl.FileRepositoryImpl;
import org.example.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

public class FileService {

    private static final FileService INSTANCE = new FileService();
    private final FileRepository fileRepository = FileRepositoryImpl.getInstance();

    private FileService() {
    }

    public static FileService getInstance() {
        return INSTANCE;
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

    public Optional<FileDto> getFileById(Integer fileId) {
        var foundFile = fileRepository.findById(fileId);
        return foundFile.map(file -> FileDto.builder()
                        .id(file.getId())
                        .fileName(file.getFileName())
                        .fileType(file.getFileType())
                        .fileDownloadUri(file.getFileDownloadUri())
                        .build())
                .or(Optional::empty);
    }

    public String getFileNameByFileId(Integer fileId) {
        if (fileId == null || fileId < 0) {
            throw new IllegalArgumentException("Id cannot be null or negative number");
        }
        var foundFile = fileRepository.findById(fileId);
        return foundFile.map(File::getFileName).orElse("Empty");
    }

    public void deleteFile(Integer fileId) {
        fileRepository.deleteById(fileId);
    }

    public String getUserNameByFileId(Integer fileId) {
        var file = fileRepository.findById(fileId);
        var uriParts = file.get().getFileDownloadUri().split("/");
        return uriParts[uriParts.length - 2];
    }

    public FileDto updateFileIntoDatabase(FileDto savedFile, Integer fileId) {
        var updatedFile = fileRepository.update(File.builder()
                .id(fileId)
                .fileName(savedFile.getFileName())
                .fileType(savedFile.getFileType())
                .fileDownloadUri(savedFile.getFileDownloadUri())
                .build());

        return FileDto.builder()
                .id(updatedFile.getId())
                .fileName(updatedFile.getFileName())
                .fileType(updatedFile.getFileType())
                .fileDownloadUri(updatedFile.getFileDownloadUri())
                .build();
    }
}
