<%-- 
    Document   : home.jsp
    Created on : Sep 19, 2020, 6:14:38 PM
    Author     : Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>

        <style>
            h1,h2 { text-align:center; }
            form { text-align:center; }
            #page { display:inline; }
        </style>
    </head>
    <body style="background-color:#cccccc">

        <h1> Social Network </h1>

        <c:if test="${empty sessionScope.USER}" var="loginCheck">

            <form action="register.jsp" method="POST">
                <input type="submit" value="Create new Account"/>
            </form>
            <form action="login.jsp" method="POST">
                <input type="submit" value="Login"/>
            </form>

        </c:if>

        <c:if test="${!loginCheck}">
            <h2>
                Welcome, ${sessionScope.USER.name}. 
                <c:url var="logoutLink" value="MainController">
                    <c:param name="action" value="Logout"/>
                </c:url>
                <a href="${logoutLink}">Log out</a>
                <form action="post.jsp" method="POST">
                    <input type="submit" value="Post an Article"/>
                </form>
            </h2>
        </c:if>

        <font color="red">
        <h2>${requestScope.NOTICE}</h2>
        </font><br/>

        <c:if test="${!loginCheck}">
            <c:if test="${not empty requestScope.NOTIFICATION}" var="notificationList">
                <table align="center" border="1">
                    <thead>
                        <tr>
                            <th>Notifications</th>
                            <th>Remove</th>
                        </tr>
                    </thead>
                    <tbody>

                        <c:forEach var="notification" items="${requestScope.NOTIFICATION}">

                            <c:url value="DetailController" var="notificationDetailLink">
                                <c:param name="action" value="Detail"/>
                                <c:param name="txtArticleId" value="${notification.articleId}"/>
                            </c:url>

                            <tr>
                                <td>
                                    <a href="${notificationDetailLink}">
                                        <c:if test="${notification.type eq 'Like'}">
                                            <font style="font-size:12px">${notification.date}</font><br/>
                                            <b>${notification.name}</b> liked your article <b>${notification.title}</b>.
                                        </c:if>

                                        <c:if test="${notification.type eq 'Dislike'}">
                                            <font style="font-size:12px">${notification.date}</font><br/>
                                            <b>${notification.name}</b> dislike your article <b>${notification.title}</b>.
                                        </c:if>

                                        <c:if test="${notification.type eq 'Comment'}">
                                            <font style="font-size:12px">${notification.date}</font><br/>
                                            <b>${notification.name}</b> commented on your article <b>${notification.title}</b>.
                                        </c:if>

                                        <c:if test="${notification.type eq 'Delete Comment'}">
                                            <font style="font-size:12px">${notification.date}</font><br/>
                                            <b>${notification.name}</b> deleted their comment on your article <b>${notification.title}</b>.
                                        </c:if>

                                        <c:if test="${notification.type eq 'Unlike'}">
                                            <font style="font-size:12px">${notification.date}</font><br/>
                                            <b>${notification.name}</b> un-liked your article <b>${notification.title}</b>.
                                        </c:if>

                                        <c:if test="${notification.type eq 'Undislike'}">
                                            <font style="font-size:12px">${notification.date}</font><br/>
                                            <b>${notification.name}</b> un-disliked your article <b>${notification.title}</b>.
                                        </c:if>
                                    </a>
                                </td>

                                <td>
                                    <form action="MainController" method="POST">
                                        <input type="submit" name="action" value="Got it"/>
                                        <input type="hidden" name="txtSearch" value="${param.txtSearch}"/>
                                        <input type="hidden" name="txtNotificationId" value="${notification.id}"/>
                                    </form>
                                </td>
                            </tr>

                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <c:if test="${!notificationList}">
                No notifications found.
            </c:if>

            <br/>
            <form action="MainController" method="POST">
                Search Article:<input type="text" name="txtSearch" value="${param.txtSearch}"/>
                <input type="submit" name="action" value="Search"/>
            </form><br/>
        </c:if>

        <c:if test="${not empty requestScope.INFO}" var="checkList">

            <div style="text-align:center;">
                Displaying <b>${requestScope.ARTICLE_CURRENT}/${requestScope.ARTICLE_MAX}</b> Articles.<br/>
                <form action="MainController" method="POST" id='page'>
                    <input type="submit" name="action" value="Prev Page"/>
                    <input type="hidden" name="txtPage" value="${requestScope.PAGE_CURRENT}"/>
                    <input type="hidden" name="movePage" value="prev"/>
                    <input type="hidden" name="txtSearch" value="${param.txtSearch}"/>
                </form>
                <b>Page ${requestScope.PAGE_CURRENT}/${requestScope.PAGE_MAX}</b>
                <form action="MainController" method="POST" id='page'>
                    <input type="submit" name="action" value="Next Page"/>
                    <input type="hidden" name="txtPage" value="${requestScope.PAGE_CURRENT}"/>
                    <input type="hidden" name="movePage" value="next"/>
                    <input type="hidden" name="txtSearch" value="${param.txtSearch}"/>
                </form>
            </div>
            <br/>
            <c:forEach var="article" items="${requestScope.INFO}" varStatus="counter">
                <table style="border:1px solid black; background-color:#ffffff; width: 23%; float:left; margin: 1%">
                    <tbody>
                        <tr>
                            <td>
                                <c:url var="detailLink" value="MainController">
                                    <c:param name="txtArticleId" value="${article.id}"/>
                                    <c:param name="action" value="Detail"/>
                                </c:url>

                                <a href="${detailLink}">
                                    <font style="font-size:30px; font-weight:bold">
                                    ${article.title}
                                    </font>
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td>Author: ${article.author}</td>
                        </tr>
                        <tr>
                            <td>Posted on: ${article.date}</td>
                        </tr>
                        <tr>
                            <td>
                                <font style="font-size:20px">
                                ${article.description}
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <a href="${detailLink}">
                                    <img src="${article.image}" height="335px" width="335px" style="border: 1px solid black"/>
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style="float:right">
                                    <a href="${detailLink}"><img src="images/like-white.png" width="15" height="15"/>(${article.like})</a>|
                                    <a href="${detailLink}"><img src="images/dislike-white.png" width="15" height="15"/>(${article.dislike})</a>|
                                    <a href="${detailLink}">Comment(${article.comment})</a>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </c:forEach>

        </c:if>
        <c:if test="${!checkList}">
            <font color="red">
            No articles found.
            </font>
        </c:if>
    </body>
</html>
