<%-- 
    Document   : index
    Created on : Sep 15, 2020, 9:39:36 PM
    Author     : Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        
        <style>
            h2,h3,form { text-align:center; }
            a { text-align:center;display:block; }
        </style>
    </head>
    <body>
        <h2> Login </h2>
        
        <font color="red">
        <h3>            
            ${requestScope.INVALID.actionError}
            ${requestScope.INVALID.invalidError}
            ${requestScope.INVALID.loginError}
            ${requestScope.INVALID.emailError}<br/>
            ${requestScope.INVALID.passwordError}
        </h3>
        </font>
        
        <form action="MainController" method="POST">
            Email:<input type="text" name="txtEmail" value="${param.txtEmail}"/></br>
            Password:<input type="password" name="txtPassword"/></br>
            <input type="submit" name="action" value="Login"/>
        </form>
        
        <a href="SearchController">Back to Home Page</a>
    </body>
</html>
