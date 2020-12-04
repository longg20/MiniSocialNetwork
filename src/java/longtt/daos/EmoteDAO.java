/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.daos;

import longtt.dtos.EmoteDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author Long
 */
public class EmoteDAO implements Serializable {
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
    
    public EmoteDTO checkEmoteExist(String articleId, String email) throws Exception {
        EmoteDTO dto = null;
        try {
            String query = "Select ID, convert(varchar,[Date],0) as Date, [Like], [Dislike] "
                         + "From tblEmote where ArticleID = ? AND Email = ? AND Status = 1";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, articleId);
            preStm.setString(2, email);
            rs = preStm.executeQuery();
            if (rs.next()) {
                String id = rs.getString("ID");
                String date = rs.getString("Date");
                boolean like = rs.getBoolean("Like");
                boolean dislike = rs.getBoolean("Dislike");
                dto = new EmoteDTO(id, articleId, email, like, dislike, date, 1);
            }
        } finally {
            closeConnection();
        }
        return dto;
    }
    
    public boolean insertEmote(EmoteDTO dto) throws Exception {
        boolean check = false;
        try {
            String query = "Insert into tblEmote(ID, ArticleID, Email, [Like], [Dislike], [Date], Status) values (?,?,?,?,?,GETDATE(),1)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, dto.getId());
            preStm.setString(2, dto.getArticleId());
            preStm.setString(3, dto.getEmail());
            preStm.setBoolean(4, dto.isLike());
            preStm.setBoolean(5, dto.isDislike());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public boolean dislikeArticle(EmoteDTO dto) throws Exception {
        boolean check = false;
        try {
            String query = "Insert into tblEmote(ID, ArticleID, Email, [Like], [Dislike], [Date], Status) values (?,?,?,?,?,GETDATE(),?)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, dto.getId());
            preStm.setString(2, dto.getArticleId());
            preStm.setString(3, dto.getEmail());
            preStm.setString(4, "False");
            preStm.setString(5, "True");
            preStm.setInt(6, dto.getStatus());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public int getLikesByArticleId(String id) throws Exception {
        int like = 0;
        try {
            String query = "Select COUNT([Like]) as [Like] from tblEmote where [Like] = 1 AND [Dislike] = 0 AND ArticleID = ? AND Status = 1";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, id);
            rs = preStm.executeQuery();
            if (rs.next())
                like = rs.getInt("Like");
        } finally {
            closeConnection();
        }
        return like;
    }
    
    public int getDislikesByArticleId(String id) throws Exception {
        int dislike = 0;
        try {
            String query = "Select COUNT([Dislike]) as [Dislike] from tblEmote where [Like] = 0 AND [Dislike] = 1 AND ArticleID = ? AND Status = 1";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, id);
            rs = preStm.executeQuery();
            if (rs.next())
                dislike = rs.getInt("Dislike");
        } finally {
            closeConnection();
        }
        return dislike;
    }
    
    public boolean deactiveEmote(String id) throws Exception {
        boolean check = false;
        try {
            String query = "Update tblEmote set Status = -1 Where ID = ? AND Status = 1";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, id);
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public String getEmoteID() throws Exception {
        String count = null;
        try {
            String query = "Select COUNT(ID)+1 as Count From tblEmote";
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
