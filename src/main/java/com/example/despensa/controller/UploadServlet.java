package com.example.despensa.controller;

import com.example.despensa.model.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;

@WebServlet(name = "upload", value = "/upload-servlet")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    @Resource(name = "jdbc/Despensa")
    DataSource connectionPool;
    ArchivoController archivoController;
    Usuario usuario;


    @Override
    public void init() throws ServletException {
        archivoController = new ArchivoController();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        archivoController.uploadArchivo(resp, req);


        // Reenvío a la página principal.
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/home.jsp");
        rd.forward(req, resp);


    }


}

