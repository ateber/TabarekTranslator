/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabarektranslator;

import java.util.List;

/**
 *
 * @author ThekME
 */
public class TranslationWord {

    public Double getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(Double accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTranslationsTheWord() {
        return translationsTheWord;
    }

    public void setTranslationsTheWord(List<String> otherTranslationsWord) {
        this.translationsTheWord = otherTranslationsWord;
    }

    public TranslationWord(String content,Double accuracyRate,List<String> TranslationsTheWord) {
        this.content=content;
        this.accuracyRate=accuracyRate;
        this.translationsTheWord=TranslationsTheWord;
    }
    public TranslationWord() {
      
    }
    public TranslationWord(String content) {
        this.content=content; 
    }
    
    private Double accuracyRate=null;
    private String content=null;
    private List<String> translationsTheWord=null;
    
      

}
