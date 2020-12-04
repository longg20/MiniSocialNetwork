/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.controllers;

import longtt.daos.AccountDAO;
import longtt.dtos.AccountDTO;
import longtt.dtos.ErrorObj;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import longtt.utilities.sha256;

/**
 *
 * @author Long
 */
public class LoginController extends HttpServlet {

    private static final String SUCCESS = "SearchController";
    private static final String INVALID = "login.jsp";
    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = INVALID;
        try {
            String email = request.getParameter("txtEmail");
            String password = request.getParameter("txtPassword");

            boolean valid = true;
            ErrorObj errorObj = new ErrorObj();
            if (email.length() == 0) {
                valid = false;
                errorObj.setEmailError("Email can't be blank");
            }
            if (password.length() == 0) {
                valid = false;
                errorObj.setPasswordError("Password can't be blank");
            }

            if (valid) {
                password = sha256.encrypt(password);
                AccountDAO dao = new AccountDAO();
                AccountDTO dto = dao.checkLogin(email, password);
                if (dto != null) {
                    url = SUCCESS;
                    HttpSession session = request.getSession();
                    session.setAttribute("USER", dto);
                } else {
                    errorObj.setLoginError("Invalid email or password!");
                    request.setAttribute("INVALID", errorObj);
                }
            } else {
                request.setAttribute("INVALID", errorObj);
            }

        } catch (Exception e) {
            LOGGER.error("Error at LoginController");
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
