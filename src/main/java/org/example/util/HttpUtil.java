package org.example.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class HttpUtil {

    private HttpUtil() {
    }

    public static void sendResponse(int statusCode, String message, HttpServletResponse resp) {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setStatus(statusCode);
        resp.setContentType(Constant.TEXT_PLAIN);
        try (var writer = resp.getWriter()) {
            writer.println(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendResponse(int statusCode, HttpServletResponse resp) {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setStatus(statusCode);
        resp.setContentType(Constant.TEXT_PLAIN);
    }

}
