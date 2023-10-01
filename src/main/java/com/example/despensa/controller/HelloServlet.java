package com.example.despensa.controller;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private int visitorCount;

    public void init() {
        visitorCount = 0;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        visitorCount++;
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>You are the " + visitorCount + " visitor!</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}