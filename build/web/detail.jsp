<%-- 
    Document   : detail
    Created on : Sep 20, 2020, 5:17:01 PM
    Author     : Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Article Detail</title>

        <style>
            a.return { text-align:center;display:block; }
            #deleteIcon{
                float: right;
            }
        </style>
    </head>
    <body style="background-color:#cccccc">
        
        <c:if test="${empty sessionScope.USER}">
            <c:redirect url="login.jsp"/>
        </c:if>
            
        <table align="center" style="border:1px solid black; background-color:#ffffff">
            <tbody>
                <tr>
                    <td>
                        <font style="font-size:40px; font-weight:bold">
                        ${requestScope.ARTICLE.title}
                        </font>
                    </td>
                </tr>
                <tr>
                    <td>
                        <c:url var="DeleteArticleLink" value="MainController">
                            <c:param name="action" value="Delete Article"/>
                            <c:param name="txtArticleId" value="${requestScope.ARTICLE.id}"/>
                        </c:url>

                        <c:if test="${sessionScope.USER.email eq requestScope.ARTICLE.email}" var="deleteArticleCheck">
                            Author:${requestScope.ARTICLE.author}
                            <a href="${DeleteArticleLink}" id="deleteIcon" 
                               onclick="return confirm('Are you sure you want to delete this Article?')"><img src="images/trash-can.png" width="13" height="13"/></a>
                            </c:if>

                        <c:if test="${!deleteArticleCheck}">
                            Author:${requestScope.ARTICLE.author}
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>Posted on:${requestScope.ARTICLE.date}</td>
                </tr>
                <tr>
                    <td>
                        <font style="font-size:25px">
                        ${requestScope.ARTICLE.description}
                        </font>
                    </td>
                </tr>
                <tr>
                    <td><img src="${requestScope.ARTICLE.image}" width="350" height="350"/></td>
                </tr>
                <tr>
                    <td id="emote">
                        <c:url var="likeLink" value="MainController">
                            <c:param name="txtEmail" value="${sessionScope.USER.email}"/>
                            <c:param name="txtArticleId" value="${requestScope.ARTICLE.id}"/>
                            <c:param name="emote" value="like"/>
                            <c:param name="action" value="Emote"/> 
                        </c:url>

                        <c:if test="${requestScope.EMOTE.like eq true}" var="likeCheck">
                            <a href="${likeLink}"><img src="images/like-black.png" height="25" width="25"/>${requestScope.LIKE}</a>
                            </c:if>

                        <c:if test="${!likeCheck}">
                            <a href="${likeLink}"><img src="images/like-white.png" height="25" width="25"/>${requestScope.LIKE}</a>
                            </c:if>

                        <c:url var="dislikeLink" value="MainController">
                            <c:param name="txtEmail" value="${sessionScope.USER.email}"/>
                            <c:param name="txtArticleId" value="${requestScope.ARTICLE.id}"/>
                            <c:param name="emote" value="dislike"/>
                            <c:param name="action" value="Emote"/> 
                        </c:url>

                        <c:if test="${requestScope.EMOTE.dislike eq true}" var="dislikeCheck">
                            <a href="${dislikeLink}"><img src="images/dislike-black.png" height="25" width="25"/>${requestScope.DISLIKE}</a>
                            </c:if>

                        <c:if test="${!dislikeCheck}">
                            <a href="${dislikeLink}"><img src="images/dislike-white.png" height="25" width="25"/>${requestScope.DISLIKE}</a>
                            </c:if>    
                    </td>
                </tr>

                <c:forEach var="comment" items="${requestScope.COMMENT}" varStatus="counter">

                    <c:url var="deleteCommentLink" value="MainController">
                        <c:param name="action" value="Delete Comment"/>
                        <c:param name="txtCommentId" value="${comment.id}"/>
                        <c:param name="txtArticleId" value="${requestScope.ARTICLE.id}"/>
                        <c:param name="txtEmail" value="${comment.email}"/>
                    </c:url>

                    <tr style="background-color:#e5e5e5">
                        <td id="${comment.id}">
                            <c:if test="${comment.email eq sessionScope.USER.email}" var="deleteCommentCheck">
                                <b>${comment.name}</b> - <font style="font-size:12px">${comment.date}</font><br/>
                                ${comment.content}
                                <a href="${deleteCommentLink}" id="deleteIcon"
                                   onclick="return confirm('Are you sure you want to delete this Comment?')"><img src="images/trash-can.png" width="13" height="13"/></a>
                                </c:if>

                            <c:if test="${!deleteCommentCheck}">
                                <b>${comment.name}</b> - <font style="font-size:12px">${comment.date}</font><br/>
                                ${comment.content}
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                        <form action="MainController" method="POST">
                            Post a Comment:<input type="text" name="txtComment"/>
                            <input type="hidden" name="txtEmail" value="${sessionScope.USER.email}"/>
                            <input type="hidden" name="txtArticleId" value="${requestScope.ARTICLE.id}"/>
                            <input type="hidden" name="txtId" value="${requestScope.ARTICLE.id}"/>
                            <input type="submit" name="action" value="Comment"/>
                        </form>
                    </td>
                </tr>
            </tbody>
        </table><br/>

        <c:url var="homeLink" value="MainController">
            <c:param name="action" value="Search"/>
        </c:url>
        <a class="return" href="${homeLink}">Return to Home</a>
    </body>
</html>
