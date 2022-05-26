package org.example.rest;

import com.google.gson.Gson;
import org.example.service.EventService;
import org.example.util.Constant;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.example.util.HttpUtil.sendResponse;

@WebServlet(name = "EventServlet", urlPatterns = "/api/v1/events")
public class EventController extends HttpServlet {

    private final EventService eventService = EventService.getInstance();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(Constant.APPLICATION_JSON);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var writer = resp.getWriter()) {
            eventService.getAllEvents().forEach(eventDto -> writer.println(gson.toJson(eventDto)));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var eventId = Optional.ofNullable(req.getHeader(Constant.EVENT_ID));
        var uploadTime = Optional.ofNullable(req.getHeader(Constant.UPLOAD_TIME));

        if (eventId.isEmpty() || uploadTime.isEmpty()) {
            sendResponse(HttpServletResponse.SC_NOT_FOUND, "Event id and Upload time is required", resp);
        }

        var updatedEvent = eventService.updateEvent(Integer.valueOf(eventId.get()), uploadTime.get());
        if (updatedEvent.getId() == null) {
            sendResponse(HttpServletResponse.SC_NOT_FOUND, "Id not found", resp);
            return;
        }
        resp.setContentType(Constant.APPLICATION_JSON);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var writer = resp.getWriter()) {
            writer.println(gson.toJson(updatedEvent));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var eventId = Optional.ofNullable(req.getHeader(Constant.EVENT_ID));
        if (eventId.isEmpty()) {
            sendResponse(HttpServletResponse.SC_NOT_FOUND, "Event id is required", resp);
        }
        eventService.deleteEvent(Integer.valueOf(eventId.get()));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }
}
