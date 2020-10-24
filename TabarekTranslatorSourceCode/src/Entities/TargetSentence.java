/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author ThekME
 */
public class TargetSentence {
    private long id;
    private long mainSentenceId;
    private String Sentence;
    private int date;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMainSentenceId() {
        return mainSentenceId;
    }

    public void setMainSentenceId(long mainSentenceId) {
        this.mainSentenceId = mainSentenceId;
    }

    public String getSentence() {
        return Sentence;
    }

    public void setSentence(String Sentence) {
        this.Sentence = Sentence;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
    
}
