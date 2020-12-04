/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.daos;

import longtt.dtos.CommentDTO;
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
public class CommentDAO implements Serializable {
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
    
    public boolean postComment(CommentDTO comment) throws Exception {
        boolean check = false;
        try {
            String query = "Insert into tblComment(ID, ArticleID, Email, Content, Date, Status) values (?,?,?,?,GETDATE(),1)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, comment.getId());
            preStm.setString(2, comment.getArticleId());
            preStm.setString(3, comment.getEmail());
            preStm.setString(4, comment.getContent());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public List<CommentDTO> getAllCommentsByArticleId(String articleId) throws Exception {
        List<CommentDTO> list = new ArrayList<>();
        CommentDTO dto = null;
        String id, email, content, date, name;
        int status;
        try {
            String query = "Select A.ID, A.Email, B.Name, A.Content, convert(varchar,A.[Date],0) as Date, A.Status " +
                           "From tblComment A, tblAccount B " +
                           "Where A.ArticleID = ? AND A.Status = 1 AND A.Email = B.Email " +
                           "Order by A.[Date] ASC";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, articleId);
            rs = preStm.executeQuery();
            while (rs.next()) {
                id = rs.getString("ID");
                email = rs.getString("Email");
                content = rs.getString("Content");
                date = rs.getString("Date");
                status = rs.getInt("Status");
                name = rs.getString("Name");
                dto = new CommentDTO(id, articleId, email, content, date, status);
                dto.setName(name);
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    }
    
    public String getCommentID() throws Exception {
        String count = null;
        try {
            String query = "Select COUNT(ID)+1 as Count From tblComment";
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
    
    public boolean deactiveComment(String id) throws Exception {
        boolean check = false;
        try {
            String query = "Update tblComment set Status = -1 where ID = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, id);
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
}
