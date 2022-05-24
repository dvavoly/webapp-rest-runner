package org.example.rest;

import com.google.gson.Gson;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.EventService;
import org.example.service.FileService;
import org.example.service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet(name = "FileServlet", urlPatterns = "/api/v1/files")
@MultipartConfig
public class FileController extends HttpServlet {

    private final FileService fileService = FileService.getInstance();
    private final UserService userService = UserService.getInstance();
    private final EventService eventService = EventService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var writer = resp.getWriter()) {
            fileService.getAllFiles().forEach(file -> writer.println(gson.toJson(file)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        var userName = Optional.ofNullable(req.getHeader("userName"));
        if (userName.isEmpty()) {
            sendResponse(HttpServletResponse.SC_NOT_FOUND, "The User name is empty", resp);
            return;
        }

        var foundUser = userService.getUserByUserName(userName.get());
        if (foundUser.isEmpty()) {
            foundUser = Optional.of(userService.createUser(userName.get()));
        }

        var fileDto = fileService.persistFileToStorage(req, foundUser.get());
        fileDto = fileService.saveFileToDatabase(fileDto);
        eventService.saveEvent(LocalDateTime.now(), foundUser.get().getId(), fileDto.getId());
        sendResponse(HttpServletResponse.SC_NO_CONTENT, resp);
    }

    private void sendResponse(int statusCode, String message, HttpServletResponse resp) {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setStatus(statusCode);
        resp.setContentType("text/plain");
        try (var writer = resp.getWriter()) {
            writer.println(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponse(int statusCode, HttpServletResponse resp) {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setStatus(statusCode);
        resp.setContentType("text/plain");
    }
}
