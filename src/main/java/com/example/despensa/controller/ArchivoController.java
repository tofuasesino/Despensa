package com.example.despensa.controller;

import com.example.despensa.model.Archivo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;

public class ArchivoController {

    private DataSource dataSource;

    public ArchivoController() {

    }

    public ArchivoController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteArchivo(HttpServletResponse resp, HttpServletRequest req) {
        HttpSession session = req.getSession();
        String filename = req.getParameter("filename");
        File userHomeDir = (File) session.getAttribute("ruta");

        File fullPathToFile = new File(userHomeDir.getPath() + File.separator + filename);
        fullPathToFile.delete();

        // Actualizo de nuevo la lista de archivos.
        String[] archivos = userHomeDir.list();
        req.setAttribute("archivos", archivos);
    }

    public void uploadArchivo(HttpServletResponse resp, HttpServletRequest req) {
        HttpSession session = req.getSession();

        // Guardo el archivo o "Part" en una variable de tipo part para poder recuperar su nombre y su valor.
        Part part = null;
        try {
            part = req.getPart("file");
        } catch (IOException e) {
            System.out.println("un error");
        } catch (ServletException e) {
            System.out.println("un errorcito");
        }
        // Recupero la ruta del usuario en el filesystem.
        File userHomeDir = (File) session.getAttribute("ruta");

        String nombreArchivo = part.getSubmittedFileName();


        // Cojo el inputStream desde donde se recibe el archivo.
        // En el try-with-resources abro los streams para que se cierren automáticamente al finalizar.
        // El input lo coge del Part de UploadServlet, el Output lo seteo a la ruta del usuario + nombre del archivo a subir.
        try (InputStream inputStream = part.getInputStream();
             InputStream in = new BufferedInputStream(inputStream);
             FileOutputStream fos = new FileOutputStream(userHomeDir.getPath() + File.separator + nombreArchivo)) {

            // Variable de tipo int para guardar el numero de bytes que se copiaran en el buffer y en el filesystem.
            int count;

            // Buffer de datos en donde se copiarán los bytes del archivo.
            byte[] buffer = new byte[16 * 1024];

            // Bucle que recoge en count el numero de bytes copiados a la vez que comprueba que no da -1 (final de archivo).
            // A cada vuelta de bucle copia en el stream de salida el número de bytes (count) que se han copiado al búfer (buffe).
            while ((count = in.read(buffer)) != -1) {
                fos.write(buffer, 0, count);
            }

        } catch (Exception e) {
            System.out.println("Ups! Un error.");
        }
        // Vuelvo a guardar en la sesión la lista actualizada de los archivos que tiene el usuario.
        String[] archivos = userHomeDir.list();
        req.setAttribute("archivos", archivos);
    }

    public void downloadArchivo(HttpServletResponse resp, HttpServletRequest req) {
        String filename = req.getParameter("filename");

        HttpSession session = req.getSession();
        File userHomeDir = (File) session.getAttribute("ruta");
        String fileExtension = filename.split("\\.", 2)[1];

        File archivo = new File(userHomeDir.getPath() + File.separator + filename);

        switch (fileExtension) {
            case "png": case "jpeg": case "jpg": case "heic":
                resp.setContentType("image/" + fileExtension);
                break;
            case "txt":
                resp.setContentType("text/plain");
                break;
            case "html":
                resp.setContentType("text/html");
                break;
            default:
                resp.setContentType("application/" + fileExtension);
        }
        resp.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");

        try (InputStream in = new FileInputStream(archivo);
             OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[1048];
            int numBytesCount;
            while((numBytesCount = in.read(buffer)) != -1) {
                out.write(buffer, 0, numBytesCount);
            }
        } catch (Exception e) {
            System.out.println("Error recuperando el archivo a descargar");
        }

    }
}
