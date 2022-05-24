package org.example.rest;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet(name = "UserServlet", urlPatterns = "/api/v1/users")
public class UserController extends HttpServlet {

    private final UserService service = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var writer = resp.getWriter()) {
            service.getAllUsers().forEach(userDto -> writer.println(gson.toJson(userDto)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        var userName = Optional.ofNullable(req.getHeader("userName"));
        if (userName.isEmpty()) {
            return;
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var newUser = service.createUser(userName.get());
        try (var writer = resp.getWriter()) {
            writer.println(gson.toJson(newUser));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        var id = Optional.ofNullable(req.getHeader("userId"));
        var userName = Optional.ofNullable(req.getHeader("userName"));
        if (id.isEmpty() || userName.isEmpty()) {
            return;
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var userDto = service.updateUser(Integer.valueOf(id.get()), userName.get());
        try (var writer = resp.getWriter()) {
            writer.println(gson.toJson(userDto));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var id = Optional.ofNullable(req.getHeader("userId"));
        if (id.isEmpty()) {
            return;
        }
        service.deleteUser(Integer.valueOf(id.get()));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }
}
