<%@ page import="java.io.PrintWriter" %>
<%--
  Created by IntelliJ IDEA.
  User: whoami
  Date: 5/7/23
  Time: 19:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Despensa</title>
    <style>
        table, th, td {
            border: 1px solid #0f562d;
        }
    </style>
</head>
<body>

<%
    PrintWriter pw = response.getWriter();
    pw.println("<h1>¡Bienvenido a tu despensa!</h1>");
    pw.println("<h2>Aquí podrás ver, subir y descargar tus archivos de manera gratuita y fácil.</h2>");
    pw.println("<table width='50%'>" +
            "<tr>" +
            "<th>Nombre del archivo</th>" +
            "<th>Acción</th>" +
            "</tr>");
    String[] archivos = (String[]) request.getAttribute("archivos");
    for (int i = 0; i < archivos.length; i++) {
        pw.println("<tr>" +
                "<td>" +
                archivos[i] +
                "</td>" +
                "<td><form action='archivo-servlet' method='get'><input type='hidden' name='filename' value='" + archivos[i] + "'/>&nbsp<input type='submit' name='accion' value='Descargar'/>&nbsp&nbsp&nbsp&nbsp<input type='submit' name='accion' value='Eliminar'/></form></td>" +
                "</tr>");
    }
    pw.println("</table>");
%>


<form action="upload-servlet" method="post" enctype="multipart/form-data">
    <input type="file" name="file" required/><br>
    <input type="submit" value="Subir"/>
</form>

</body>
</html>
