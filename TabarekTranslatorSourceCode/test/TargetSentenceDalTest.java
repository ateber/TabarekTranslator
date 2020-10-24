 
import DataAccessLayer.TargetSentenceDal; 
import Entities.TargetSentence;
import java.time.Instant;


public class TargetSentenceDalTest {
    
    public static void main(String[] args) {
        TargetSentenceDal tsDal=new TargetSentenceDal(); 
        TargetSentence ts= new TargetSentence();  
       
        
        
        ts.setSentence("Ben okula gidiyorum");
        ts.setMainSentenceId(5);
        ts.setDate((int) Instant.now().getEpochSecond());
        
        //add
        //tsDal.add(ts);
        
        //getAll
        for(TargetSentence entity:tsDal.get() ) 
            System.out.println(entity.getId()+" "+entity.getSentence()+" "+entity.getDate());
        //getbyId
        System.out.println("Data whose Id is 2 -> "+tsDal.get(1).getSentence());
        
        //delet
        //System.out.println(tsDal.delete(3));
        
        
        //update
        TargetSentence newTS= new TargetSentence();
        newTS.setId(2);
        newTS.setSentence("Ben okula gitmiyorum");
        tsDal.update(newTS);
        
        
    }
    
        
        
}
