/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.daos;

import longtt.dtos.ArticleDTO;
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
public class ArticleDAO implements Serializable {
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
    
    public boolean postArticle(ArticleDTO dto) throws Exception {
        boolean check = false;
        try {
            String query = "Insert into tblArticle(ID, Title, Description, Image, Date, Email, Status) values (?,?,?,?,GETDATE(),?,1)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, dto.getId());
            preStm.setString(2, dto.getTitle());
            preStm.setString(3, dto.getDescription());
            preStm.setString(4, dto.getImage());
            preStm.setString(5, dto.getEmail());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public List<ArticleDTO> findArticlesByLikeName(String search, int page) throws Exception {
        List<ArticleDTO> list = new ArrayList<>();
        ArticleDTO dto = null;
        String id, title, description, image, date, email, author;
        int status, like, dislike, comment;
        try {
            String query = "Select * From ( "
                         + "Select T1.rownumber, T1.ID, T1.Title, T1.Description, T1.Image, T1.[Date], T1.Email, T1.Name, "
                         + "T1.Status, T1.[Like], T1.[Dislike], T2.Comment "
                         + "From ( "
                         + "Select ROW_NUMBER() Over (Order By A.[Date] DESC) as rownumber, A.ID, A.Title, A.Description, "
                         + "A.Image, convert(varchar,A.[Date],0) as [Date], A.Email, C.Name, A.Status, "
                         + "Count(NULLIF(B.[Like], 0)) as [Like], "
                         + "Count(NULLIF(B.[Dislike], 0)) as [Dislike] "
                         + "From tblArticle A "
                         + "LEFT JOIN tblEmote B ON A.ID = B.ArticleID AND B.Status = 1 "
                         + "JOIN tblAccount C ON A.Email = C.Email "
                         + "Where A.Status = 1 AND (A.ID Like ? OR A.Title Like ? OR A.Description Like ? OR A.Email Like ?) "
                         + "Group By A.ID, A.Title, A.Description, A.Image, A.[Date], A.Email, C.Name, A.Status "
                         + ") T1 JOIN ( "
                         + "Select A.ID, Count(B.ID) as Comment "
                         + "From tblArticle A "
                         + "LEFT JOIN tblComment B ON A.ID = B.ArticleID AND B.Status = 1 "
                         + "JOIN tblAccount C ON A.Email = C.Email "
                         + "Where A.Status = 1 AND (A.ID Like ? OR A.Title Like ? OR A.Description Like ? OR A.Email Like ?) "
                         + "Group By A.ID, A.Title, A.Description, A.Image, A.[Date], A.Email, C.Name, A.Status "
                         + ") T2 ON T1.ID = T2.ID "
                         + ") as tblArticlePaging "
                         + "Where rownumber > ? AND rownumber <= ? "
                         + "Order By rownumber ASC";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, "%" + search + "%");
            preStm.setString(2, "%" + search + "%");
            preStm.setString(3, "%" + search + "%");
            preStm.setString(4, "%" + search + "%");
            preStm.setString(5, "%" + search + "%");
            preStm.setString(6, "%" + search + "%");
            preStm.setString(7, "%" + search + "%");
            preStm.setString(8, "%" + search + "%");
            preStm.setInt(9, page * 20 - 20);
            preStm.setInt(10, page * 20);
            rs = preStm.executeQuery();
            while (rs.next()) {
                id = rs.getString("ID");
                title = rs.getString("Title");
                description = rs.getString("Description");
                image = rs.getString("Image");
                date = rs.getString("Date");
                email = rs.getString("Email");
                author = rs.getString("Name");
                status = rs.getInt("Status");
                like = rs.getInt("Like");
                dislike = rs.getInt("Dislike");
                comment = rs.getInt("Comment");
                dto = new ArticleDTO(id, title, description, image, date, email, status);
                dto.setAuthor(author);
                dto.setLike(like);
                dto.setDislike(dislike);
                dto.setComment(comment);
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    }
    
    public int getArticleCountByLikeName(String search) throws Exception {
        int count = 0;
        try {
            String query = "Select COUNT(ID) as Count from tblArticle "
                         + "Where (ID Like ? OR Title Like ? OR Description Like ? OR Email Like ?) AND Status = 1";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, "%" + search + "%");
            preStm.setString(2, "%" + search + "%");
            preStm.setString(3, "%" + search + "%");
            preStm.setString(4, "%" + search + "%");
            rs = preStm.executeQuery();
            if (rs.next())
                count = rs.getInt("Count");
        } finally {
            closeConnection();
        }
        return count;
    }

    public ArticleDTO findArticleById(String id) throws Exception {
        ArticleDTO dto = null;
        try {
            String query = "Select A.ID, A.Title, A.Description, A.Image, Convert(varchar, A.[Date], 0) as Date, A.Email, B.Name, A.Status "
                         + "From tblArticle A, tblAccount B "
                         + "Where A.ID = ? AND A.Email = B.Email AND A.Status = 1";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, id);
            rs = preStm.executeQuery();
            if (rs.next()) {
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String image = rs.getString("Image");
                String date = rs.getString("Date");
                String email = rs.getString("Email");
                String author = rs.getString("Name");
                int status = rs.getInt("Status");
                dto = new ArticleDTO(id, title, description, image, date, email, status);
                dto.setAuthor(author);
            }
        } finally {
            closeConnection();
        }
        return dto;
    }
    
    public String getArticleID() throws Exception {
        String count = null;
        try {
            String query = "Select COUNT(ID)+1 as Count From tblArticle";
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
       
    public boolean deactiveArticle(String articleId) throws Exception {
        boolean check = false;
        try {
            String query = "Update tblArticle set Status = -1 where ID = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, articleId);
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
}
