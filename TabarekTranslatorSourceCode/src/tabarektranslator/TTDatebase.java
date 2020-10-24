/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabarektranslator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 



public class TTDatebase {
    private static Connection conn = null;
    final static private String DB_URL="jdbc:sqlite:C:\\Users\\ThekME\\Documents\\NetBeansProjects\\TabarekTranslator\\src\\Database\\";
    final static private String DB_FILENAME="TabarekTranslatorDB.db";
    public static boolean connect(){ 
        try {
            conn = DriverManager.getConnection(DB_URL+DB_FILENAME);
            System.out.println("Connection to SQLite has been established."); 
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage()); 
        } 
        return false;
    }
    public static boolean disconnect(){
        try { 
            if(conn!=null){
                conn.close();
                System.out.println("Connection to SQLite has been stopped.");
            }
                 
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());  
        }
        return false;
    }
    
    public static ResultSet getQuery(String query){ 
        try {
            Statement pstmt=conn.createStatement();
            return pstmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } 
    }
    
    public static int setQuery(String query){ 
        try {
           PreparedStatement pstmt = conn.prepareStatement(query); 
           return pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage()); 
            return 0;
        }  
    }
    
}
