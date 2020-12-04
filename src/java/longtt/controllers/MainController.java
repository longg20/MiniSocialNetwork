/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.controllers;

import longtt.dtos.ErrorObj;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Long
 */
public class MainController extends HttpServlet {

    private static final String LOGIN = "LoginController";
    private static final String LOGOUT = "LogoutController";
    private static final String REGISTER = "RegisterController";
    private static final String POST = "PostController";
    private static final String SEARCH = "SearchController";
    private static final String DETAIL = "DetailController";
    private static final String COMMENT = "CommentController";
    private static final String EMOTE = "EmoteController";
    private static final String DELETE_COMMENT = "DeleteCommentController";
    private static final String DELETE_ARTICLE = "DeleteArticleController";
    private static final String INVALID = "login.jsp";
    private static final String DELETE_NOTIFICATION = "DeleteNotificationController";
    private static final String PREV_PAGE = "SearchController";
    private static final String NEXT_PAGE = "SearchController";
    private static final Logger LOGGER = Logger.getLogger(MainController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = INVALID;
        try {
            String action = request.getParameter("action");
            if (action.equals("Login")) {
                url = LOGIN;
            } else if (action.equals("Logout")) {
                url = LOGOUT;
            } else if (action.equals("Register")) {
                url = REGISTER;
            } else if (action.equals("Post")) {
                url = POST;
            } else if (action.equals("Search")) {
                url = SEARCH;
            } else if (action.equals("Detail")) {
                url = DETAIL;
            } else if (action.equals("Comment")) {
                url = COMMENT;
            } else if (action.equals("Emote")) {
                url = EMOTE;
            } else if (action.equals("Delete Comment")) {
                url = DELETE_COMMENT;
            } else if (action.equals("Delete Article")) {
                url = DELETE_ARTICLE;
            } else if (action.equals("Got it")) {
                url = DELETE_NOTIFICATION;
            } else if (action.equals("Prev Page")) {
                url = PREV_PAGE;
            } else if (action.equals("Next Page")) {
                url = NEXT_PAGE;
            } else {
                ErrorObj errorObj = new ErrorObj();
                errorObj.setActionError("Invalid action");
                request.setAttribute("INVALID", errorObj);
            }
        } catch (Exception e) {
            LOGGER.error("ERROR at MainController");
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
