package org.example.rest;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.FileService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "FileServlet", urlPatterns = "/api/v1/files")
public class FileController extends HttpServlet {

    private final FileService fileService = FileService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var writer = resp.getWriter()) {
            fileService.getAllFiles().forEach(file -> writer.write(gson.toJson(file)));
        }
    }
}
