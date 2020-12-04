/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.controllers;

import longtt.daos.ArticleDAO;
import longtt.daos.CommentDAO;
import longtt.daos.EmoteDAO;
import longtt.dtos.AccountDTO;
import longtt.dtos.ArticleDTO;
import longtt.dtos.ErrorObj;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Long
 */
public class DetailController extends HttpServlet {

    private static final String SUCCESS = "detail.jsp";
    private static final String INVALID = "SearchController";
    private static final String GUEST = "login.jsp";
    private static final Logger LOGGER = Logger.getLogger(DetailController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = INVALID;
        try {
            HttpSession session = request.getSession();
            AccountDTO accdto = (AccountDTO) session.getAttribute("USER");
            if (accdto == null) {
                url = GUEST;
                ErrorObj errorObj = new ErrorObj();
                errorObj.setInvalidError("You must Login to View Article's Detail.");
                request.setAttribute("INVALID", errorObj);
            } else {
                String articleId = request.getParameter("txtArticleId");
                ArticleDAO artdao = new ArticleDAO();
                CommentDAO comdao = new CommentDAO();
                EmoteDAO emodao = new EmoteDAO();

                ArticleDTO artdto = artdao.findArticleById(articleId);
                if (artdto != null) {
                    url = SUCCESS;
                    request.setAttribute("ARTICLE", artdto);
                    request.setAttribute("LIKE", emodao.getLikesByArticleId(artdto.getId()));
                    request.setAttribute("DISLIKE", emodao.getDislikesByArticleId(artdto.getId()));
                    request.setAttribute("COMMENT", comdao.getAllCommentsByArticleId(artdto.getId()));
                    request.setAttribute("EMOTE", emodao.checkEmoteExist(articleId, accdto.getEmail()));
                } else {
                    request.setAttribute("NOTICE", "The Article is deleted.");
                }
            }
        } catch (Exception e) {
            LOGGER.error("ERROR at DetailController");
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
