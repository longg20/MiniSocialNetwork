/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.controllers;

import longtt.daos.CommentDAO;
import longtt.daos.NotificationDAO;
import longtt.dtos.NotificationDTO;
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
public class DeleteCommentController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DeleteCommentController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String commentId = request.getParameter("txtCommentId");
            String articleId = request.getParameter("txtArticleId");
            String email = request.getParameter("txtEmail");
            CommentDAO dao = new CommentDAO();
            dao.deactiveComment(commentId);
            NotificationDAO notdao = new NotificationDAO();
            NotificationDTO notdto = new NotificationDTO(notdao.getNotificationID(), articleId, email, "Delete Comment");
            notdao.insertNotification(notdto);
        } catch (Exception e) {
            LOGGER.error("ERROR at DeleteCommentController");
        } finally {
            request.getRequestDispatcher("DetailController").forward(request, response);
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
