
import DataAccessLayer.MainSentenceDal;
import DataAccessLayer.TargetSentenceDal;
import Entities.MainSentence; 
import Entities.TargetSentence;
import java.time.Instant;

 
public class MainSentenceDalTest {
     public static void main(String[] args)   {
        MainSentenceDal msDal=new MainSentenceDal(); 
        TargetSentenceDal tsDal=new TargetSentenceDal();
        
        
       
       
        
        //getAll
        for(MainSentence entity:msDal.get() ) 
            System.out.println(entity.getId()+" "+entity.getSentence()+" "+entity.getDate());
        //getbyId
        System.out.println("Data whose Id is 2 -> "+msDal.get(1).getSentence());
        
        //delet
        //System.out.println(mwDal.delete(4));
        
        
        //update
//        MainSentence newMS= new MainSentence();
//        newMS.setId(2);
//        newMS.setSentence("I does not go to School");
//        msDal.update(newMS);
        
        //add
        MainSentence ms= new MainSentence();
        ms.setSentence("I go to the school");
        ms.setDegree(2);
        ms.setDate((int) Instant.now().getEpochSecond());
        long mainSentenceId=msDal.add(ms);
        
        TargetSentence ts=new TargetSentence();
        ts.setMainSentenceId(mainSentenceId);
        ts.setSentence("Ben okula Gidiyorum lan");
        ts.setDate((int) Instant.now().getEpochSecond());
        tsDal.add(ts); 
        
        //getDetailAll
        for(MainSentence entity:msDal.getDetail() ) 
            System.out.println(entity.getId()+" "+entity.getSentence()+" "+entity.getDate()+ " "+entity.getTargetSentence().getSentence()+" "+entity.getTargetSentence().getId());
        //getDetailbyId
        System.out.println("Data whose Id is 5 -> "+msDal.getDetail(5).getSentence());
        
        
    }
}
