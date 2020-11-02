/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabarektranslator;

 
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger; 
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
 
 
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author ThekME   
 */ 
public class TabarekTranslator   {
    private String TRANSLATION_URL="https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=tr&dt=bd&dt=t&q=";
    private String TRANSLATION_URL2="http://translate.google.com/translate_a/single?client=t&dt=bd&dt=t&q=";
    private String TRANSLATION_URL3="http://translate.google.com/translate_a/single?client=t&sl=en&tl=tr&dt=bd&dt=t";
    private int TEXT_LIMIT=5000;
    
    public TranslationResponse translate(String source,String from,String to)   { 
        try { 
            String translation="";
            TranslationResponse translationResponse=new TranslationResponse();
            
            /*if(source.length()>TEXT_LIMIT){
                int textLength=source.length();
                int index=0;
                int endSentenceIndex=0;
                for( ;index<textLength;){
                    //endSentenceIndex=source.lastIndexOf(".", TEXT_LIMIT);
                    String patch=source.substring(index,Math.min(index + TEXT_LIMIT,textLength));
                    Response res = Request.Get(TRANSLATION_URL2+URLEncoder.encode(patch,"UTF-8")+"&tk="+Helper.tk(patch)) 
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 " +"(KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36" )
                    .execute();   
                    JSONArray jsonResult=new JSONArray( res.returnContent().toString()); 
                    res.discardContent(); 
                    for(int l=0;l<jsonResult.getJSONArray(0).length();l++){
                        translation+=jsonResult.getJSONArray(0).getJSONArray(l).getString(0);
                    }
                }
                translationResponse.setTranslation(translation);
                return translationResponse;
            } */
            if(source.length()>TEXT_LIMIT)
                source=source.substring(0, TEXT_LIMIT);
            Response res = Request.Get(TRANSLATION_URL+URLEncoder.encode(source,"UTF-8")+"&tk="+Helper.tk(source)+"&sl="+from+"&tl="+to) 
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 " +"(KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36" )
            .execute();   
            
            JSONArray jsonResult=new JSONArray( res.returnContent().toString()); 
            res.discardContent();  
            //System.out.println(" -> "+jsonResult.toString());
            
            for(int i=0;i<jsonResult.getJSONArray(0).length();i++){
                translation+=jsonResult.getJSONArray(0).getJSONArray(i).getString(0); 
            } 
            translationResponse.setTranslation(translation);
            
            
            JSONArray jsonOtherTranslations ; 
            if(!jsonResult.isNull(1)){
                jsonOtherTranslations=jsonResult.getJSONArray(1);
                
                for(int i=0;i<jsonOtherTranslations.length();i++){
                    //  ---------   what is  TYPE of Word   ------------------- 
                    String wordType=jsonOtherTranslations.getJSONArray(i).getString(0); //adjective,verb ,adverb vs ..
                    
                    //  ---------     Parse Words of related Type(adjective,verb,adverb,noun ...)  ------------------
                    List< TranslationWord> words=new ArrayList<TranslationWord>(); 
                    JSONArray jsonWords=jsonOtherTranslations.getJSONArray(i).getJSONArray(2);
                    for(int j=0;j<jsonWords.length();j++){ 
                        TranslationWord translationWord= new TranslationWord();

                        String content=jsonWords.getJSONArray(j).getString(0).trim();  //content  
                        translationWord.setContent(content);
                        
                        if(jsonWords.getJSONArray(j).isNull(3)){
                            translationWord.setAccuracyRate(0.0);
                        }
                        else{
                            Double accuracyRate=jsonWords.getJSONArray(j).getDouble(3);  //accuracyRate  
                            translationWord.setAccuracyRate(accuracyRate);
                        }
                        

                        JSONArray jsonTranslationsTheWord=jsonWords.getJSONArray(j).getJSONArray(1);
                        List< String> translationsTheWord=new ArrayList<String>(); 
                        for(int l=0;l<jsonTranslationsTheWord.length();l++){ 
                            translationsTheWord.add(jsonTranslationsTheWord.getString(l).trim());
                        }  
                        translationWord.setTranslationsTheWord(translationsTheWord);

                        words.add(translationWord);
                    }

                    
                    if(wordType.equals("verb")){ 
                        translationResponse.setVerbs(words);
                    }
                    else if(wordType.equals("adverb")){
                        translationResponse.setAdverbs(words);
                        //System.out.println("adverb");
                    }
                    else if(wordType.equals("adjective")){
                        translationResponse.setAdjectives(words);
                        //System.out.println("adjective");
                    }
                    else if(wordType.equals("conjunction")){
                        translationResponse.setConjunctions(words);
                        //System.out.println("preposition");
                    }
                    else if(wordType.equals("preposition")){
                        translationResponse.setPrepositions(words);
                        //System.out.println("preposition");
                    }
                    else if(wordType.equals("noun")||wordType.equals("pronoun")){
                        translationResponse.setNoun(words);
                       //System.out.println("noun");
                    }
//                    else if(wordType.equals("pronoun")){    note :  it was accepted as a noun ! example -> " my " me
//                        translationResponse.setNoun(words);
//                       //System.out.println("noun");
//                    }
                }
            } 
           
            return translationResponse;
        } catch (UnsupportedEncodingException ex) {
            System.err.println("error-1");
            Logger.getLogger(TabarekTranslator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.err.println("error-2");
            Logger.getLogger(TabarekTranslator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            System.err.println("error-3");
            Logger.getLogger(TabarekTranslator.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return null;
    }
    
  
}
