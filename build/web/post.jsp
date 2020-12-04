<%-- 
    Document   : post
    Created on : Sep 20, 2020, 11:39:13 AM
    Author     : Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Post Article</title>
        <style>
            h1 { text-align:center; }
            form { text-align:center; }
            a { text-align:center;display:block; } 
        </style>
    </head>
    <body>
        <c:if test="${empty sessionScope.USER}">
            <c:redirect url="login.jsp"/>
        </c:if>
        
        <h1>Post Article</h1>

        <form action="MainController" method="POST">
            <table align="center" border="1">
                <tbody>
                    <tr>
                        <td>Title</td>
                        <td><input type="text" name="txtTitle" value="${param.txtTitle}"/></td>
                    </tr>
                    <tr>
                        <td>Description</td>
                        <td><input type="text" name="txtDescription" value="${param.txtDescription}"/></td>
                    </tr>
                    <tr>
                        <td>Image</td>
                        <td><input type="text" name="txtImage" value="${param.txtImage}"/></td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="txtEmail" value="${sessionScope.USER.email}"/>
            <input type="submit" name="action" value="Post"/>
        </form>
        <font color="red">
        ${requestScope.INVALID.titleError}<br/>
        </font>
        <font color="red">
        ${requestScope.INVALID.descriptionError}<br/>
        </font>
        <font color="red">
        ${requestScope.INVALID.imageError}<br/>
        </font>
    </body>

    <c:url var="homeLink" value="MainController">
        <c:param name="action" value="Search"/>
    </c:url>
    <a href="${homeLink}">Back to Home</a>
</html>
