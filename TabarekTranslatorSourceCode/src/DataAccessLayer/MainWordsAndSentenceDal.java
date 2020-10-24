/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import tabarektranslator.Helper;

/**
 *
 * @author ThekME
 */
public class MainWordsAndSentenceDal {
    Connection conn=null;
    
    public void add(long mainWordId,long mainSentenceId){ 
        try {
            conn=Helper.getSQLConnection();
            PreparedStatement preparedStatement  = conn.prepareStatement("INSERT INTO MainWordsAndSentences VALUES(?,?);"); 
            preparedStatement.setLong(1,mainWordId);
            preparedStatement.setLong(2, mainSentenceId);
            preparedStatement.executeUpdate();  
        } catch (Exception ex) {
            System.err.println("addMainWordsAndSentence HATAA "+ex.toString());
        }  
    }  
}
