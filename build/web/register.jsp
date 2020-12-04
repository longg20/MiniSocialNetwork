<%-- 
    Document   : register
    Created on : Sep 19, 2020, 2:11:09 PM
    Author     : Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Page</title>
        <style>
            h1, form { text-align:center; }
            a { text-align:center;display:block; }
        </style>
    </head>
    <body>
        <h1>Create new Account</h1>
        <form method="POST" action="MainController">
            Name:<input type="text" name="txtName" value="${param.txtName}"/><br/>
            <font color="red">
            ${requestScope.INVALID.nameError}
            </font></br>
            Email:<input type="email" name="txtEmail" value="${param.txtEmail}"/><br/>
            <font color="red">
            ${requestScope.INVALID.emailError}
            </font></br>
            Password:<input type="password" name="txtPassword"/><br/>
            <font color="red">
            ${requestScope.INVALID.passwordError}
            </font></br>
            Confirm:<input type="password" name="txtConfirm"/><br/>
            <font color="red">
            ${requestScope.INVALID.confirmError}
            </font></br>
            <input type="submit" name="action" value="Register"/>
        </form><br/>

        <a href="SearchController">Return to Home Page</a>
    </body>
</html>
