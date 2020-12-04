/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.controllers;

import longtt.daos.EmoteDAO;
import longtt.daos.NotificationDAO;
import longtt.dtos.EmoteDTO;
import longtt.dtos.NotificationDTO;
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
public class EmoteController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(EmoteController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String emote = request.getParameter("emote");
            String articleId = request.getParameter("txtArticleId");
            String email = request.getParameter("txtEmail");
            EmoteDAO emodao = new EmoteDAO();
            EmoteDTO check = emodao.checkEmoteExist(articleId, email);
            NotificationDAO notdao = new NotificationDAO();

            if (emote.equals("like")) {
                if (check == null) {    //not emote yet
                    EmoteDTO emodto = new EmoteDTO(emodao.getEmoteID(), articleId, email, true, false);
                    if (emodao.insertEmote(emodto)) {
                        NotificationDTO notdto = new NotificationDTO(notdao.getNotificationID(), articleId, email, "Like");
                        notdao.insertNotification(notdto);
                    }
                } else if (check.isDislike() == true) {     //dislike -> like
                    emodao.deactiveEmote(check.getId());
                    EmoteDTO emodto = new EmoteDTO(emodao.getEmoteID(), articleId, email, true, false);
                    if (emodao.insertEmote(emodto)) {
                        NotificationDTO notdto = new NotificationDTO(notdao.getNotificationID(), articleId, email, "Like");
                        notdao.insertNotification(notdto);
                    }
                } else {    //like -> unlike
                    if (emodao.deactiveEmote(check.getId())) {
                        NotificationDTO notdto = new NotificationDTO(notdao.getNotificationID(), articleId, email, "Unlike");
                        notdao.insertNotification(notdto);
                    }
                }
            } else if (emote.equals("dislike")) {
                if (check == null) {    //not emote yet
                    EmoteDTO emodto = new EmoteDTO(emodao.getEmoteID(), articleId, email, false, true);
                    if (emodao.insertEmote(emodto)) {
                        NotificationDTO notdto = new NotificationDTO(notdao.getNotificationID(), articleId, email, "Dislike");
                        notdao.insertNotification(notdto);
                    }
                } else if (check.isLike() == true) {    //like -> dislike
                    emodao.deactiveEmote(check.getId());
                    EmoteDTO emodto = new EmoteDTO(emodao.getEmoteID(), articleId, email, false, true);
                    if (emodao.insertEmote(emodto)) {
                        NotificationDTO notdto = new NotificationDTO(notdao.getNotificationID(), articleId, email, "Dislike");
                        notdao.insertNotification(notdto);
                    }
                } else {    //dislike -> undislike
                    if (emodao.deactiveEmote(check.getId())) {
                        NotificationDTO notdto = new NotificationDTO(notdao.getNotificationID(), articleId, email, "Undislike");
                        notdao.insertNotification(notdto);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("ERROR at EmoteController");
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
