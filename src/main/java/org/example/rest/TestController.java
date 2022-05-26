package org.example.rest;

import org.example.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "TestServlet", urlPatterns = "/test")
public class TestController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String url = req.getRequestURL().toString();

        // Getting servlet request query string.
        String queryString = req.getQueryString();

        // Getting request information without the hostname.
        String uri = req.getRequestURI();

        // Below we extract information about the request object path
        // information.
        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int portNumber = req.getServerPort();
        String contextPath = req.getContextPath();
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        String query = req.getQueryString();

        resp.setContentType(Constant.TEXT_PLAIN);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var writer = resp.getWriter()) {
            writer.println(getServletContext().getRealPath(""));
            writer.println("Url: " + url + "<br/>");
            writer.println("Uri: " + uri + "<br/>");
            writer.println("Scheme: " + scheme + "<br/>");
            writer.println("Server Name: " + serverName + "<br/>");
            writer.println("Port: " + portNumber + "<br/>");
            writer.println("Context Path: " + contextPath + "<br/>");
            writer.println("Servlet Path: " + servletPath + "<br/>");
            writer.println("Path Info: " + pathInfo + "<br/>");
            writer.println("Query: " + query);
        }
    }
}
