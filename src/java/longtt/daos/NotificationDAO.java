/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.daos;

import longtt.dtos.NotificationDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author Long
 */
public class NotificationDAO implements Serializable {
    private Connection conn = null;
    private PreparedStatement preStm = null;
    private ResultSet rs = null;
    
    private void closeConnection() throws Exception {
        if (rs != null)
            rs.close();
        if (preStm != null)
            preStm.close();
        if (conn != null)
            conn.close();
    }
    
    public static Connection getMyConnection() throws Exception {
        Connection conn = null;
        Context context = new InitialContext();
        Context end = (Context) context.lookup("java:comp/env");
        DataSource ds = (DataSource) end.lookup("DBCon");
        conn = ds.getConnection();
        return conn;
    }
    
    public List<NotificationDTO> getAccountNotifications(String loginEmail) throws Exception {
        List<NotificationDTO> list = new ArrayList<>();
        NotificationDTO dto = null;
        String id, articleId, email, date, type, title, name;
        int status;
        try {
            String query = "Select A.ID, A.ArticleID, C.Title, A.Email, B.[Name], convert(varchar,A.[Date],0) as [Date], A.[Type], A.[Status] " +
                           "From tblNotification A, tblAccount B, tblArticle C " +
                           "Where A.Email = B.Email AND A.ArticleID = C.ID AND C.Email = ? AND A.Email != ? " +
                           "AND A.Status = 1 " + 
                           "ORDER by [Date] DESC";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, loginEmail);
            preStm.setString(2, loginEmail);
            rs = preStm.executeQuery();
            while (rs.next()) {
                id = rs.getString("ID");
                articleId = rs.getString("ArticleID");
                title = rs.getString("Title");
                email = rs.getString("Email");
                name = rs.getString("Name");
                date = rs.getString("Date");
                type = rs.getString("Type");
                status = rs.getInt("Status");
                dto = new NotificationDTO(id, articleId, email, date, type, status);
                dto.setName(name);
                dto.setTitle(title);
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    } 
    
    public boolean insertNotification(NotificationDTO dto) throws Exception {
        boolean check = false;
        try {
            String query = "Insert into tblNotification(ID, ArticleID, Email, [Date], Type, Status) values (?,?,?,GETDATE(),?,1)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, dto.getId());
            preStm.setString(2, dto.getArticleId());
            preStm.setString(3, dto.getEmail());
            preStm.setString(4, dto.getType());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public boolean deactiveNotification(String id) throws Exception {
        boolean check = false;
        try {
            String query = "Update tblNotification set Status = -1 Where ID = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, id);
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public String getNotificationID() throws Exception {
        String count = null;
        try {
            String query = "Select COUNT(ID)+1 as [Count] from tblNotification";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            rs = preStm.executeQuery();
            if (rs.next())
                count = rs.getString("Count");
        } finally {
            closeConnection();
        }
        return count;
    }
}
