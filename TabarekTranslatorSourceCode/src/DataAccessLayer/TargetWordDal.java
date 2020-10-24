/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer;
 
import Entities.TargetWord;
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
public class TargetWordDal {
    Connection conn=null;
    
    public long add(TargetWord tw){ 
        try { 
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("INSERT INTO TargetWords VALUES(NULL,?,?,?,?,?,?,?,?,?);",Statement.RETURN_GENERATED_KEYS); 
            preparedStatement.setLong(1,tw.getMainWordId());
            preparedStatement.setString(2, tw.getWord());
            preparedStatement.setString(3, tw.getVerb());
            preparedStatement.setString(4, tw.getNoun());
            preparedStatement.setString(5, tw.getAdjective());
            preparedStatement.setString(6, tw.getAdverb());
            preparedStatement.setString(7, tw.getPreposition());
            preparedStatement.setString(8, tw.getConjunction());
            preparedStatement.setInt(9, tw.getDate()); 
            preparedStatement.executeUpdate(); 
            ResultSet rs=preparedStatement.getGeneratedKeys();
            
            if(rs.next()){
                return rs.getLong(1);
            }
        } catch (Exception ex) {
            System.err.println("addTargetWord HATAA "+ex.toString());
        } 
        return -1;
    } 
    
    public ArrayList<TargetWord> get(){
        try {
            conn=Helper.getSQLConnection();
            Statement statement  = conn.createStatement(); 
            ResultSet rs= statement.executeQuery("SELECT * FROM TargetWords");
            if(rs!=null){
                ArrayList<TargetWord> tss=new ArrayList<>();            
                while(rs.next()){  
                    TargetWord ts=new TargetWord();
                    ts.setId(rs.getLong("Id"));
                    ts.setMainWordId(rs.getLong("MainWordId"));
                    ts.setWord(rs.getString("Word"));
                    ts.setVerb(rs.getString("Verb"));
                    ts.setNoun(rs.getString("Noun"));
                    ts.setAdjective(rs.getString("Adjective"));
                    ts.setAdverb(rs.getString("Adverb"));
                    ts.setPreposition(rs.getString("Preposition"));
                    ts.setConjunction(rs.getString("Conjuction"));
                    ts.setDate(rs.getInt("Date")); 
                    tss.add(ts);
                }
                return tss; 
            }
            else
                return null;  
        } catch (Exception ex) {
            System.err.println("getTargetWord HATAA "+ex.toString());
        }
        return null;
    }  
    
    
    public TargetWord get(long id){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM TargetWords WHERE Id=? ");
            preparedStatement.setLong(1, id); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){ 
                TargetWord ts=new TargetWord();
                ts.setId(rs.getLong("Id"));
                ts.setMainWordId(rs.getLong("MainWordId"));
                ts.setWord(rs.getString("Word"));
                ts.setVerb(rs.getString("Verb"));
                ts.setNoun(rs.getString("Noun"));
                ts.setAdjective(rs.getString("Adjective"));
                ts.setAdverb(rs.getString("Adverb"));
                ts.setPreposition(rs.getString("Preposition"));
                ts.setConjunction(rs.getString("Conjuction"));
                ts.setDate(rs.getInt("Date")); 
                return ts;
            }
           
        } catch (Exception ex) {
            System.err.println("getTargetWord HATAA "+ex.toString());
        }
        return null;
    }  
    
    public TargetWord getIdByWord(String w){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM TargetWords WHERE LOWER(Word)=LOWER(?) ");
            preparedStatement.setString(1, w); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){ 
                TargetWord ts=new TargetWord();
                ts.setId(rs.getLong("Id"));
                ts.setMainWordId(rs.getLong("MainWordId"));
                ts.setWord(rs.getString("Word"));
                ts.setVerb(rs.getString("Verb"));
                ts.setNoun(rs.getString("Noun"));
                ts.setAdjective(rs.getString("Adjective"));
                ts.setAdverb(rs.getString("Adverb"));
                ts.setPreposition(rs.getString("Preposition"));
                ts.setConjunction(rs.getString("Conjuction"));
                ts.setDate(rs.getInt("Date")); 
                return ts;
            }
           
        } catch (Exception ex) {
            System.err.println("getIdByWord HATAA "+ex.toString());
        }
        return null;
    }
    
    public boolean isWordExist(String w){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM TargetWords WHERE LOWER(Word)=LOWER(?) ");
            preparedStatement.setString(1, w); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){  
                return true;
            } 
        } catch (Exception ex) {
            System.err.println("isWordExist HATAA "+ex.toString());
        }
        return false;
    }
    
    
    public int delete(long id){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("DELETE FROM TargetWords WHERE Id=? ");
            preparedStatement.setLong(1, id); 
            return  preparedStatement.executeUpdate();
           
        } catch (Exception ex) {
            System.err.println("deleteTargetWord HATAA "+ex.toString());
        } 
        return 0;
    }
    
    public int deleteByWord(String w){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("DELETE FROM TargetWords WHERE LOWER(Word)=LOWER(?)");
            preparedStatement.setString(1, w); 
            return  preparedStatement.executeUpdate();
           
        } catch (Exception ex) {
            System.err.println("deleteByWord HATAA "+ex.toString());
        } 
        return 0;
    }
    
    public void update(TargetWord tw){ 
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("UPDATE TargetWords SET Word=?,Verb=?,Noun=?,Adjective=?,Adverb=?,Preposition=?,Conjunction=? WHERE Id=?"); 
            preparedStatement.setString(1, tw.getWord());
            preparedStatement.setString(2, tw.getVerb());
            preparedStatement.setString(3, tw.getNoun());
            preparedStatement.setString(4, tw.getAdjective());
            preparedStatement.setString(5, tw.getAdverb());
            preparedStatement.setString(6, tw.getPreposition());
            preparedStatement.setString(7, tw.getConjunction());
            preparedStatement.setLong(8, tw.getId());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.err.println("updateTargetWord HATAA "+ex.toString());
        } 
    } 
}
