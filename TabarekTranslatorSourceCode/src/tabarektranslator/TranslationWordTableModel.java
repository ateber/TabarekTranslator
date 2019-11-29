/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabarektranslator;
 
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 *
 * @author ThekME
 */
public class TranslationWordTableModel {
     public double getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(double accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTranslationsTheWord() {
        return translationsTheWord;
    }

    public void setTranslationsTheWord(String otherTranslationsWord) {
        this.translationsTheWord = otherTranslationsWord;
    }

    public TranslationWordTableModel(String content,String TranslationsTheWord,double accuracyRate ) {
        this.content=content;
        this.accuracyRate=accuracyRate;
        this.translationsTheWord=TranslationsTheWord;
    }
    
    public TranslationWordTableModel() { 
    } 
    
    private double accuracyRate=0;
    private String content=null;
    private String translationsTheWord=null;
}
