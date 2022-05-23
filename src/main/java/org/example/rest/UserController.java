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

@WebServlet(name = "UserServlet", urlPatterns = "/api/v1/users")
public class UserController extends HttpServlet {

    private final UserService service = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var writer = resp.getWriter()) {
            service.getAllUsers().forEach(userDto -> writer.write(gson.toJson(userDto)));
        }
    }
}
