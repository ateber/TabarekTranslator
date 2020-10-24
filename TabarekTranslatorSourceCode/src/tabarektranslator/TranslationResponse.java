 
package tabarektranslator;

import java.util.ArrayList;
import java.util.List;

 
public class TranslationResponse {

    public List<TranslationWord> getAdjectives() {
        return adjectives;
    }

    public void setAdjectives(List<TranslationWord> adjectives) {
        this.adjectives = adjectives;
    }

    public List<TranslationWord> getVerbs() {
        return verbs;
    }

    public void setVerbs(List<TranslationWord> verbs) {
        this.verbs = verbs;
    }

    public List<TranslationWord> getAdverbs() {
        return adverbs;
    }

    public void setAdverbs(List<TranslationWord> adverbs) {
        this.adverbs = adverbs;
    }

    public List<TranslationWord> getConjunctions() {
        return conjunctions;
    }

    public void setConjunctions(List<TranslationWord> conjunctions) {
        this.conjunctions = conjunctions;
    }

    public List<TranslationWord> getNoun() {
        return noun;
    }

    public void setNoun(List<TranslationWord> noun) {
        this.noun = noun;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
 
    public List<TranslationWord> getPrepositions() {
        return prepositions;
    }

    public void setPrepositions(List<TranslationWord> prepositions) {
        this.prepositions = prepositions;
    }
    
    public boolean isSentence() {
        return prepositions==null&&adjectives==null&&verbs==null&&adverbs==null&&conjunctions==null&&noun==null;
    }

    
    
    String translation=null;
    List<TranslationWord> prepositions=null; 
    List<TranslationWord> adjectives=null; 
    List<TranslationWord> verbs=null; 
    List<TranslationWord> adverbs=null; 
    List<TranslationWord> conjunctions=null; 
    List<TranslationWord> noun=null; 
    
    public void TranslateResponse(){ 
        
    }
    
    public void TranslateResponse(String translation){
       this.translation=translation;
    }
}
