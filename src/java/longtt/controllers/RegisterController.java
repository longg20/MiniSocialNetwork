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
import org.apache.log4j.Logger;
import longtt.utilities.sha256;

/**
 *
 * @author Long
 */
public class RegisterController extends HttpServlet {

    private static final String SUCCESS = "SearchController";
    private static final String INVALID = "register.jsp";
    private static final Logger LOGGER = Logger.getLogger(RegisterController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = INVALID;
        try {
            String name = request.getParameter("txtName");
            String email = request.getParameter("txtEmail");
            String password = request.getParameter("txtPassword");
            String confirm = request.getParameter("txtConfirm");

            boolean valid = true;
            AccountDAO dao = new AccountDAO();
            ErrorObj errorObj = new ErrorObj();
            if (name.length() == 0) {
                valid = false;
                errorObj.setNameError("Name can't be blank");
            }
            if (name.length() > 500) {
                valid = false;
                errorObj.setNameError("Name exceed maximum length");
            }
            if (email.length() == 0) {
                valid = false;
                errorObj.setEmailError("Email can't be blank");
            }
            if (email.length() > 500) {
                valid = false;
                errorObj.setEmailError("Email exceed maximum length");
            }
            if (dao.checkEmailExist(email) == true) {
                valid = false;
                errorObj.setEmailError("This email is existed. Try another");
            }
            if (password.length() == 0) {
                valid = false;
                errorObj.setPasswordError("Password can't be blank");
            }
            if (password.length() > 500) {
                valid = false;
                errorObj.setPasswordError("Password exceed maximum length");
            }
            if (confirm.length() == 0) {
                valid = false;
                errorObj.setConfirmError("Confirm can't be blank");
            }
            if (!confirm.equals(password)) {
                valid = false;
                errorObj.setConfirmError("Confirm does not match");
            }

            if (valid) {
                password = sha256.encrypt(password);
                AccountDTO dto = new AccountDTO(email, name, "Member", 0);
                dto.setPassword(password);
                if (dao.createAccount(dto)) {
                    url = SUCCESS;
                    request.setAttribute("NOTICE", "Account created successfully");
                }
            } else {
                url = INVALID;
                request.setAttribute("INVALID", errorObj);
            }
        } catch (Exception e) {
            LOGGER.error("ERROR at RegisterController");
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
