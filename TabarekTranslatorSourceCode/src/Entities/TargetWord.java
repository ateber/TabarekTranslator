/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

 
public class TargetWord {
    private long id;
    private long mainWordId;
    private String word;
    private String verb;
    private String noun;
    private String adjective;
    private String adverb;
    private String preposition;
    private String conjunction;
    private int date;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMainWordId() {
        return mainWordId;
    }

    public void setMainWordId(long mainWordId) {
        this.mainWordId = mainWordId;
    }
    
    public String getWord() {
        return word;
    } 

    public void setWord(String word) {
        this.word = word;
    }

    public String getVerb() {
        return verb;
    } 

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public String getAdjective() {
        return adjective;
    }

    public void setAdjective(String adjective) {
        this.adjective = adjective;
    }

    public String getAdverb() {
        return adverb;
    }

    public void setAdverb(String adverb) {
        this.adverb = adverb;
    }

    public String getPreposition() {
        return preposition;
    }

    public void setPreposition(String preposition) {
        this.preposition = preposition;
    }

    public String getConjunction() {
        return conjunction;
    }

    public void setConjunction(String Conjunction) {
        this.conjunction = Conjunction;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
    
}
