/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.controllers;

import longtt.daos.ArticleDAO;
import longtt.dtos.ArticleDTO;
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
public class PostController extends HttpServlet {

    private static final String INVALID = "post.jsp";
    private static final String SUCCESS = "SearchController";
    private static final Logger LOGGER = Logger.getLogger(PostController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = INVALID;
        try {
            String title = request.getParameter("txtTitle");
            String description = request.getParameter("txtDescription");
            String image = request.getParameter("txtImage");
            String email = request.getParameter("txtEmail");

            boolean valid = true;
            ErrorObj errorObj = new ErrorObj();

            if (title.length() == 0) {
                valid = false;
                errorObj.setTitleError("Title can't be blank");
            }
            if (title.length() > 500) {
                valid = false;
                errorObj.setTitleError("Title exceed maximum length");
            }
            if (description.length() == 0) {
                valid = false;
                errorObj.setDescriptionError("Description can't be blank");
            }
            if (description.length() > 500) {
                valid = false;
                errorObj.setDescriptionError("Description exceed maximum length");
            }
            if (image.length() == 0) {
                valid = false;
                errorObj.setImageError("Image can't be blank");
            }
            if (image.length() > 500) {
                valid = false;
                errorObj.setImageError("Image exceed maximum length");
            }

            if (valid) {
                ArticleDAO dao = new ArticleDAO();
                String id = dao.getArticleID();
                ArticleDTO dto = new ArticleDTO(id, title, description, image, email);
                if (dao.postArticle(dto)) {
                    url = SUCCESS;
                    request.setAttribute("NOTICE", "Article posted successfully.");
                }
            } else {
                url = INVALID;
                request.setAttribute("INVALID", errorObj);
            }
        } catch (Exception e) {
            LOGGER.error("ERROR at PostController");
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
