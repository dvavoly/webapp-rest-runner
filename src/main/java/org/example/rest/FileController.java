package org.example.rest;

import com.google.gson.Gson;
import org.example.service.EventService;
import org.example.service.FileService;
import org.example.service.UserService;
import org.example.util.Constant;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.example.util.HttpUtil.sendResponse;

@WebServlet(name = "FileServlet", urlPatterns = "/api/v1/files")
@MultipartConfig
public class FileController extends HttpServlet {

    private final FileService fileService = FileService.getInstance();
    private final UserService userService = UserService.getInstance();
    private final EventService eventService = EventService.getInstance();

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Constant.APPLICATION_JSON);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var writer = resp.getWriter()) {
            var fileId = Optional.ofNullable(req.getHeader(Constant.FILE_ID));
            if (fileId.isPresent()) {
                var file = fileService.getFileById(Integer.valueOf(fileId.get()));
                file.ifPresentOrElse(
                        fileDto -> writer.println(gson.toJson(fileDto)),
                        () -> sendResponse(HttpServletResponse.SC_NOT_FOUND, "The File does not exist", resp));
                return;
            }
            fileService.getAllFiles().forEach(file -> writer.println(gson.toJson(file)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var userName = Optional.ofNullable(req.getHeader(Constant.USER_NAME));
        if (userName.isEmpty()) {
            sendResponse(HttpServletResponse.SC_NOT_FOUND, "The User name is required", resp);
            return;
        }

        var foundUser = userService.getUserByUserName(userName.get());
        if (foundUser.isEmpty()) {
            foundUser = Optional.of(userService.createUser(userName.get()));
        }

        var savedFile = fileService.persistFileToStorage(req, foundUser.get());
        savedFile = fileService.saveFileToDatabase(savedFile);
        eventService.saveEvent(LocalDateTime.now(), foundUser.get().getId(), savedFile.getId());
        resp.setContentType(Constant.APPLICATION_JSON);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var writer = resp.getWriter()) {
            writer.println(gson.toJson(savedFile));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var fileId = Optional.ofNullable(req.getHeader(Constant.FILE_ID));
        if (fileId.isEmpty()) {
            sendResponse(HttpServletResponse.SC_NOT_FOUND, "The File id is required", resp);
            return;
        }

        String userName = fileService.getUserNameByFileId(Integer.valueOf(fileId.get()));
        var foundUser = userService.getUserByUserName(userName);
        var savedFile = fileService.persistFileToStorage(req, foundUser.get());
        savedFile = fileService.updateFileIntoDatabase(savedFile, Integer.valueOf(fileId.get()));
        resp.setContentType(Constant.APPLICATION_JSON);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var writer = resp.getWriter()) {
            writer.println(gson.toJson(savedFile));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var id = Optional.ofNullable(req.getHeader(Constant.FILE_ID));
        if (id.isEmpty()) {
            sendResponse(HttpServletResponse.SC_NOT_FOUND, "File id is required", resp);
            return;
        }
        fileService.deleteFile(Integer.valueOf(id.get()));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }
}
