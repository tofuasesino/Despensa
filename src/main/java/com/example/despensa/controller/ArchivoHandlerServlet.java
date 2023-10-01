package com.example.despensa.controller;

import com.example.despensa.model.Archivo;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;

@WebServlet(name = "archivo-servlet", value = "/archivo-servlet")
public class ArchivoHandlerServlet extends HttpServlet {

    @Resource(name = "jdbc/Despensa")
    private DataSource pool;
    private Archivo archivo;
    private ArchivoController archivoController;

    @Override
    public void init(ServletConfig config) throws ServletException {
            archivoController = new ArchivoController();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("accion");

        switch (action){
            case "Descargar":
                archivoController.downloadArchivo(resp, req);
                break;
            case "Eliminar":
                archivoController.deleteArchivo(resp, req);
                break;
        }

        RequestDispatcher rd = req.getRequestDispatcher("/home.jsp");
        rd.forward(req, resp);
    }

}
