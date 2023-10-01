<%--
  Created by IntelliJ IDEA.
  User: whoami
  Date: 30/6/23
  Time: 18:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        html {
            /* background-color: #B0FFAD;*/
            background-color: #d2e7d6;
        }
        .mainDiv {
            height: 70%;
            display: flex;
            flex-direction: row;
            justify-content: center;
            align-items: center;
            gap: 20px;
            font-size: 20px;
            font-family: Roboto, Arial, sans-serif;
        }
        .cabesa {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            font-family: Roboto, Arial, sans-serif;
        }
    </style>
    <title>Despensa de Java</title>
</head>
<body>
<div class="cabesa">
    <h1>Despensa de Java</h1>
    <h3>Desarrollado por <a href ="https://github.com/tofuasesino">tofuasesino</a></h3>
</div>
<div class="mainDiv">
    <a href="registro.html">Registrarse</a>
    <a href="login.html">Iniciar sesión</a>
</div>

</body>
</html>
