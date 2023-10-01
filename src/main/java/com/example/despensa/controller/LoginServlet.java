package com.example.despensa.controller;

import com.example.despensa.model.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "login", value = "/login-servlet")
public class LoginServlet extends HttpServlet {

    private UsuarioController usuarioController;
    @Resource(name = "jdbc/Despensa")
    private DataSource connectionPool;
    private Usuario usuario;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext initialContext = new InitialContext();
            Context context = (Context) initialContext.lookup("java:comp/env");
            connectionPool = (DataSource) context.lookup("jdbc/Despensa");
            usuarioController = new UsuarioController(connectionPool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        usuario = new Usuario(username, password);
        boolean userLogged = usuarioController.checkUserLogin(usuario);

        if (userLogged) {
            HttpSession session = req.getSession();
            // Guardo el username del cliente en la sesión.
            session.setAttribute("username", username);

            String userHomeDirPath = System.getProperty("user.home") + File.separator + "Despensa" + File.separator + username;
            File userHomeDirectory = new File(userHomeDirPath);

            // Guarda la ruta en la sesión para poder utilizarla en otros servlets.
            session.setAttribute("ruta", userHomeDirectory);

            // Si no existen los directorios los crea.
            if (!userHomeDirectory.exists()) {
                userHomeDirectory.mkdirs();
            }
            String[] filenames = userHomeDirectory.list();
            req.setAttribute("archivos", filenames);

            // Lo envía a la página principal.
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/home.jsp");
            rd.forward(req, resp);
        } else {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<!DOCTYPE html>\n" +
                    "<html lang=\"es\">");
            out.println("<h1>¡Uy!</h1>");
            out.println("<p>Parece ser que este usuario no existe o todavía no ha sido validado :(</p>");
            out.println("<a href='" + req.getContextPath() + "/index.jsp'>Inicio</a>");
        }

    }

    @Override
    public void destroy() {
        super.destroy();
    }


}
