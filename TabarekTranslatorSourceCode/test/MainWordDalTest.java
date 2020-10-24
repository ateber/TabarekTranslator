
import DataAccessLayer.MainWordDal;
import DataAccessLayer.TargetWordDal;
import Entities.MainSentence;
import Entities.MainWord;
import Entities.TargetWord;
import java.time.Instant;

 
public class MainWordDalTest {
    public static void main(String[] args)   {
        MainWordDal mwDal=new MainWordDal(); 
        TargetWordDal tdDal=new TargetWordDal(); 
        
        
        
        
        //getAll
        for(MainWord entity:mwDal.get() ) 
            System.out.println(entity.getId()+" "+entity.getWord()+" "+entity.getDate());
        
        //getbyId
        System.out.println("Data whose Id is 2 -> "+mwDal.get(3).getWord());
        
        //add and return Id
        MainWord mw= new MainWord(); 
        mw.setWord("test");
        mw.setDate((int) Instant.now().getEpochSecond());
       // System.out.println(mwDal.add(mw)); 
        
        //update
        MainWord m=new MainWord();
        m.setId(2);
        m.setWord("MAIN");
        mwDal.update(m);

        
        //delete
        //System.out.println(mainWordDal.deleteMainWordByWord("Main"));
        
        //add MainWord and TargetWord
        MainWord mWord=new MainWord();
        mWord.setWord("MainWordAndTragetWord");
        mWord.setDate((int) Instant.now().getEpochSecond());
        long Id=mwDal.add(mWord);
        
        TargetWord tWord=new TargetWord();
        tWord.setMainWordId(Id);
        tWord.setWord("AnaKelimeVeHedefKelime"); 
        tWord.setDate((int) Instant.now().getEpochSecond());
        tdDal.add(tWord);
        
        //getDetail
        for(MainWord entity:mwDal.getDetail()) 
            System.out.println(entity.getId()+" "+entity.getWord()+" "+entity.getDate()+" "+entity.getTargetWord().getWord());
        //getDetailById
        System.out.println("Data whose Id is 45 -> "+mwDal.getDetail(45).getTargetWord().getWord());
    }
}
