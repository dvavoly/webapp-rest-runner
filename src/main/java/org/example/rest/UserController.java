package org.example.rest;

import com.google.gson.Gson;
import org.example.service.UserService;
import org.example.util.Constant;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.example.util.HttpUtil.sendResponse;

@WebServlet(name = "UserServlet", urlPatterns = "/api/v1/users")
public class UserController extends HttpServlet {

    private final UserService service = UserService.getInstance();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Constant.APPLICATION_JSON);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        var userId = Optional.ofNullable(req.getHeader(Constant.USER_ID));

        try (var writer = resp.getWriter()) {
            if (userId.isPresent()) {
                var idOfUser = Integer.valueOf(userId.get());
                var user = service.getUserById(idOfUser);
                var events = service.getAllEventsByUserId(idOfUser);
                writer.println(gson.toJson(user.get()));
                writer.println(gson.toJson(events));
                return;
            }

            service.getAllUsers().forEach(userDto -> writer.println(gson.toJson(userDto)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var userName = Optional.ofNullable(req.getHeader(Constant.USER_NAME));
        if (userName.isEmpty()) {
            sendResponse(HttpServletResponse.SC_NOT_FOUND, "User name is required", resp);
            return;
        }
        resp.setContentType(Constant.APPLICATION_JSON);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var newUser = service.createUser(userName.get());
        try (var writer = resp.getWriter()) {
            writer.println(gson.toJson(newUser));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var id = Optional.ofNullable(req.getHeader(Constant.USER_ID));
        var userName = Optional.ofNullable(req.getHeader(Constant.USER_NAME));
        if (id.isEmpty() || userName.isEmpty()) {
            sendResponse(HttpServletResponse.SC_NOT_FOUND, "User id or User name is required", resp);
            return;
        }
        resp.setContentType(Constant.APPLICATION_JSON);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        var userDto = service.updateUser(Integer.valueOf(id.get()), userName.get());
        try (var writer = resp.getWriter()) {
            writer.println(gson.toJson(userDto));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var id = Optional.ofNullable(req.getHeader(Constant.USER_ID));
        if (id.isEmpty()) {
            sendResponse(HttpServletResponse.SC_NOT_FOUND, "User id is required", resp);
            return;
        }
        service.deleteUser(Integer.valueOf(id.get()));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }
}
