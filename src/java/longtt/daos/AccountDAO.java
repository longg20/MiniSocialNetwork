/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.daos;

import longtt.dtos.AccountDTO;
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
public class AccountDAO implements Serializable {
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
    
    public AccountDTO checkLogin(String email, String password) throws Exception {
        AccountDTO dto = null;
        try {
            String query = "Select Name, Role, Status from tblAccount where Email = ? AND Password = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, email);
            preStm.setString(2, password);
            rs = preStm.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Name");
                String role = rs.getString("Role");
                int status = rs.getInt("Status");
                dto = new AccountDTO(email, name, role, status);
            }
        } finally {
            closeConnection();
        }
        return dto;
    }
    
    public boolean createAccount(AccountDTO dto) throws Exception {
        boolean check = false;
        try {
            String query = "Insert into tblAccount(Email, Name, Password, Role, Status) values (?,?,?,?,?)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, dto.getEmail());
            preStm.setString(2, dto.getName());
            preStm.setString(3, dto.getPassword());
            preStm.setString(4, dto.getRole());
            preStm.setInt(5, 0); //0 = new, 1 = active, -1 = deactive
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public String findNameByEmail(String email) throws Exception {
        String name = null;
        try {
            String query = "Select Name From tblAccount Where Email = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, email);
            rs = preStm.executeQuery();
            if (rs.next())
                name = rs.getString("Name");
        } finally {
            closeConnection();
        }
        return name;
    }
    
    public boolean checkEmailExist(String email) throws Exception {
        boolean exist = false;
        try {
            String query = "Select Email from tblAccount where Email = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, email);
            rs = preStm.executeQuery();
            if (rs.next())
                exist = true;
        } finally {
            closeConnection();
        }
        return exist;
    }
}
