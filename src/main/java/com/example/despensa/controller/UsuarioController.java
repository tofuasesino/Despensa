package com.example.despensa.controller;

import com.example.despensa.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioController {
    private DataSource dataSource;

    public UsuarioController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addUser(Usuario usuario) {
        PreparedStatement prepStmt = null;

        try (Connection conn = dataSource.getConnection()) {

            String stmt = "INSERT INTO users (username, password, salt) VALUES (?, ?, ?)";
            prepStmt = conn.prepareStatement(stmt);

            prepStmt.setString(1, usuario.getUsername());
            prepStmt.setString(2, usuario.getPassword());
            prepStmt.setString(3, usuario.getSalt());

            prepStmt.execute();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkUserLogin(Usuario usuario) {
        boolean check = false;

        try (Connection conn = dataSource.getConnection()) {
            String stmt = "SELECT username, password, salt, valid FROM users WHERE username = ?";

            PreparedStatement prepStmt = conn.prepareStatement(stmt);
            prepStmt.setString(1, usuario.getUsername());

            ResultSet rs = prepStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("No existe o no coinciden las credenciales");
            } else {
                // Si el usuario no está validado no tiene permiso para acceder a la despensa.
                if (!rs.getBoolean("valid")) {
                    return false;
                }
                // Setea la sal recogida de la tabla en el usuario, recoge la contraseña del formulario y la hashea
                // con la sal, después comprueba en la base de datos si los hashes coinciden
                usuario.setSalt(rs.getString("salt"));
                String hashed = BCrypt.hashpw(usuario.getPassword(), usuario.getSalt());
                if (BCrypt.checkpw(usuario.getPassword(), hashed)) {
                    check = true;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return check;
    }

    public boolean checkUserRegister(Usuario usuario) {
        boolean userExists = true;

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement prepStmt;

            String stmt = "SELECT username FROM users WHERE username = ?";
            prepStmt = conn.prepareStatement(stmt);
            prepStmt.setString(1, usuario.getUsername());
            ResultSet result = prepStmt.executeQuery();

            if (!result.next()) {
                userExists = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userExists;
    }

    public int getUserId(Usuario usuario) {
        int id = 0;

        try (Connection conn = dataSource.getConnection()) {
            String stmt = "SELECT id FROM users WHERE username = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(stmt);
            preparedStmt.setString(1, usuario.getUsername());

            ResultSet rs = preparedStmt.executeQuery();

            while(rs.next()) {
                id = rs.getInt("id");
            }

        } catch(SQLException e) {

        }

        return 0;
    }

}
