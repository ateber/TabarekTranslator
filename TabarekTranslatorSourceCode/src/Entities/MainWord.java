 
package Entities;

import java.util.ArrayList;

 
public class MainWord {
    private long id;
    private String word;
    private int date;
    private TargetWord targetWord;
    private ArrayList<MainSentence> mainSentences; 
    private int degree;
     
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
 
    public String getWord() {
        return word;
    }
 
    public void setWord(String word) {
        this.word = word;
    } 
    
    public int getDate() {
        return date;
    }
 
    public void setDate(int Date) {
        this.date = Date;
    } 

    public TargetWord getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(TargetWord targetWord) {
        this.targetWord = targetWord;
    }

    public ArrayList<MainSentence> getMainSentences() {
        return mainSentences;
    }

    public void setMainSentences(ArrayList<MainSentence> mainSentences) {
        this.mainSentences = mainSentences;
    } 
}
