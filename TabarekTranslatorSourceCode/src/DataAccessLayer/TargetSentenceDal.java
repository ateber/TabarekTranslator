/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer; 
 
import Entities.TargetSentence;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import tabarektranslator.Helper;

/**
 *
 * @author ThekME
 */
public class TargetSentenceDal {
      
    Connection conn=null;
    
    public long add(TargetSentence ts){ 
        try { 
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("INSERT INTO TargetSentences VALUES(NULL,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, ts.getMainSentenceId());
            preparedStatement.setString(2,ts.getSentence());
            preparedStatement.setInt(3, ts.getDate());
            preparedStatement.executeUpdate();
            ResultSet rs=preparedStatement.getGeneratedKeys();
            if(rs.next()){
                return rs.getLong(1);
            }  
        } catch (Exception ex) {
            System.err.println("addTargetSentence HATAA "+ex.toString());
        } 
        return -1;
    } 
    
    public ArrayList<TargetSentence> get(){
        try {
            conn=Helper.getSQLConnection();
            Statement statement  = conn.createStatement(); 
            ResultSet rs= statement.executeQuery("SELECT * FROM TargetSentences");
            if(rs!=null){
                ArrayList<TargetSentence> tss=new ArrayList<>();            
                while(rs.next()){   
                    TargetSentence ts=new TargetSentence();
                    ts.setId(rs.getLong("Id"));
                    ts.setMainSentenceId(rs.getLong("MainSentenceId"));
                    ts.setSentence(rs.getString("Sentence"));
                    ts.setDate(rs.getInt("Date")); 
                    tss.add(ts);
                }
                return tss; 
            }
            else
                return null;  
        } catch (Exception ex) {
            System.err.println("getTargetSentence HATAA "+ex.toString());
        }
        return null;
    }  
    
    
    public TargetSentence get(long id){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM TargetSentences WHERE Id=? ");
            preparedStatement.setLong(1, id); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){ 
                TargetSentence ts=new TargetSentence();
                ts.setId(rs.getLong("Id"));
                ts.setMainSentenceId(rs.getLong("MainSentenceId"));
                ts.setSentence(rs.getString("Sentence"));
                ts.setDate(rs.getInt("Date")); 
                return ts;
            }
           
        } catch (Exception ex) {
            System.err.println("getTargetSentence HATAA "+ex.toString());
        }
        return null;
    }  
    
    public TargetSentence getIdBySentence(String s){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM TargetSentences WHERE LOWER(Sentence)=LOWER(?) ");
            preparedStatement.setString(1, s); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){ 
                TargetSentence ms=new TargetSentence();
                ms.setId(rs.getLong("Id"));
                ms.setMainSentenceId(rs.getLong("MainSentenceId"));
                ms.setSentence(rs.getString("Sentence"));
                ms.setDate(rs.getInt("Date"));
               
                return ms;
            }
           
        } catch (Exception ex) {
            System.err.println("getIdBySentence HATAA "+ex.toString());
        }
        return null;
    }
    
    public boolean isSentenceExist(String s){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM TargetSentences WHERE LOWER(Sentence)=LOWER(?) ");
            preparedStatement.setString(1, s); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){  
                return true;
            } 
        } catch (Exception ex) {
            System.err.println("isSentenceExist HATAA "+ex.toString());
        }
        return false;
    }
    
    
    public int delete(long id){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("DELETE FROM TargetSentences WHERE Id=? ");
            preparedStatement.setLong(1, id); 
            return  preparedStatement.executeUpdate();
           
        } catch (Exception ex) {
            System.err.println("deleteTargetSentence HATAA "+ex.toString());
        } 
        return 0;
    }
    
    public int deleteBySentence(String s){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("DELETE FROM TargetSentences WHERE LOWER(Sentence)=LOWER(?)");
            preparedStatement.setString(1, s); 
            return  preparedStatement.executeUpdate();
           
        } catch (Exception ex) {
            System.err.println("deleteBySentence HATAA "+ex.toString());
        } 
        return 0;
    }
    
    public void update(TargetSentence ts){ 
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("UPDATE TargetSentences SET Sentence=? WHERE Id=?");
            preparedStatement.setString(1,ts.getSentence());
            preparedStatement.setLong(2, ts.getId());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.err.println("updateTargetSentence HATAA "+ex.toString());
        } 
    } 
}
