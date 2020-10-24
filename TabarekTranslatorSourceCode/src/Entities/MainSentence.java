 
package Entities;

import java.util.ArrayList;

 
public class MainSentence {
    private long id;
    private String sentence;
    private int degree;
    private int date;
    private TargetSentence targetSentence;
    
    public TargetSentence getTargetSentence() {
        return targetSentence;
    }

    public void setTargetSentence(TargetSentence targetSentence) {
        this.targetSentence = targetSentence;
    }
 
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public int getDegree(){
        return degree;
    }
    
    public void setDegree(int degree){
        this.degree=degree;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String Sentence) {
        this.sentence = Sentence;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    } 
}
