/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.controllers;

import longtt.daos.ArticleDAO;
import longtt.daos.NotificationDAO;
import longtt.dtos.AccountDTO;
import longtt.dtos.ArticleDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
public class SearchController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SearchController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String search = request.getParameter("txtSearch");
            String pageStr = request.getParameter("txtPage");
            String move = request.getParameter("movePage");

            int page;

            if (search == null) {
                search = "";
            }

            if (pageStr == null) {
                page = 1;
            } else {
                page = Integer.parseInt(pageStr);
            }

            ArticleDAO artdao = new ArticleDAO();
            int articleCount = artdao.getArticleCountByLikeName(search);
            int pageCount = (int) Math.ceil(articleCount / (double) 20); //round up, 21 -> page 2
            if (move == null); else if (move.equals("next") && page < pageCount) {
                page = page + 1;
            } else if (move.equals("prev") && page > 1) {
                page = page - 1;
            }

            List<ArticleDTO> artList = artdao.findArticlesByLikeName(search, page);
            request.setAttribute("INFO", artList);
            request.setAttribute("PAGE_CURRENT", page);
            request.setAttribute("PAGE_MAX", pageCount);
            request.setAttribute("ARTICLE_CURRENT", artList.size());
            request.setAttribute("ARTICLE_MAX", articleCount);

            HttpSession session = request.getSession();
            AccountDTO accdto = (AccountDTO) session.getAttribute("USER");
            NotificationDAO notdao = new NotificationDAO();
            request.setAttribute("NOTIFICATION", notdao.getAccountNotifications(accdto.getEmail()));
        } catch (Exception e) {
            LOGGER.error("ERROR at SearchController");
        } finally {
            request.getRequestDispatcher("home.jsp").forward(request, response);
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
