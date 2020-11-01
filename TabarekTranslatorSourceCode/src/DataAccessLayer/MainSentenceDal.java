/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer;

import Entities.MainSentence; 
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
public class MainSentenceDal {
    Connection conn=null;
    
    public long add(MainSentence ms){ 
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("INSERT INTO MainSentences VALUES(NULL,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,ms.getSentence());
            preparedStatement.setInt(2,ms.getDegree());
            preparedStatement.setInt(3, ms.getDate());
            preparedStatement.executeUpdate(); 
            ResultSet rs=preparedStatement.getGeneratedKeys();
            if(rs.next()){
                return rs.getLong(1);
            }
        } catch (Exception ex) {
            
            System.err.println("addMainSentence HATAA "+ex.toString());
        }
        return -1;
    } 
    
    public ArrayList<MainSentence> get(){
        try {
            conn=Helper.getSQLConnection();
            Statement statement  = conn.createStatement(); 
            ResultSet rs= statement.executeQuery("SELECT * FROM MainSentences");
            if(rs!=null){
                ArrayList<MainSentence> mss=new ArrayList<>();            
                while(rs.next()){  
                    MainSentence ms=new MainSentence();
                    ms.setId(rs.getLong("Id"));
                    ms.setSentence(rs.getString("Sentence"));
                    ms.setDegree(rs.getInt("Degree")); 
                    ms.setDate(rs.getInt("Date")); 
                    mss.add(ms);
                }
                return mss; 
            }
            else
                return null;  
        } catch (Exception ex) {
            System.err.println("getMainSentence HATAA "+ex.toString());
        }
        return null;
    }  
    
    
    public MainSentence get(long id){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM MainSentences WHERE Id=? ");
            preparedStatement.setLong(1, id); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){ 
                MainSentence ms=new MainSentence();
                ms.setSentence(rs.getString("Sentence"));
                ms.setDegree(rs.getInt("Degree"));
                ms.setDate(rs.getInt("Date"));
                ms.setId(rs.getLong("Id"));
                return ms;
            }
           
        } catch (Exception ex) {
            System.err.println("getMainSentence HATAA "+ex.toString());
        }
        return null;
    }  
    
    public MainSentence getBySentence(String s){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM MainSentences WHERE Sentence=? ");
            preparedStatement.setString(1, s); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){ 
                MainSentence ms=new MainSentence();
                ms.setSentence(rs.getString("Sentence"));
                ms.setDegree(rs.getInt("Degree"));
                ms.setDate(rs.getInt("Date"));
                ms.setId(rs.getLong("Id"));
                return ms;
            }
           
        } catch (Exception ex) {
            System.err.println("getMainSentence HATAA "+ex.toString());
        }
        return null;
    }  
    
    public MainSentence getIdBySentence(String s){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM MainSentences WHERE LOWER(Sentence)=LOWER(?) ");
            preparedStatement.setString(1, s); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){ 
                MainSentence ms=new MainSentence();
                ms.setSentence(rs.getString("Sentence"));
                ms.setDegree(rs.getInt("Degree"));
                ms.setDate(rs.getInt("Date"));
                ms.setId(rs.getLong("Id"));
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
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM MainSentences WHERE LOWER(Sentence)=LOWER(?) ");
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
            PreparedStatement preparedStatement  = conn.prepareStatement("DELETE FROM MainSentences WHERE Id=? ");
            preparedStatement.setLong(1, id); 
            return  preparedStatement.executeUpdate();
           
        } catch (Exception ex) {
            System.err.println("deleteMainSentence HATAA "+ex.toString());
        } 
        return 0;
    }
    
    public int deleteBySentence(String s){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("DELETE FROM MainSentences WHERE LOWER(Sentence)=LOWER(?)");
            preparedStatement.setString(1, s); 
            return  preparedStatement.executeUpdate();
           
        } catch (Exception ex) {
            System.err.println("deleteBySentence HATAA "+ex.toString());
        } 
        return 0;
    }
    
    public void update(MainSentence ms){ 
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("UPDATE MainSentences SET Sentence=?,Degree=? WHERE Id=?");
            preparedStatement.setString(1,ms.getSentence());
            preparedStatement.setInt(2,ms.getDegree());
            preparedStatement.setLong(3, ms.getId());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.err.println("updateMainSentece HATAA "+ex.toString());
        } 
    } 
    
    public MainSentence getDetail(long id){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement=conn.prepareStatement(
                "SELECT MainSentences.Id, MainSentences.Sentence, MainSentences.Degree, MainSentences.Date, TargetSentences.Id as TargetSentenceId, TargetSentences.Sentence as TargetSentence, TargetSentences.Date as TargetSentenceDate\n" +
                "FROM MainSentences \n" +
                "INNER JOIN TargetSentences ON MainSentences.Id=TargetSentences.MainSentenceId \n" +
                "WHERE MainSentences.Id=?");
            preparedStatement.setLong(1,id);
            ResultSet rs=preparedStatement.executeQuery(); 
            if(rs.next()){
                MainSentence ms=new MainSentence();
                ms.setTargetSentence(new TargetSentence());     
                ms.setId(rs.getLong("Id"));
                ms.setSentence(rs.getString("Sentence"));
                ms.setDegree(rs.getInt("Degree"));
                ms.setDate(rs.getInt("Date"));
                ms.getTargetSentence().setSentence(rs.getString("TargetSentence"));
                ms.getTargetSentence().setId(rs.getLong("TargetSentenceId"));
                ms.getTargetSentence().setDate(rs.getInt("TargetSentenceDate")); 
                
                return ms;                
            } 
        } catch (Exception ex) {
            System.err.println("getDetail HATAA "+ex.toString());
        }
        return null;
    } 
    
    public MainSentence getDetailBySentence(String sentence){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement=conn.prepareStatement(
                "SELECT MainSentences.Id, MainSentences.Sentence, MainSentences.Degree, MainSentences.Date, TargetSentences.Id as TargetSentenceId, TargetSentences.Sentence as TargetSentence, TargetSentences.Date as TargetSentenceDate\n" +
                "FROM MainSentences \n" +
                "INNER JOIN TargetSentences ON MainSentences.Id=TargetSentences.MainSentenceId \n" +
                "WHERE LOWER(MainSentences.Sentence)=LOWER(?)");
            preparedStatement.setString(1,sentence);
            ResultSet rs=preparedStatement.executeQuery(); 
            if(rs.next()){
                MainSentence ms=new MainSentence();
                ms.setTargetSentence(new TargetSentence());     
                ms.setId(rs.getLong("Id"));
                ms.setSentence(rs.getString("Sentence"));
                ms.setDegree(rs.getInt("Degree"));
                ms.setDate(rs.getInt("Date"));
                ms.getTargetSentence().setSentence(rs.getString("TargetSentence"));
                ms.getTargetSentence().setId(rs.getLong("TargetSentenceId"));
                ms.getTargetSentence().setDate(rs.getInt("TargetSentenceDate")); 
                
                return ms;                
            } 
        } catch (Exception ex) {
            System.err.println("getDetail HATAA "+ex.toString());
        }
        return null;
    }
    
    public ArrayList<MainSentence> getDetailIncludingInSentence(String string){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement=conn.prepareStatement(
                "SELECT MainSentences.Id, MainSentences.Sentence, MainSentences.Degree, MainSentences.Date, TargetSentences.Id as TargetSentenceId, TargetSentences.Sentence as TargetSentence, TargetSentences.Date as TargetSentenceDate\n" +
                "FROM MainSentences \n" +
                "INNER JOIN TargetSentences ON MainSentences.Id=TargetSentences.MainSentenceId \n" +
                "WHERE instr(LOWER(MainSentences.Sentence), LOWER(?)) > 0");
            preparedStatement.setString(1,string);
            ResultSet rs=preparedStatement.executeQuery(); 
            
            ArrayList<MainSentence> mss=new ArrayList<>();
            while(rs.next()){
                MainSentence ms=new MainSentence();
                ms.setTargetSentence(new TargetSentence());     
                ms.setId(rs.getLong("Id"));
                ms.setSentence(rs.getString("Sentence"));
                ms.setDegree(rs.getInt("Degree"));
                ms.setDate(rs.getInt("Date"));
                ms.getTargetSentence().setSentence(rs.getString("TargetSentence"));
                ms.getTargetSentence().setId(rs.getLong("TargetSentenceId"));
                ms.getTargetSentence().setDate(rs.getInt("TargetSentenceDate"));  
                mss.add(ms);
            } 
            return mss;
        } catch (Exception ex) {
            System.err.println("getDetail HATAA "+ex.toString());
        }
        return null;
    }
    
    public ArrayList<MainSentence> getDetail(){
        try {
            conn=Helper.getSQLConnection();
            Statement statement  = conn.createStatement(); 
            ResultSet rs=statement.executeQuery(
                "SELECT MainSentences.Id, MainSentences.Sentence, MainSentences.Degree, MainSentences.Date, TargetSentences.Id as TargetSentenceId, TargetSentences.Sentence as TargetSentence, TargetSentences.Date as TargetSentenceDate\n" +
                "FROM MainSentences \n" +
                "INNER JOIN TargetSentences ON MainSentences.Id=TargetSentences.MainSentenceId");
            ArrayList<MainSentence> mss=new ArrayList<>();
            while(rs.next()){
                MainSentence ms=new MainSentence();
                ms.setTargetSentence(new TargetSentence());     
                ms.setId(rs.getLong("Id"));
                ms.setSentence(rs.getString("Sentence"));
                ms.setDegree(rs.getInt("Degree"));
                ms.setDate(rs.getInt("Date"));
                ms.getTargetSentence().setSentence(rs.getString("TargetSentence"));
                ms.getTargetSentence().setId(rs.getLong("TargetSentenceId"));
                ms.getTargetSentence().setDate(rs.getInt("TargetSentenceDate")); 
                mss.add(ms);
            }
            return mss;
        } catch (Exception ex) {
            System.err.println("getDetailById HATAA "+ex.toString());
        }
        return null;
    }
}
