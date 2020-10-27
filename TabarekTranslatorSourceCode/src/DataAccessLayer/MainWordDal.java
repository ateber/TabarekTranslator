 
package DataAccessLayer;
 
import Entities.MainWord;
import Entities.TargetWord;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tabarektranslator.Helper;

public class MainWordDal {
    
    Connection conn=null;
    
    public long add(MainWord mw){ 
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("INSERT INTO MainWords VALUES(NULL,?,?,?);",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,mw.getWord());
            preparedStatement.setInt(2, mw.getDegree());
            preparedStatement.setInt(3, mw.getDate());
            preparedStatement.executeUpdate();
            ResultSet rs=preparedStatement.getGeneratedKeys();
            if(rs.next()){
                return rs.getLong(1);
            }
        } catch (Exception ex) {
            System.err.println("addMainWord HATAA "+ex.toString());
        } 
        return -1;
    } 
    
    public ArrayList<MainWord> get(){
        try {
            conn=Helper.getSQLConnection();
            Statement statement  = conn.createStatement(); 
            ResultSet rs= statement.executeQuery("SELECT * FROM MainWords");
            if(rs!=null){
                ArrayList<MainWord> mws=new ArrayList<>();            
                while(rs.next()){  
                    MainWord mw=new MainWord();
                    mw.setId(rs.getLong("Id"));
                    mw.setWord(rs.getString("Word"));
                    mw.setDegree(rs.getInt("Degree"));
                    mw.setDate(rs.getInt("Date")); 
                    mws.add(mw);
                }
                return mws; 
            }
            else
                return null;  
        } catch (Exception ex) {
            System.err.println("getMainWord HATAA "+ex.toString());
        }
        return null;
    }  
    
    
    public MainWord get(long id){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM MainWords WHERE Id=? ");
            preparedStatement.setLong(1, id); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){ 
                MainWord mw=new MainWord();
                mw.setId(rs.getLong("Id"));
                mw.setWord(rs.getString("Word"));
                mw.setDegree(rs.getInt("Degree"));
                mw.setDate(rs.getInt("Date"));                
                return mw;
            } 
        } catch (Exception ex) {
            System.err.println("getMainWord HATAA "+ex.toString());
        }
        return null;
    }  
    
    public MainWord getByWord(String w){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM MainWords WHERE LOWER(Word)=LOWER(?) ");
            preparedStatement.setString(1, w); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){ 
                MainWord mw=new MainWord();
                mw.setId(rs.getLong("Id"));
                mw.setWord(rs.getString("Word"));
                mw.setDegree(rs.getInt("Degree"));
                mw.setDate(rs.getInt("Date")); 
                return mw;
            }
           
        } catch (Exception ex) {
            System.err.println("getIdByWord HATAA "+ex.toString());
        }
        return null;
    }
    
    public long isWordExist(String w){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("SELECT *  FROM MainWords WHERE LOWER(Word)=LOWER(?) ");
            preparedStatement.setString(1, w); 
            ResultSet rs= preparedStatement.executeQuery();
            if(rs.next()){  
                return rs.getLong("Id");
            } 
        } catch (Exception ex) {
            System.err.println("isWordExist HATAA "+ex.toString());
        }
        return -1;
    }
    
    
    public int delete(long id){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("DELETE FROM MainWords WHERE Id=? ");
            preparedStatement.setLong(1, id); 
            return  preparedStatement.executeUpdate();
           
        } catch (Exception ex) {
            System.err.println("deleteMainWord HATAA "+ex.toString());
        } 
        return 0;
    }
    
    public int deleteTargetWords(long id){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("DELETE FROM TargetWords WHERE MainWordId=? "); 
            preparedStatement.setLong(1, id);  
            return  preparedStatement.executeUpdate();
            
        } catch (Exception ex) {
            System.err.println("deleteDetail MainWord HATAA "+ex.toString());
        } 
        return 0;
    }
    
    public int deleteByWord(String w){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("DELETE FROM MainWords WHERE LOWER(Word)=LOWER(?)");
            preparedStatement.setString(1, w); 
            return  preparedStatement.executeUpdate();
           
        } catch (Exception ex) {
            System.err.println("deleteByWord HATAA "+ex.toString());
        } 
        return 0;
    }
    
    public void update(MainWord mw){ 
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("UPDATE MainWords SET Word=?,Degree=? WHERE Id=?");
            preparedStatement.setString(1,mw.getWord());
            preparedStatement.setInt(2, mw.getDegree());
            preparedStatement.setLong(3, mw.getId());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            
            System.err.println("updateMainWord HATAA "+ex.toString());
        } 
    } 
    
    public MainWord getDetail(long id){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement=conn.prepareStatement(
                "SELECT MainWords.Id ,MainWords.Word,MainWords.Degree,MainWords.Date ,TargetWords.Id as TargetWordId , TargetWords.Word as TargetWord,TargetWords.Verb, TargetWords.Noun,TargetWords.Adjective,TargetWords.Adverb ,TargetWords.Preposition,TargetWords.Conjunction,TargetWords.Date as TargetWordDate \n" +
                "FROM MainWords \n" +
                "INNER JOIN TargetWords ON MainWords.Id=TargetWords.MainWordId \n" +
                "WHERE MainWords.Id=?");
            preparedStatement.setLong(1,id);
            ResultSet rs=preparedStatement.executeQuery(); 
            if(rs.next()){
                MainWord mw=new MainWord();
                mw.setTargetWord(new TargetWord());     
                mw.setId(rs.getLong("Id"));
                mw.setWord(rs.getString("Word"));
                mw.setDegree(rs.getInt("Degree"));
                mw.setDate(rs.getInt("Date"));
                mw.getTargetWord().setId(rs.getLong("TargetWordId"));
                mw.getTargetWord().setWord(rs.getString("TargetWord"));
                mw.getTargetWord().setVerb(rs.getString("Verb"));
                mw.getTargetWord().setNoun(rs.getString("Noun"));
                mw.getTargetWord().setAdjective(rs.getString("Adjective"));
                mw.getTargetWord().setAdverb(rs.getString("Adverb"));
                mw.getTargetWord().setPreposition(rs.getString("Preposition"));
                mw.getTargetWord().setConjunction(rs.getString("Conjunction"));
                mw.getTargetWord().setDate(rs.getInt("TargetWordDate"));
                return mw;                
            } 
        } catch (Exception ex) {
            System.err.println("getDetailById HATAA "+ex.toString());
        }
        return null;
    }
    
    public MainWord getDetailByWord(String word){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement=conn.prepareStatement(
                "SELECT MainWords.Id ,MainWords.Word,MainWords.Degree,MainWords.Date ,TargetWords.Id as TargetWordId , TargetWords.Word as TargetWord,TargetWords.Verb, TargetWords.Noun,TargetWords.Adjective,TargetWords.Adverb ,TargetWords.Preposition,TargetWords.Conjunction,TargetWords.Date as TargetWordDate \n" +
                "FROM MainWords \n" +
                "INNER JOIN TargetWords ON MainWords.Id=TargetWords.MainWordId \n" +
                "WHERE LOWER(MainWords.Word)=LOWER(?)"); 
            preparedStatement.setString(1,word);
            ResultSet rs=preparedStatement.executeQuery(); 
            if(rs.next()){
                MainWord mw=new MainWord();
                mw.setTargetWord(new TargetWord());     
                mw.setId(rs.getLong("Id"));
                mw.setWord(rs.getString("Word"));
                mw.setDegree(rs.getInt("Degree"));
                mw.setDate(rs.getInt("Date"));
                mw.getTargetWord().setId(rs.getLong("TargetWordId"));
                mw.getTargetWord().setWord(rs.getString("TargetWord"));
                mw.getTargetWord().setVerb(rs.getString("Verb"));
                mw.getTargetWord().setNoun(rs.getString("Noun"));
                mw.getTargetWord().setAdjective(rs.getString("Adjective"));
                mw.getTargetWord().setAdverb(rs.getString("Adverb"));
                mw.getTargetWord().setPreposition(rs.getString("Preposition"));
                mw.getTargetWord().setConjunction(rs.getString("Conjunction"));
                mw.getTargetWord().setDate(rs.getInt("TargetWordDate"));
                return mw;                
            } 
        } catch (Exception ex) {
            System.err.println("getDetailById HATAA "+ex.toString());
        }
        return null;
    }
    
    public ArrayList<MainWord> getDetailConstainInWord(String constain){
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement=conn.prepareStatement(
                "SELECT MainWords.Id ,MainWords.Word,MainWords.Degree,MainWords.Date ,TargetWords.Id as TargetWordId , TargetWords.Word as TargetWord,TargetWords.Verb, TargetWords.Noun,TargetWords.Adjective,TargetWords.Adverb ,TargetWords.Preposition,TargetWords.Conjunction,TargetWords.Date as TargetWordDate \n" +
                "FROM MainWords \n" +
                "INNER JOIN TargetWords ON MainWords.Id=TargetWords.MainWordId \n" +
                " WHERE instr(LOWER(MainWords.Word), LOWER(?)) > 0"); 
            preparedStatement.setString(1,constain);
            ResultSet rs=preparedStatement.executeQuery(); 
            ArrayList<MainWord> mws=new ArrayList<>();
             
            while(rs.next()){
                MainWord mw=new MainWord();
                mw.setTargetWord(new TargetWord());     
                mw.setId(rs.getLong("Id"));
                mw.setWord(rs.getString("Word"));
                mw.setDegree(rs.getInt("Degree"));
                mw.setDate(rs.getInt("Date"));
                mw.getTargetWord().setId(rs.getLong("TargetWordId"));
                mw.getTargetWord().setWord(rs.getString("TargetWord"));
                mw.getTargetWord().setVerb(rs.getString("Verb"));
                mw.getTargetWord().setNoun(rs.getString("Noun"));
                mw.getTargetWord().setAdjective(rs.getString("Adjective"));
                mw.getTargetWord().setAdverb(rs.getString("Adverb"));
                mw.getTargetWord().setPreposition(rs.getString("Preposition"));
                mw.getTargetWord().setConjunction(rs.getString("Conjunction"));
                mw.getTargetWord().setDate(rs.getInt("TargetWordDate"));
                mws.add(mw) ;                
            } 
            return mws;
        } catch (Exception ex) {
            System.err.println("getDetailByConstain HATAA "+ex.toString());
        }
        return null;
    }
    
    public ArrayList<MainWord> getDetail(){
        try {
            conn=Helper.getSQLConnection();
            Statement statement  = conn.createStatement(); 
            ResultSet rs=statement.executeQuery(
                "SELECT MainWords.Id ,MainWords.Word,MainWords.Degree,MainWords.Date ,TargetWords.Id as TargetWordId , TargetWords.Word as TargetWord,TargetWords.Verb, TargetWords.Noun,TargetWords.Adjective,TargetWords.Adverb ,TargetWords.Preposition,TargetWords.Conjunction,TargetWords.Date as TargetWordDate \n" +
                "FROM MainWords \n" +
                "INNER JOIN TargetWords ON MainWords.Id=TargetWords.MainWordId"); 
            
            ArrayList<MainWord> mws=new ArrayList<>();
            while(rs.next()){
                MainWord mw=new MainWord();
                mw.setTargetWord(new TargetWord());     
                mw.setId(rs.getLong("Id"));
                mw.setWord(rs.getString("Word"));
                mw.setDegree(rs.getInt("Degree"));
                mw.setDate(rs.getInt("Date"));
                mw.getTargetWord().setId(rs.getLong("TargetWordId"));
                mw.getTargetWord().setWord(rs.getString("TargetWord"));
                mw.getTargetWord().setVerb(rs.getString("Verb"));
                mw.getTargetWord().setNoun(rs.getString("Noun"));
                mw.getTargetWord().setAdjective(rs.getString("Adjective"));
                mw.getTargetWord().setAdverb(rs.getString("Adverb"));
                mw.getTargetWord().setPreposition(rs.getString("Preposition"));
                mw.getTargetWord().setConjunction(rs.getString("Conjunction"));
                mw.getTargetWord().setDate(rs.getInt("TargetWordDate")); 
                mws.add(mw);
            } 
            return mws;  
        } catch (Exception ex) {
            System.err.println("getDetail HATAA "+ex.toString());
        }
        return null;
    } 
}
