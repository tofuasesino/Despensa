package com.example.despensa.controller;

import com.example.despensa.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "registro", value = "/registro-servlet")
public class RegistroServlet extends HttpServlet {

    @Resource(name="jdbc/Despensa")
    private DataSource connectionPool;
    private Usuario usuario;
    private UsuarioController usuarioController;

    @Override
    public void init() throws ServletException {
        try {

            InitialContext initialContext = new InitialContext();
            Context context = (Context) initialContext.lookup("java:comp/env");
            connectionPool = (DataSource) context.lookup("jdbc/Despensa");
            usuarioController = new UsuarioController(connectionPool);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // Se hashea la contraseña para protegerla.
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        // La sal se añadirá a la base de datos junto al nombre de usuario y la contraseña hasheada.
        usuario = new Usuario(username, hashedPassword, salt);
        if(!usuarioController.checkUserRegister(usuario)) {
            usuarioController.addUser(usuario);
            resp.sendRedirect(req.getContextPath() + "/login.html");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<h2>El nombre de usuario ''<i>" + usuario.getUsername() + "</i>'' ya existe.</h2>");
            out.println("</head>");
            out.println("<a href='" + req.getContextPath() + "/registro.html'>Volver</a>");

        }
    }

    @Override
    public void destroy() {

    }


}
