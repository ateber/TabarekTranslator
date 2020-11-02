//windows 
package tabarektranslator;

 
import DataAccessLayer.MainSentenceDal;
import DataAccessLayer.MainWordDal;
import DataAccessLayer.MainWordsAndSentenceDal;
import DataAccessLayer.TargetSentenceDal;
import DataAccessLayer.TargetWordDal;
import Entities.MainSentence;
import Entities.MainWord;
import Entities.TargetSentence;
import Entities.TargetWord;
import com.sun.javafx.css.StyleManager;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.systemTray.Separator;
import java.awt.Dimension; 
import java.awt.MouseInfo;
import java.awt.Point;
 
import java.awt.Robot;
import java.awt.Toolkit;
 
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
 
 
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date; 
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask; 
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
 
 
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
 
 
 
import javafx.application.Application;  
import javafx.application.Platform; 
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
 
import javafx.concurrent.Task;
 
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos; 
import javafx.scene.Cursor;
 
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
 
import javafx.scene.control.Label; 
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent; 
import javafx.scene.layout.Background;
 
 
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
 
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
 
 
import javafx.stage.Stage; 
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
 
 
 
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputAdapter;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseMotionAdapter;
import org.jnativehook.mouse.NativeMouseMotionListener;
 

 
public class MainApp extends Application{
    String USER_DIR = System.getProperty("user.dir"); 
    Robot robot ; 
    TabarekTranslator translator;
    volatile String  clipboardText ;
    volatile String  translatedText ;    
    
    Stage shortcutPopupIcon ;
    TabPane WordTypesTabPane;
    TextArea mainTranslationTA;
    HBox titleBarHBox;
    Stage translationStage;
    Scene translationScene;
    VBox mainLayoutVBox;
    
    private double xOffset  = 0;
    private double yOffset  = 0;
    private int resizeState = 0;
    private double dx=0;
    private double dy=0;
    double firstX=0;
    double firstY=0;
    double firstWidth=0;
    double firstHeight=0;
    double bigHeight=0;
    double totalWidth=0;
    
    boolean shortcutPopupIconEnteredState=false;
    boolean translatorPopupEnteredState=false;
    boolean robotKeyEventState=false;
    boolean showNewlineState=false;
            
    String SystemCopyData; 
    
    TextTransfer clip=new TextTransfer();  
    TranslationResponse translation;
    Text textComputer=new Text();
    String toLang="tr";
    String fromLang="en";
     
    HBox progressHB;
    Stage wordPoolStage;
    Stage sentencePoolStage;
    Stage progressStage;
    Stage favoriteDialog;
    Stage stg;
    Dimension screenSize;
    
    NativeMouseMotionListener nativeMouseMotionListener;
    NativeMouseInputListener nativeMouseListener ;
    NativeKeyListener nativeKeyListener;
    
    DateFormat dateFormat;
    Date date; 
    
    MainWordDal _mainWordDal;
    TargetWordDal _targetWordDal;
    MainSentenceDal _mainSentenceDal;
    TargetSentenceDal _targetSentenceDal;
    MainWordsAndSentenceDal _mainWordsAndSentenceDal; 
    
    ArrayList<MainSentence> mainSentences;
    ArrayList<MainWord> mainWords;
    
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {  
        
        createWordPool(); 
        createSentencePool();
        
        _mainWordDal=new MainWordDal();
        _targetWordDal=new TargetWordDal();
        _mainSentenceDal=new MainSentenceDal();
        _targetSentenceDal=new TargetSentenceDal();
        _mainWordsAndSentenceDal=new MainWordsAndSentenceDal();
        
        dateFormat= new SimpleDateFormat("dd/MM/yyyy"); //HH:mm:ss
        dateFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        date=new Date();
        
        //System.setProperty("jnativehook.button.multiclick.iterval", "1000");    
        translator=new TabarekTranslator();           //Translator init  
        robot=new Robot();
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        StyleManager.getInstance().addUserAgentStylesheet("styles/app.css");
       
        SystemTray.AUTO_SIZE=true;
        SystemTray.AUTO_FIX_INCONSISTENCIES=true;
        SystemTray systemTray = SystemTray.get();   
        systemTray.setTooltip("Tabarek Translate");
        
     
        if (systemTray == null) {
            System.out.println("Sistem tepsisi desteklenmiyor..");
            throw new RuntimeException("Unable to load SystemTray!");
        }  
       
        try {
            systemTray.setImage(USER_DIR+"/src/Icon/second.png"); 
        } catch ( Exception e) {
            System.out.println("Sistem tepsisi resim bulunamadı.."+e.getMessage()); 
        }
        MenuItem wordPool=new MenuItem("Word Pool");
        MenuItem sentencePool=new MenuItem("Sentence Pool"); 
        MenuItem options=new MenuItem("Options"); 
        MenuItem about=new MenuItem("About");
        dorkbox.systemTray.Checkbox showTIcon=new dorkbox.systemTray.Checkbox ("Show T Icon"); 
        dorkbox.systemTray.Checkbox cutNewline=new dorkbox.systemTray.Checkbox ("Cut Newline"); 
        MenuItem exit=new MenuItem("Exit"); 
        showTIcon.setChecked(true); 
        cutNewline.setChecked(false);
        
        wordPool.setCallback(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) { 
                try{ 
                    Platform.runLater(() -> { 
                        mainWords=_mainWordDal.getDetail();
                        putMainWordsToTreeTableView(mainWords); 
                        wordPoolStage.show();
                    }); 
                }catch(Exception e){
                    System.out.println(e.toString());
                }
                
            }
        });
        
        sentencePool.setCallback(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) { 
                Platform.runLater(() -> {
                    mainSentences=_mainSentenceDal.getDetail();
                    putMainSentencesToTreeTableView(mainSentences,getTreeItemViewOfSentencePoolStage());
                    sentencePoolStage.show();
                }); 
            }
        });
        
        showTIcon.setCallback((e) -> { 
            if(showTIcon.getChecked()==false){
                try {
                    systemTray.setImage(USER_DIR+"/src/Icon/first_icon.png"); 
                } catch ( Exception ex) {
                    System.out.println("Sistem tepsisi resim bulunamadı.."+ex.getMessage()); 
                }
                showTIcon.setChecked(false); 
                
                GlobalScreen.removeNativeMouseListener(nativeMouseListener);  
            }
            else{ 
                try {
                    systemTray.setImage(USER_DIR+"/src/Icon/second.png");  // for exe -> systemTray.setImage("Icon/second.png");  
                } catch ( Exception ex) {
                    System.out.println("Sistem tepsisi resim bulunamadı.."+ex.getMessage()); 
                }
                showTIcon.setChecked(true);
                GlobalScreen.addNativeMouseListener(nativeMouseListener); 
            } 
        });
        
        cutNewline.setCallback((ae) -> {
            if(cutNewline.getChecked()==false){ 
                showNewlineState=true;
                cutNewline.setChecked(false);  
            }
            else{  
                showNewlineState=true;
                cutNewline.setChecked(true); 
            } 
        });
        
        
        
        exit.setCallback(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                systemTray.shutdown();
                //Platform.exit();
                System.exit(0);
            } 
        });
        
        Menu mainMenu = systemTray.getMenu();   
        mainMenu.add(wordPool);
        mainMenu.add(sentencePool); 
        mainMenu.add(options);
        mainMenu.add(about );
        mainMenu.add(new Separator());    
        mainMenu.add(showTIcon );
        mainMenu.add(cutNewline);
        mainMenu.add(new Separator());         
        mainMenu.add(exit);
        
       
        
           
        // ----------------------- END --------------------------------------------
        
        // ------------------------- GEREKLİ STAGE  ------------------------------------
        stg=new Stage(StageStyle.UTILITY); 
        stg.setOpacity(0);
        stg.setHeight(0);
        stg.setWidth(0);
        stg.show(); 
        
        // ------------------------- END GEREKLİ STAGE  ------------------------------------
        
        //-------------------- FAVORİ DİALOG ----------------------------------------
        favoriteDialog= new Stage(); 
        HBox dialogHbox = new HBox(); 
        dialogHbox.setBackground(Background.EMPTY); 
        
        //dialogHbox.setSpacing(1);
        ArrayList<ImageView> favoriteStars=new ArrayList<>();
        for(int i=0;i<5;i++){  
            
            ImageView iv= new ImageView("Icon/favoriteButton.jpg") ;
            iv.setOnMousePressed((event) -> {
                int index=favoriteStars.indexOf(iv);  
                
                if(favoriteDialog.getUserData()!=null){
                    System.out.println("kontrol 1"); 
                    if(favoriteDialog.getUserData() instanceof MainWord){
                        MainWord mw=(MainWord)favoriteDialog.getUserData();
                        mw.setDegree(index+1);
                        _mainWordDal.update(mw);
                        mw.getTargetWord().setWord(mainTranslationTA.getText());
                        _targetWordDal.update(mw.getTargetWord());
                    }
                    if(favoriteDialog.getUserData() instanceof MainSentence){ 
                        MainSentence ms=(MainSentence)favoriteDialog.getUserData();
                        ms.setDegree(index+1);
                        _mainSentenceDal.update(ms);
                        ms.getTargetSentence().setSentence(mainTranslationTA.getText());
                        _targetSentenceDal.update(ms.getTargetSentence());
                    }
                }
                else{ 
                    System.out.println("kontrol 2");
                    Object text=addWordOrSentence(translatedText,mainTranslationTA.getText(),index+1,translation);
                    favoriteDialog.setUserData(text);
                } 
                
                CheckBox cb=(CheckBox)titleBarHBox.getChildren().get(5);
                ChangeListener<Boolean> cl=(ChangeListener)cb.getUserData();
                cb.selectedProperty().removeListener(cl);
                cb.setSelected(true);
                cb.selectedProperty().addListener(cl);  
                
                favoriteDialog.hide();  
                translationStage.toFront(); 
                
            });  
             
            iv.setOnMouseEntered((event) -> { 
                int index=favoriteStars.indexOf(iv);
                final Image image=new Image("Icon/favoriteButton2.jpg");
                for(int j=0;j<=index;j++){ 
                    HBox hb=(HBox)favoriteDialog.getScene().getRoot();
                    ImageView imageView= (ImageView)hb.getChildren().get(j);
                    imageView.setImage(image);
                } 
            }); 
            
            iv.setOnMouseExited((event) -> { 
                int index=favoriteStars.indexOf(iv);
                final Image image=new Image("Icon/favoriteButton.jpg");
                for(int j=0;j<=index;j++){ 
                    HBox hb=(HBox)favoriteDialog.getScene().getRoot();
                    ImageView imageView= (ImageView)hb.getChildren().get(j);
                    imageView.setImage(image);
                } 
            }); 
            
            Rectangle rec=new Rectangle(16,16); 
            rec.setArcWidth(100);
            rec.setArcHeight(100); 
            iv.setClip(rec);  
          
            favoriteStars.add(iv); 
        } 
        
        dialogHbox.getChildren().addAll( favoriteStars );
        
        Scene dialogScene = new Scene(dialogHbox); 
        dialogScene.setFill(Color.TRANSPARENT);
        
        
        favoriteDialog.setAlwaysOnTop(true);
        favoriteDialog.initOwner(stg);
        favoriteDialog.setScene(dialogScene); 
        //favoriteDialog.initModality(Modality.APPLICATION_MODAL);
        favoriteDialog.initStyle(StageStyle.TRANSPARENT);
       
        // ----------------------- FAVORİ DİALOG END _-----------------------------
        
       //-------------------------- Progress Indicator  ------------------------------------ 
       
        
        progressHB=new HBox();  
        progressHB.getStyleClass().add("progressHB"); 
        progressStage=new Stage(); 
        progressStage.initOwner( stg );  
        progressStage.setScene(new Scene(progressHB));  
        progressStage.initStyle(StageStyle. TRANSPARENT);  
        progressStage.getScene().setFill(Color.TRANSPARENT); 
        progressStage.setAlwaysOnTop(true); 
        progressStage.setOpacity(0);
        progressStage.show();
         
        
        //-------------------------- END Progress Indicator  ------------------------------------
        
        // ----------------------------     TOOL      ------------------------------ 
         
        mainLayoutVBox=new VBox();
        mainLayoutVBox.setId("mainLayoutVBox");
        mainLayoutVBox.setMaxWidth(600);
        mainLayoutVBox.setMaxHeight(300);
        mainLayoutVBox.setMinHeight(63);
        mainLayoutVBox.setMinWidth(150);
         
        // ------------------------- Tool Title Bar ------------------------------------set show  
        
        titleBarHBox=new HBox(); 
        titleBarHBox.setId("titleBarHBox");
        titleBarHBox.setOnMouseDragged((event) -> {
            if(resizeState==0){
                translationStage.setX(event.getScreenX() + xOffset);
                translationStage.setY(event.getScreenY() + yOffset);
            } 
        }); 
        mainLayoutVBox.getChildren().add(titleBarHBox); 
        mainLayoutVBox.setMargin(titleBarHBox, new Insets(0,-1, 0, -1));
         
        Button exitButton=new Button();
        exitButton.setId("green");
        titleBarHBox.getChildren().add(exitButton); 
        exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(resizeState==0){
                    translationStage.setOpacity(0);
                    favoriteDialog.hide();
                }
                    
            }
        });
           
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        titleBarHBox.getChildren().add(region1);
        
        Label titleBarLanguageLabel=new Label();
        titleBarLanguageLabel.setText( Helper.capitalize(fromLang) +" To "+Helper.capitalize(toLang));
        titleBarLanguageLabel.setId("titleBarLanguageLabel");
        titleBarHBox.getChildren().add(titleBarLanguageLabel);
         
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);
        titleBarHBox.getChildren().add(region2);
         
        Button swapButton=new Button();
        swapButton.setId("swapButton");
        swapButton.setOnMouseClicked((event) -> {
            String temp=fromLang;   //swap
            fromLang=toLang;
            toLang=temp;
            titleBarLanguageLabel.setText( Helper.capitalize(fromLang)+" To "+ Helper.capitalize(toLang)); 
            showTool(translationStage.getX(), translationStage.getY(), mainTranslationTA.getText());
        });
        titleBarHBox.getChildren().add(swapButton);
 
        CheckBox favoriteCB=new CheckBox();
        favoriteCB.setId("check-box");  
        ChangeListener<Boolean> favoriteCL=new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //System.out.println("");
                if(favoriteDialog.getUserData()!=null){ 
                    System.out.println("GELDİ 1"); 
                    if(favoriteDialog.isShowing()){ 
                        if(favoriteDialog.getUserData() instanceof MainWord){
                            MainWord mw=(MainWord)favoriteDialog.getUserData();
                            mw.setDegree(3);
                            _mainWordDal.update(mw);
                            String targetWord=Helper.capitalize(mainTranslationTA.getText());
                            mw.getTargetWord().setWord(targetWord);
                            _targetWordDal.update(mw.getTargetWord());
                            System.out.println("GELDİ a"); 
                        }
                        else if(favoriteDialog.getUserData() instanceof MainSentence){ 
                            MainSentence ms=(MainSentence)favoriteDialog.getUserData();
                            ms.setDegree(3);
                            _mainSentenceDal.update(ms);
                            String targetSentence=Helper.capitalize(mainTranslationTA.getText());
                            ms.getTargetSentence().setSentence(targetSentence);
                            _targetSentenceDal.update(ms.getTargetSentence());
                             System.out.println("GELDİ b"); 

                        }
                        else{
                            addWordOrSentence(translatedText,mainTranslationTA.getText(),3,translation); 
                        }
                        favoriteDialog.hide();
                        translationStage.toFront();
                       
                    } 
                    else{ 
                        //favoriteCB.setSelected(false);  
                        favoriteDialog.setX(translationStage.getX()+favoriteCB.getLayoutX()-31 );
                        favoriteDialog.setY(translationStage.getY()+favoriteCB.getLayoutY()-20);  
                        favoriteDialog.show();  
                        translationStage.toFront(); 
                    } 
                }  
          
                else{  
                    if(favoriteDialog.isShowing()&&newValue ){  
                        System.out.println("GELDİ 2"); 
                        favoriteDialog.hide();
                        translationStage.toFront();
                        Object text=addWordOrSentence(translatedText,mainTranslationTA.getText(),3,translation);
                        favoriteDialog.setUserData(text); 
                    } 
                    else {
                        System.out.println("GELDİ 3"); 
                        favoriteCB.selectedProperty().removeListener(this);
                        favoriteCB.setSelected(false);
                        favoriteCB.selectedProperty().addListener(this);   
                        favoriteDialog.setX(translationStage.getX()+favoriteCB.getLayoutX()-31 );
                        favoriteDialog.setY(translationStage.getY()+favoriteCB.getLayoutY()-20);  
                        favoriteDialog.show(); 
                        translationStage.toFront();  
                    } 
                }
            }
        };
        favoriteCB.setUserData(favoriteCL);  
        favoriteCB.selectedProperty().addListener(favoriteCL);  
        titleBarHBox.getChildren().add(favoriteCB);
        
        
        Button speechButton=new Button();
        speechButton.setId("speechButton");
        titleBarHBox.getChildren().add(speechButton);
        
        // ------------------------- END Tool Title Bar ------------------------------------
        
        //------------------------- Main Tool Translation Text Area  -------------------------
        
        mainTranslationTA=new TextArea(); 
//        mainTranslationTA.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {  
//               
//            }
//        });
        
        
        //mainTranslationTA.setMaxSize(600,500); 
        mainTranslationTA.setMinSize(50,30); 
        //mainTranslationTA.setPrefSize(50,50);
        mainTranslationTA.setId("mainTranslationTA"); 
        mainLayoutVBox.getChildren().add(mainTranslationTA);
        mainLayoutVBox.setMargin(mainTranslationTA, new Insets(4, 4, 4, 4));
         
        
         //------------------------- END of Tool Main Translation Text Area  -------------------------
        
        //-------------------  Tool Word Types Tab Pane  ------------------------  show clipboardText  
        WordTypesTabPane=new TabPane();
        WordTypesTabPane.setMinWidth(235);
        //WordTypesTabPane.setMinHeight(0);
        WordTypesTabPane.setId("WordTypesTabPane");
        WordTypesTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        mainLayoutVBox.setMargin(WordTypesTabPane, new Insets(0, 0, 4, 0));
        
        
        String  wordTypeEnglish[] ={"verb","noun","adjective","adverb","preposition","conjunction"}; 
        String  wordTypeTurkish[] ={"fiil","isim","sıfat","zarf","edat","bağlaç"};
        
        for(int i=0;i<wordTypeTurkish.length;i++){ 
            TableColumn< TranslationWord,String> tc1=new TableColumn( ); 
            TableColumn< TranslationWord,String> tc2=new < TranslationWord,Text>TableColumn( );
            
            tc1.setCellValueFactory(new PropertyValueFactory<>("content"));
            tc2.setCellValueFactory(new PropertyValueFactory<>("translationsTheWord"));   
  
            tc2.getStyleClass().add("translationsTheWordColumn"); 
            tc1.getStyleClass().add("contentColumn");
        
            tc1.setCellFactory( (param) -> {
                return new TableCell< TranslationWord,String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) { 
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        setText(item);
                    } 
                    
                    @Override
                    public void updateIndex(int i) {
                        super.updateIndex(i); 
                        if(i<param.getTableView().getItems().size()&&i>-1){ 
                            if(param.getTableView().getItems().get(i).getAccuracyRate()>0.1){
                                setStyle("-fx-border-color:green;");
                            }
                            else if(param.getTableView().getItems().get(i).getAccuracyRate()>0.01){
                                
                                setStyle("-fx-border-color:blue;");
                            }
                            else {
                                setStyle("-fx-border-color:red;");
                            } 
                        }
                        else{
                            setStyle("-fx-border-color: -fx-background;");
                        }
                    }  
                }; 
            });
           
            TableView <TranslationWord> tv=new <TranslationWord>TableView();  
            tv.widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                    Pane header = (Pane) tv.lookup("TableHeaderRow");
                    if (header.isVisible()){
                        header.setMaxHeight(0);
                        header.setMinHeight(0);
                        header.setPrefHeight(0);
                        header.setVisible(false);
                    }
                }
            }); 
            tv.setFixedCellSize(24); 
            tv.getColumns().addAll(tc1,tc2 ); 
            tv.getStyleClass().add("tableStyle");
            Tab wordTypeTab = new Tab(wordTypeTurkish[i]);  
            wordTypeTab.setContent(tv); 
            WordTypesTabPane.getTabs().add(wordTypeTab); 
        } 
        
        translationScene=new Scene(mainLayoutVBox);
        translationScene.setFill(Color.TRANSPARENT);
        translationScene.setOnMouseEntered((event) -> {
            translatorPopupEnteredState=true;
            if(translationStage.getOpacity()!=0)
                translationStage.setOpacity(1);
        });
        translationScene.setOnMouseExited((event) -> {
            translatorPopupEnteredState=false;
            if(translationStage.getOpacity()!=0)
                translationStage.setOpacity(0.91);
        });
      
        translationScene.addEventFilter(MouseEvent.MOUSE_PRESSED,(event) -> {   
            xOffset = translationStage.getX() - event.getScreenX();
            yOffset = translationStage.getY() - event.getScreenY(); 
            dx = translationStage.getWidth() - event.getScreenX();
            dy = translationStage.getHeight() - event.getScreenY(); 
            firstX=event.getScreenX();
            firstY=event.getScreenY();
            firstHeight=translationStage.getHeight();
            firstWidth=translationStage.getWidth();
            
        });
         
        translationScene.addEventFilter(MouseEvent.MOUSE_DRAGGED,(event) -> { 
            if(resizeState!=0)
                translationStage.setOpacity(1);
            if(resizeState==3||resizeState==9||resizeState==6){
                if(event.getScreenX()+ dx > translationStage.getMinWidth()){
                    translationStage.setWidth(event.getScreenX()+ dx);
                    mainTranslationTA.setPrefWidth( event.getScreenX()+ dx);
                } 
            }
            else if(resizeState==7||resizeState==1||resizeState==4){
                if((firstX-  event.getScreenX()+firstWidth) > translationStage.getMinWidth()){
                    translationStage.setWidth(firstX-  event.getScreenX()+firstWidth);
                    translationStage.setX( event.getScreenX()+xOffset  );
                    mainTranslationTA.setPrefWidth(firstX-  event.getScreenX()+firstWidth);
                } 
            } 
            if(resizeState==3||resizeState==1||resizeState==2){
                if(event.getScreenY()+ dy > translationStage.getMinHeight()){
                    translationStage.setHeight(event.getScreenY()+ dy);  
                    WordTypesTabPane.setPrefHeight(event.getScreenY()+ dy );
                    if(WordTypesTabPane.getParent()==null) 
                        mainTranslationTA.setPrefHeight(event.getScreenY() + dy); 
                } 
            } 
            else if(resizeState==7||resizeState==9||resizeState==8){
                if((firstY- event.getScreenY()+firstHeight) > translationStage.getMinHeight()){
                    translationStage.setHeight(firstY- event.getScreenY()+firstHeight);  
                    translationStage.setY( event.getScreenY()+yOffset );  
                    WordTypesTabPane.setPrefHeight(firstY- event.getScreenY()+firstHeight);
                    if(WordTypesTabPane.getParent()==null)
                        mainTranslationTA.setPrefHeight(firstY- event.getScreenY()+firstHeight);
                } 
            } 
            favoriteDialog.setX(translationStage.getX()+favoriteCB.getLayoutX()-31);
            favoriteDialog.setY(translationStage.getY()+favoriteCB.getLayoutY()-20);  
        });
 
        translationScene.addEventFilter(MouseEvent.MOUSE_MOVED, (event) -> {  
            if (event.getX() > translationStage.getWidth() - 10 && event.getY() > translationStage.getHeight() - 10) { 
                translationScene.setCursor(Cursor.SE_RESIZE); 
                resizeState=3; 
            }
            else if(event.getX() <   5  && event.getY() < 5  ){
                translationScene.setCursor(Cursor.SE_RESIZE);
                resizeState=7;
            }
            else if(event.getX() > translationStage.getWidth() - 10  && event.getY() < 5  ){
                translationScene.setCursor(Cursor.SW_RESIZE);
                resizeState=9;
            }
            else if(event.getX() <   5  && event.getY() > translationStage.getHeight() - 10 ){
                translationScene.setCursor(Cursor.SW_RESIZE);
                resizeState=1;
            }
            else if(event.getX() <   5   ){
                translationScene.setCursor(Cursor.E_RESIZE);
                resizeState=4;
            }
            else if( event.getX() > translationStage.getWidth() - 5  ){
                translationScene.setCursor(Cursor.E_RESIZE);
                resizeState=6;
            }
            else if(event.getY() > translationStage.getHeight() - 10    ){
                translationScene.setCursor(Cursor.N_RESIZE);
                resizeState=2;
            }
            else if( event.getY() < 5   ){
                translationScene.setCursor(Cursor.N_RESIZE);
                resizeState=8;
            } 
            else  {
                translationScene.setCursor(Cursor.DEFAULT);
                resizeState=0; 
            }    
        });
        
        translationStage=new Stage(StageStyle. TRANSPARENT);  
        translationStage.initOwner(stg); 
        translationStage.setMaxWidth(600);
        translationStage.setMinWidth(150);
        translationStage.setMaxHeight(300); 
        translationStage.setMinHeight(63);  
        translationStage.setAlwaysOnTop(true); 
        translationStage.setScene(translationScene); 
        translationStage.opacityProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.doubleValue()==0){ 
                translationStage.setX(-Integer.MAX_VALUE);
                translationStage.setY(-Integer.MAX_VALUE); 
            }
        }); 
        translationStage.setOpacity(0); 
        translationStage.show();
         
        
         

        
        // -----------------------------   END  Tool Word Types Tab Pane    ----------------------------------
        
        // ----------------------------   END  TOOL      ------------------------------ show b
       
        //-----------------------  Tool İcon    --------------------------------------get
        
     
        Button favoriteButtonTool = new Button(); 
        favoriteButtonTool.setId("favoriteButtonTool"); 
        
        Button speechButtonTool = new Button(); 
        speechButtonTool.setId("speechButtonTool"); 
       
        
        Button translationButtonTool = new Button(); 
        translationButtonTool.setId("translationButtonTool");
        
        translationButtonTool.setOnMouseClicked((event) -> { 
            shortcutPopupIconEnteredState=false; 
            if(clipboardText!=null){
                clipboardText=clipboardText.trim();
                if(!clipboardText.isEmpty()){
                    showTool(shortcutPopupIcon.getX()-2, shortcutPopupIcon.getY()-2,clipboardText);
                    if(clipboardText!=null)
                        translatedText=clipboardText;
                }
                
            }
                
        }); 
        
        HBox shortcutPopupIconRoot = new HBox(); 
        shortcutPopupIconRoot.setId("shortcutPopupIconRoot");
        shortcutPopupIconRoot.setSpacing(1);
        Scene shortcutPopupIconScene=new Scene(shortcutPopupIconRoot );
        
        shortcutPopupIconRoot.getChildren().addAll(translationButtonTool ,speechButtonTool ,favoriteButtonTool);
        shortcutPopupIconScene.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) { 
                shortcutPopupIconEnteredState=true;
                if(shortcutPopupIcon.getOpacity()!=0){
                    shortcutPopupIcon.setWidth(64);  
                    shortcutPopupIcon.setOpacity(1);
                    shortcutPopupIconScene.setCursor(Cursor.HAND);
                } 
            }
        });
        
        shortcutPopupIconScene.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                shortcutPopupIconEnteredState=false;
                if(shortcutPopupIcon.getOpacity()!=0){
                    shortcutPopupIcon.setWidth(22);
                    shortcutPopupIcon.setOpacity(0.7);
                }
            }
        });
        
        shortcutPopupIcon = new Stage();
        shortcutPopupIcon.initOwner(stg);
        shortcutPopupIcon.initStyle(StageStyle.TRANSPARENT);  
        shortcutPopupIcon.setAlwaysOnTop(true); 
        shortcutPopupIcon.setWidth(22); 
        shortcutPopupIcon.setHeight(22); 
        shortcutPopupIcon.setScene(shortcutPopupIconScene);
        shortcutPopupIcon.opacityProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.doubleValue()==0){ 
                shortcutPopupIcon.setX(-Integer.MAX_VALUE);
                shortcutPopupIcon.setY(-Integer.MAX_VALUE); 
            }
        });  
        shortcutPopupIcon.setOpacity(0); 
        shortcutPopupIcon.show();
        
        
        
        //-------------------------------     END  Tool İcon   -----------------------------
       
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());    // OS event listener lof off
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();  // OS event listener init
        }
        catch (NativeHookException ex) {
            System.out.println("There was a problem registering the native hook.");
            System.out.println(ex.getMessage()); 
            System.exit(1);
        }
           
        nativeKeyListener=new NativeKeyAdapter(){  
            @Override
            public void nativeKeyPressed(NativeKeyEvent nke) {  
                if(NativeKeyEvent.getModifiersText(nke.getModifiers()).contains("Ctrl")&&nke.getKeyCode()==NativeKeyEvent.VC_C ){
                   
                    if(!robotKeyEventState){
                        //globalEventConsume(nke);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        SystemCopyData=  clip.getClipboardContents();
                        //System.out.println("-> "+robotKeyEventState); 
                    } 
                    robotKeyEventState=false;
                }     
                else if( NativeKeyEvent.getModifiersText(nke.getModifiers()).contains("Ctrl")&&nke.getKeyCode()==NativeKeyEvent.VC_Q){
                    globalEventConsume(nke);
                    
                   
                    Platform.runLater(() -> {
                        if(shortcutPopupIcon.getOpacity()==0)
                            processClipboard();  
                        Point mousePos = MouseInfo.getPointerInfo().getLocation();
                        
                        showTool(mousePos.getX(),mousePos.getY(),clipboardText); 
                        if(clipboardText!=null)
                            translatedText=clipboardText;
                    });
                }
                else if(nke.getKeyCode()==NativeKeyEvent.VC_ESCAPE){  
                    Platform.runLater(() -> {
                        if(shortcutPopupIcon.getOpacity()>0)
                            shortcutPopupIcon.setOpacity(0); 
                        else{
                            translationStage.setOpacity(0);
                            favoriteDialog.hide();
                        }
                             
                    }); 
                } 
            } 
        };
        
        nativeMouseListener=new NativeMouseInputAdapter () {  //  Os mouse listener 
            Timer timer;  
            int clickCounter=0; 
            int process=0;  
            int firstPosX=0;
            int firstPosY=0;
            int selectingSensitive=2; 
            
            @Override
            public void nativeMousePressed(NativeMouseEvent nme) {  
                if(nme.getButton()==nme.BUTTON1&&!shortcutPopupIconEnteredState ){
                    
                    firstPosX=nme.getX();  
                    firstPosY=nme.getY();
                   
                }  
            }
            
            @Override  
            public void nativeMouseReleased(NativeMouseEvent nme) {  
                Platform.runLater(() -> {
                    if(nme.getButton()==nme.BUTTON1&&!shortcutPopupIconEnteredState){  
                        if(Math.abs(nme.getX()-firstPosX)> selectingSensitive||Math.abs(nme.getY()-firstPosY)> selectingSensitive){

                            if( process==0){ 
                                process=1;  
                                clickCounter=0;
                                
                                processClipboard();

                                Platform.runLater(() -> {
                                    showShorcutPopupIcon(nme.getX() ,nme.getY() );
                                });
                                process=0;
                            }
                        }
                        else{

                            if(timer!=null){
                                timer.cancel();  
                            } 

                            clickCounter+=1; 
                            timer= new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {  
                                    if(clickCounter>1) {
                                        Platform.runLater(() -> {
                                            processClipboard();
                                            showShorcutPopupIcon(nme.getX() ,nme.getY() ); 
                                        }); 
                                    }
                                    clickCounter=0; 
                                }
                            },300);  
                        }
                    } 
                });
            };   
        };
        
        nativeMouseMotionListener=new NativeMouseMotionAdapter(){
            
            @Override
            public void nativeMouseMoved(NativeMouseEvent nme) {
                if(translationStage.getOpacity()==0){
                    Platform.runLater(() -> { 
                        progressStage.setX(nme.getX()+12);
                        progressStage.setY(nme.getY()+12);
                    }); 
                }
            } 
            
            @Override
            public void nativeMouseDragged(NativeMouseEvent nme) {
                
                if(translationStage.getOpacity()==0){
                    Platform.runLater(() -> {
                       
                        progressStage.setX(nme.getX()+12);
                        progressStage.setY(nme.getY()+12);
                    }); 
                }
            }   
        };
        
        SystemCopyData= clip.getClipboardContents(); 
        GlobalScreen.setEventDispatcher(new DispatchService()  );
        GlobalScreen.addNativeMouseListener(nativeMouseListener); 
        GlobalScreen.addNativeMouseMotionListener(nativeMouseMotionListener);
        GlobalScreen.addNativeKeyListener(nativeKeyListener);  
        
    } 
 
    private void globalEventConsume(NativeInputEvent event){
        try {
                   Field f = NativeInputEvent.class.getDeclaredField("reserved");
                   f.setAccessible(true);
                   f.setShort(event, (short) 0x01); 
//                   System.out.print("[ OK ]\n");
               }
               catch (Exception ex) {
//                       System.out.print("[ !! ]\n");
                       ex.printStackTrace();
        }
    }
    
    public void processClipboard(){  
        clip.setClipboardContents(""); 
        clipboardText=getSelectedText();  
        
        if(clipboardText!=null){ 
            
            clipboardText=clipboardText.trim();
            
            if(showNewlineState==true)
                clipboardText=clipboardText.replaceAll("\n"," " );
            if(clipboardText.isEmpty())
                clipboardText=null;
             
        } 
        clip.setClipboardContents(SystemCopyData);  
    }  
    
    public String getSelectedText(){   
        robotKeyEventState=true;
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress (KeyEvent.VK_C); 
        robot.keyRelease(KeyEvent.VK_C); 
        robot.keyRelease(KeyEvent.VK_CONTROL); 
        robot.delay(250); 
        return clip.getClipboardContents();
    }  
    
    public void showShorcutPopupIcon(int posX,int posY){ 
        if (clipboardText!=null){  
            if(mainTranslationTA.isFocused()){
                translationStage.setAlwaysOnTop(false);
            }
            else if(!translatorPopupEnteredState) {
                    translationStage.setOpacity(0);
                    favoriteDialog.hide();
            }    

            shortcutPopupIcon.setX(posX+12);
            shortcutPopupIcon.setY(posY+12); 
            shortcutPopupIcon.setOpacity(0.7); 
            shortcutPopupIcon.setHeight(22); 
            shortcutPopupIcon.setWidth(22);
             
          //shortcutPopupIcon.toFront();
          // System.out.println(clipboardText );//  show 
        } 
    } 
    
    public void processTool(double posX,double posY,String txt ){
          
        translation=translator.translate(txt,fromLang,toLang);   //translate function get halka açık  
        
        Object o;
        String translatedText="";
        if(translation.isSentence()){
            o=_mainSentenceDal.getDetailBySentence(txt); 
        } 
        else{
            o=_mainWordDal.getDetailByWord(txt); 
        }
            
        if(o!=null){  
            if(o instanceof MainSentence){
                MainSentence ms=(MainSentence)o; System.out.println("1");
                translatedText=ms.getTargetSentence().getSentence(); System.out.println("2");
            } 
            else{
                MainWord mw=(MainWord)o;
                translatedText=mw.getTargetWord().getWord();
            }
                
            favoriteDialog.setUserData(o);  
            CheckBox cb=(CheckBox)titleBarHBox.getChildren().get(5);  
            ChangeListener<Boolean> cl=(ChangeListener)cb.getUserData(); 
            cb.selectedProperty().removeListener(cl); 
            cb.setSelected(true); 
            cb.selectedProperty().addListener(cl);
        }
        else{
            translatedText=translation.getTranslation(); 
        }
        
        mainTranslationTA.setText(translatedText);
        
        textComputer.setLineSpacing(0);
        textComputer.setWrappingWidth(0); 
        textComputer.setText(translatedText);
        textComputer.setFont(new Font("Consolas", 13));  
        if(textComputer.getLayoutBounds().getWidth()>571)
            textComputer.setWrappingWidth(571);  
        textComputer.setLineSpacing(6);
        mainLayoutVBox.setPrefWidth(textComputer.getLayoutBounds().getWidth()+45);  
        int rows= (int) Math.ceil(textComputer.getLayoutBounds().getHeight()/13);  
        mainTranslationTA.setPrefHeight(textComputer.getLayoutBounds().getHeight()+10); 

        if(!translation.isSentence()){
            WordTypesTabPane.getTabs().forEach((tab) -> { 
                tab.setDisable(true);
            });

            if(translation.getConjunctions()!=null){ 
                WordTypesTabPane.getSelectionModel().select(5); 
                WordTypesTabPane.getTabs().get(5).setDisable(false);
                TableView tv=(TableView)WordTypesTabPane.getTabs().get(5).getContent();  
                tv.setItems(FXCollections.observableArrayList(translation.getConjunctions()));
            } 
            if(translation.getPrepositions()!=null){ 
                WordTypesTabPane.getSelectionModel().select(4);
                WordTypesTabPane.getTabs().get(4).setDisable(false);
                TableView tv=(TableView)WordTypesTabPane.getTabs().get(4).getContent();  
                tv.setItems(FXCollections.observableArrayList(translation.getPrepositions()));
            }
            if(translation.getAdverbs()!=null){ 
                WordTypesTabPane.getSelectionModel().select(3);
                WordTypesTabPane.getTabs().get(3).setDisable(false);
                TableView tv=(TableView)WordTypesTabPane.getTabs().get(3).getContent();  
                tv.setItems(FXCollections.observableArrayList(translation.getAdverbs()));
            }
            if(translation.getAdjectives()!=null){ 
                WordTypesTabPane.getSelectionModel().select(2);  
                WordTypesTabPane.getTabs().get(2).setDisable(false);
                TableView tv=(TableView)WordTypesTabPane.getTabs().get(2).getContent();     
                tv.setItems(FXCollections.observableArrayList(translation.getAdjectives()));
            }
            if(translation.getNoun()!=null){  
                 WordTypesTabPane.getSelectionModel().select(1);
                WordTypesTabPane.getTabs().get(1).setDisable(false);
                TableView tv=(TableView)WordTypesTabPane.getTabs().get(1).getContent();  
                tv.setItems(FXCollections.observableArrayList(translation.getNoun()));
            } 
            if(translation.getVerbs()!=null){ 
                WordTypesTabPane.getSelectionModel().select(0);
                WordTypesTabPane.getTabs().get(0).setDisable(false); 
                TableView tv=(TableView)WordTypesTabPane.getTabs().get(0).getContent();   
                tv.setItems(FXCollections.observableArrayList(translation.getVerbs()));  
            } 
            bigHeight=0;
            totalWidth=0;
            double bigContentWidth=0;
            double bigOtherWordsWidth=0; 
            double contentWidth=0;
            double otherWordsWidth=0; 
            double height=0;

            textComputer.setWrappingWidth(0);
            textComputer.setFont(new Font("Arial", 12)); 
            for(Tab tab : WordTypesTabPane.getTabs() ){
                if(!tab.isDisable()){
                    TableView tv=(TableView)tab.getContent();
                    for(Object word : tv.getItems()){
                        textComputer.setText(((TranslationWord)word).getContent());
                        contentWidth=textComputer.getLayoutBounds().getWidth();
                        textComputer.setText(((TranslationWord)word).getTranslationsTheWord().toString());
                        otherWordsWidth=textComputer.getLayoutBounds().getWidth(); 
                        if(contentWidth>bigContentWidth)
                            bigContentWidth=contentWidth;
                        if(otherWordsWidth>bigOtherWordsWidth){
                            bigOtherWordsWidth=otherWordsWidth;
                        }
                    }
                    height=tv.getItems().size()*tv.getFixedCellSize();
                    if(bigHeight<height){
                        bigHeight=height;
                    }
                }
            }
            bigContentWidth+=8;
            bigOtherWordsWidth+=8;
            totalWidth=bigContentWidth+bigOtherWordsWidth;
            if(totalWidth<WordTypesTabPane.getMinWidth()){
                bigContentWidth=(WordTypesTabPane.getMinWidth()*(bigContentWidth/totalWidth) );
                bigOtherWordsWidth=(WordTypesTabPane.getMinWidth()*(bigOtherWordsWidth/totalWidth) ); 
                totalWidth=WordTypesTabPane.getMinWidth();
            } 

            for(Tab tab : WordTypesTabPane.getTabs() ){
                if(!tab.isDisable()){
                    TableView tv=(TableView)tab.getContent();  
                    TableColumn< TranslationWord,Text> t1=(TableColumn) tv.getColumns().get(0); 
                    TableColumn< TranslationWord,Text> t2=(TableColumn) tv.getColumns().get(1); 
                    t1.setPrefWidth(bigContentWidth );
                    t2.setPrefWidth(bigOtherWordsWidth );  
                    tv.refresh();
                } 
            }
            //System.out.println( bigHeight+  " "+bigContentWidth + " " +bigOtherWordsWidth +" -- " );   
        } 
        
      
            
            
    }
    
    void showTool(double posX,double posY,String txt){  
         
        shortcutPopupIcon.setOpacity(0);
        translationStage.setAlwaysOnTop(true); 
        translationStage.setOpacity(0);
        favoriteDialog.setUserData(null);
        favoriteDialog.hide();
         
        CheckBox cb=(CheckBox)titleBarHBox.getChildren().get(5);
        ChangeListener<Boolean> cl=(ChangeListener)cb.getUserData(); 
        cb.selectedProperty().removeListener(cl); 
        cb.setSelected(false); 
        cb.selectedProperty().addListener(cl);
        
         
       
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        progressStage.setX(mousePos.getX()+12);
        progressStage.setY(mousePos.getY()+12); 
        progressStage.setOpacity(1); 
        
        if(WordTypesTabPane.getParent()!=null){  
            mainLayoutVBox.getChildren().remove(2);  
        }  
 
        executorService.execute(processToolTask(posX+12, posY+12,txt)); 
    }
    
    private Task<Boolean> processToolTask(double posx,double posy,String txt) {
        return new Task<Boolean>() {
            double posX;
            double posY;
            
            @Override
            protected Boolean call() throws Exception { 
                this.posX=posx;
                this.posY=posy;  
                processTool( posX+12, posY+12,txt); 
                return true;
            }

            @Override
            protected void succeeded() {
                if(txt!=null){
                     
                    if(translation!=null&&!translation.isSentence()){
                        mainLayoutVBox.getChildren().add(WordTypesTabPane);
                        mainLayoutVBox.setPrefWidth(totalWidth+20);
                        WordTypesTabPane.setPrefHeight(bigHeight+33);//add noun so length update name show b
                    }

                    translationStage.sizeToScene();
                    translationStage.setOpacity(0.91);
                    screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
                    if(screenSize.width<translationStage.getWidth()+ posX)
                        posX=screenSize.width-translationStage.getWidth();
                    if(screenSize.height<translationStage.getHeight()+ posY)
                        posY=screenSize.height-translationStage.getHeight();
                    translationStage.setX( posX);
                    translationStage.setY( posY); 
                    progressStage.setOpacity(0); 
                    //translationStage.setAlwaysOnTop(false);
                }
                else{
                    System.out.println("başarısız");
                    progressStage.setOpacity(0); 
                }
            }

            @Override
            protected void failed( ) {
                System.out.println("failed -başarısız");
                progressStage.setOpacity(0); 
            }  
        };
    }  
    
    private void createWordPool(){
        
        wordPoolStage=new Stage(StageStyle. DECORATED); 
        wordPoolStage.setTitle("Word Pool");  
        wordPoolStage.getIcons().add( new Image("/Icon/poolIcon.jpg")); 
        
        TreeTableView<Object> treeTableView = new TreeTableView<>();
          //myPoolStage.setAlwaysOnTop(true);
        
//        Button backBtn=new Button("←");
//        backBtn.setDisable(true);
//        backBtn.setCancelButton(true);
//        backBtn.setFont(Font.font("verdana",FontWeight.BOLD, 14));
//        backBtn.setPrefSize(30, 10); 
        Label searchL=new Label("Search :");
        searchL.setPadding(Insets.EMPTY);
        searchL.setAlignment(Pos.CENTER);
        searchL.setPrefHeight(25); 
        searchL.setFont(Font.font("verdana",FontWeight.BOLD, FontPosture.ITALIC, 12));
        TextField searchTF=new TextField(); 
        searchTF.prefWidthProperty().bind(wordPoolStage.widthProperty().subtract(200));
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()){
                ArrayList<MainWord> mw=_mainWordDal.getDetail();
                putMainWordsToTreeTableView(mw);
            }
            else{
                ArrayList<MainWord> mw=_mainWordDal.getDetailIncludingInWord(newValue);
                putMainWordsToTreeTableView(mw); 
            } 
           
            treeTableView.refresh(); 
        });
        
        HBox firstLineHB=new HBox( searchL,searchTF);
        firstLineHB.setPrefHeight(35);
        firstLineHB.setPadding(new Insets(0,0,0, 10));
        firstLineHB.setAlignment(Pos.CENTER_LEFT);
        firstLineHB.setSpacing(10);
        
        VBox rootVB=new VBox(); 
        rootVB.setMinSize(300, 500); 
        
        TreeItem rootTI=new TreeItem( );
        
        VBox.setVgrow(treeTableView, Priority.ALWAYS);   
        treeTableView.setShowRoot(false);
        treeTableView.setRoot(rootTI);
        treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY); 
        
        TreeTableColumn<Object, String> treeTableColumn1 = new TreeTableColumn<>("No");
        TreeTableColumn<Object, String> treeTableColumn2 = new TreeTableColumn<>("Main Word");
        TreeTableColumn<Object, String> treeTableColumn3 = new TreeTableColumn<>("Target Word"); 
        TreeTableColumn<Object, String> treeTableColumn4 = new TreeTableColumn<>("Degree");
        TreeTableColumn<Object, String> treeTableColumn5 = new TreeTableColumn<>("Date");
        TreeTableColumn<Object, Object> treeTableColumn6 = new TreeTableColumn<>("Sentences");
 
        
        
        
        treeTableView.setId("treeTableView"); 
 
//        rootTI. expandedProperty().addListener((observable) -> {
//            System.out.println(" expanded "+observable.toString());
//        }); 
        
        treeTableColumn1.setMinWidth(50);
        treeTableColumn1.setMaxWidth(100);
        treeTableColumn1.setCellValueFactory((cellData) -> { 
            TreeItem<Object> rowItem = cellData.getValue(); 
            
            if (rowItem != null && (rowItem.getValue() instanceof MainWord)){
                return new ReadOnlyObjectWrapper< >(1+rootTI.getChildren().indexOf(cellData.getValue())+""); 
            }  
            return new ReadOnlyObjectWrapper< >("");  
        });
        
        treeTableColumn2.setMinWidth(100);
        treeTableColumn2.setMaxWidth(200);
        treeTableColumn2.setCellFactory(new Callback<TreeTableColumn<Object, String>, TreeTableCell<Object, String>>() {
        @Override
            public TreeTableCell<Object, String> call(TreeTableColumn<Object, String> param) {
                return new TreeTableCell<Object, String>() { 
                     
                    private Text text;
                    @Override
                    public void updateItem(String item, boolean empty) { 
                        super.updateItem(item, empty);
                      
                        if (!isEmpty()) {
                            text = new Text(item);
                            text.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                            
                            if(item.contains("○")){ 
                                text.setFill(Color.BLUEVIOLET); 
                                text.setTextAlignment(TextAlignment.RIGHT);  
                                 text.setFont(Font.font("verdana", FontPosture.ITALIC, 12)); 
                                
                            } 
                            else{ 
                                setAlignment(Pos.CENTER);
                                text.setTextAlignment(TextAlignment.LEFT); 
                                text.setFont(Font.font("verdana", FontWeight.BOLD, 13)); 
                                text.setFill(Color.color(0.2, 0.2, 0.6));
                            } 
                            setGraphic(text); 
                        }
                        else{  
                            setGraphic(null); 
                        } 
                    }  
                };
            }
        });
        treeTableColumn2.setCellValueFactory((cellData) -> { 
            
            String word="";
            TreeItem<Object> rowItem = cellData.getValue(); 
            if (rowItem != null && (rowItem.getValue() instanceof MainWord)){
                MainWord mw=(MainWord)rowItem.getValue(); 
                word=mw.getWord();  
               //cellData.getTreeTableColumn().setId("bold");
            }  
            else if (rowItem != null && (rowItem.getValue() instanceof String)){
                String tw=(String)rowItem.getValue(); 
                if(tw.charAt(0)=='1'){  // Verb number is 1 
                    return new SimpleStringProperty("○ Verb :");
                }
                if(tw.charAt(0)=='2'){  // Noun number is 2 
                    return new SimpleStringProperty("○ Noun :");
                }
                if(tw.charAt(0)=='3'){  // Adjective number is 3 
                    return new SimpleStringProperty("○ Adjective :");
                }
                if(tw.charAt(0)=='4'){  // Adverb number is 4 
                    return new SimpleStringProperty("○ Adverb :");
                }
                if(tw.charAt(0)=='5'){  // Preposition number is 5 
                    return new SimpleStringProperty("○ Preposition :");
                }
                if(tw.charAt(0)=='6'){  // Conjunction number is 6 
                    return new SimpleStringProperty("○ Conjunction :");
                } 
            }   
            return new SimpleStringProperty(word); 
            
        });
        
        treeTableColumn3.getStyleClass().add("column3");
        treeTableColumn3.setMinWidth(500); 
        treeTableColumn3.setCellFactory(new Callback<TreeTableColumn<Object, String>, TreeTableCell<Object, String>>() {
            @Override
            public TreeTableCell<Object, String> call(TreeTableColumn<Object, String> param) {
                return new TreeTableCell<Object,String>(){
                    private Text text;
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        
                        super.updateItem(item, empty);  
                        if (!isEmpty()) {
                            text = new Text(item); 
                            text.setTextAlignment(TextAlignment.JUSTIFY); 
                            text.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                            text.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.ITALIC, 13)); 
                            text.setFill(Color.color(0.1, 0.1, 0.2));
                            setGraphic(text);   
                        }
                        else{ 
                            setGraphic(null); 
                        }  
                    } 
                };
            }
        });        
        treeTableColumn3.setCellValueFactory((cellData) -> { 
            String targetWord="";
            TreeItem<Object> rowItem = cellData.getValue(); 
             
            if (rowItem != null && (rowItem.getValue() instanceof MainWord)){
                MainWord mw=(MainWord)rowItem.getValue(); 
                if(mw.getTargetWord()!=null){
                    targetWord=mw.getTargetWord().getWord();
                }  
            }
            else if (rowItem != null && (rowItem.getValue() instanceof String)){
                String tw=(String)rowItem.getValue(); 
                if(tw.charAt(0)=='1'){  // Verb number is 1 
                    String verbs=tw.substring(2, tw.length()-2);
                    return new SimpleStringProperty(verbs);
                }
                if(tw.charAt(0)=='2'){  // Noun number is 2 
                    String nouns=tw.substring(2, tw.length()-2);
                    return new SimpleStringProperty(nouns);
                }
                if(tw.charAt(0)=='3'){  // Adjective number is 3 
                    String adjectives=tw.substring(2, tw.length()-2);
                    return new SimpleStringProperty(adjectives);
                    
                }
                if(tw.charAt(0)=='4'){  // Adverb number is 4 
                    String adverbs=tw.substring(2, tw.length()-2);
                    return new SimpleStringProperty(adverbs);
                }
                if(tw.charAt(0)=='5'){  // Preposition number is 5 
                    String preposition=tw.substring(2, tw.length()-2);
                    return new SimpleStringProperty(preposition);
                }
                if(tw.charAt(0)=='6'){  // Conjunction number is 6 
                    String conjunction=tw.substring(2, tw.length()-2);
                    return new SimpleStringProperty(conjunction);
                } 
            }  
            return new SimpleStringProperty(targetWord);
        });
        
        treeTableColumn4.getStyleClass().add("column4");
        treeTableColumn4.setMinWidth(70);
        treeTableColumn4.setMaxWidth(200); 
        treeTableColumn4.setCellValueFactory((cellData) -> { 
            TreeItem<Object> rowItem = cellData.getValue(); 
            if (rowItem != null && (rowItem.getValue() instanceof MainWord)){
                int degree=((MainWord)rowItem.getValue()).getDegree();
                String star="★";
                for(int i=1;i<degree;i++){
                    star+=" ★"; 
                }
                return new SimpleStringProperty(star);
            }   
            return new SimpleStringProperty(""); 
        });
        
        treeTableColumn5.getStyleClass().add("column5");
        treeTableColumn5.setMinWidth(100);
        treeTableColumn5.setMaxWidth(120);
        treeTableColumn5.setCellValueFactory((cellData) -> { 
            TreeItem<Object> rowItem = cellData.getValue(); 
            if (rowItem != null && (rowItem.getValue() instanceof MainWord)){
                long epoch=((MainWord)rowItem.getValue()).getDate()*1000L; 
                date.setTime(epoch);
                String formattedDate = dateFormat.format(date); 
                return new SimpleStringProperty(""+formattedDate);
            }   
            return new SimpleStringProperty(""); 
        });

        treeTableColumn6.getStyleClass().add("column6");
        treeTableColumn6.setMinWidth(80);
        treeTableColumn6.setMaxWidth(100);
        treeTableColumn6.setCellValueFactory((param) -> {
            Object mainWord=null;
            TreeItem<Object> rowItem = param.getValue(); 
            if (rowItem != null && (rowItem.getValue() instanceof MainWord)){
                mainWord=(MainWord)rowItem.getValue();  
            }  
            return new SimpleObjectProperty(mainWord); 
        } );
        
        treeTableColumn6.setCellFactory( new Callback<TreeTableColumn<Object, Object>, TreeTableCell<Object, Object>>() {
            @Override
            public TreeTableCell<Object, Object> call(TreeTableColumn<Object, Object> param) { 
                TreeTableCell<Object,Object> ttc= new TreeTableCell<Object,Object>(){ 
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); 
                        if(item instanceof MainWord){
                            MainWord mw=(MainWord)item;
                            Button goButton = new Button("Go");
                            goButton.setPrefWidth(50);
                            goButton.setTextFill(Color.DARKSLATEGREY);
                            goButton.setStyle(
                                "-fx-background-radius: 20em; " 
                            ); 
                            goButton.setOnMouseClicked((event) -> { 
                                ArrayList<MainSentence> mss=_mainWordDal.getRelatedSentence(mw.getId()); 
                                
                                Scene scene=wordPoolStage.getScene();
                                StackPane rootSP=(StackPane)scene.getRoot();
                                VBox wordsVB=(VBox)rootSP.getChildren().get(0);
                                VBox sentencesVB=(VBox)rootSP.getChildren().get(1); 
                                
                                mw.setMainSentences(mainSentences);
                                sentencesVB.setUserData(mw);    //for getting id
                                
                                TreeTableView ttv=(TreeTableView)sentencesVB.getChildren().get(1); //çözüm bull
                                putMainSentencesToTreeTableView(mss, ttv);
                                ttv.refresh();
                                
                                sentencesVB.translateXProperty().set(-scene.getWidth());  
                                wordsVB.translateXProperty().set(0);
                                
                                Timeline timeline = new Timeline();
                                KeyValue kv1 = new KeyValue(sentencesVB.translateXProperty(), 0, Interpolator.EASE_IN);
                                KeyFrame kf1 = new KeyFrame(Duration.seconds(0.5), kv1);
                                
                                KeyValue kv2 = new KeyValue(wordsVB.translateXProperty(),scene.getWidth(), Interpolator.EASE_IN);
                                KeyFrame kf2 = new KeyFrame(Duration.seconds(0.5), kv2);
                                timeline.getKeyFrames().addAll(kf1,kf2);
                                timeline.setOnFinished(t -> {
                                    wordsVB.setVisible(false);
                                });
                                sentencesVB.setVisible(true);
                                timeline.play();  
                            });
                            setGraphic(goButton);
                        }   
                        else
                            setGraphic(null);
                    }  
                }; 
                ttc.setPadding(new Insets(0,0,0,0));
               

                return ttc;
            }
        });



        treeTableView.getColumns().add(treeTableColumn1);
        treeTableView.getColumns().add(treeTableColumn2);
        treeTableView.getColumns().add(treeTableColumn3);
        treeTableView.getColumns().add(treeTableColumn4);
        treeTableView.getColumns().add(treeTableColumn5);
        treeTableView.getColumns().add(treeTableColumn6); 
        
       
        
//        TreeTableView<Object> t = new TreeTableView<Object>();
//        t.setId("treeTableView");
//        t.setShowRoot(false);
//        t.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
//        
//        TreeTableColumn<Object, String> c1 = new TreeTableColumn<>("No");
//        TreeTableColumn<Object, String> c2 = new TreeTableColumn<>("Main Word");
//        TreeTableColumn<Object, String> c3 = new TreeTableColumn<>("Target Word"); 
//        TreeTableColumn<Object, String> c4 = new TreeTableColumn<>("Degree");
//        TreeTableColumn<Object, String> c5 = new TreeTableColumn<>("Date");
//        TreeTableColumn<Object, String> c6 = new TreeTableColumn<>("Sentences");
//        
//        t.getColumns().addAll(c1,c2,c3,c4,c5,c6);
//        
//        TreeItem<Object> ti1=new TreeItem<>();
//        TreeItem<Object> ti2=new TreeItem<>();
//        TreeItem<Object> ti3=new TreeItem<>();
//        TreeItem<Object> ti4=new TreeItem<>();
//        TreeItem<Object> ti5=new TreeItem<>();
//        TreeItem<Object> ti6=new TreeItem<>();
//        TreeItem<Object> rootti=new TreeItem<>();
//        rootti.getChildren().addAll(ti1,ti2,ti3,ti4,ti5,ti6); 
//        t.setRoot(rootti);
        
        
        
        VBox sentencesVB=createSentencesReleatedWordVBox(); 
        StackPane rootSP=new StackPane(rootVB,sentencesVB);
        Scene myPoolScene=new Scene(rootSP);
        sentencesVB.setVisible(false);
        rootVB.getChildren().addAll(firstLineHB,treeTableView);  
        wordPoolStage.setScene(myPoolScene); 
    }
    
    private VBox createSentencesReleatedWordVBox(){ 
        TreeTableView<Object> treeTableViewSentencePool = new TreeTableView<>(); 
        VBox rootVB=new VBox();  
        rootVB.setMinSize(950, 500); 
        
        treeTableViewSentencePool.setRowFactory( (param) -> {  //Bozulmaları önlüyor
            return  new TreeTableRow<Object>(){  
                @Override
                protected void updateItem(Object item, boolean empty) {  
                    super.updateItem(item, empty); 
                    setMinHeight(25);  
                } 
            };
        });    
        
        Label searchL=new Label("Search :");
        searchL.setPadding(Insets.EMPTY);
        searchL.setAlignment(Pos.CENTER);
        searchL.setPrefHeight(25); 
        searchL.setFont(Font.font("verdana",FontWeight.BOLD, FontPosture.ITALIC, 12));
        TextField searchTF=new TextField(); 
        searchTF.prefWidthProperty().bind(wordPoolStage.widthProperty().subtract(200));
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            MainWord mw=(MainWord)rootVB.getUserData();  
            TreeTableView tiw=getTreeItemViewOfSentencePoolStage();
            if(newValue.isEmpty()){ 
                ArrayList<MainSentence> mss=_mainWordDal.getRelatedSentence(mw.getId()); 
                putMainSentencesToTreeTableView(mss,treeTableViewSentencePool);
            }
            else{
                ArrayList<MainSentence> mss=_mainWordDal.getDetailIncludingInRelatedSentence(mw.getId(),newValue);
                putMainSentencesToTreeTableView(mss,treeTableViewSentencePool); 
            }  
            treeTableViewSentencePool.refresh(); 
        });
        
        Button backButton=new Button("◄ Back "); 
        backButton.setAlignment(Pos.CENTER);
        backButton.setPrefHeight(25);  
        backButton.setFont(Font.font("verdana",FontWeight.BOLD,  12));
        
        backButton.setOnMouseClicked((event) -> { 
            Scene scene=wordPoolStage.getScene();
            StackPane rootSP=(StackPane)scene.getRoot();
            VBox wordsVB=(VBox)rootSP.getChildren().get(0);
            VBox sentencesVB=(VBox)rootSP.getChildren().get(1);
             
            
            wordsVB.setVisible(true);
            //sentencesVB.translateXProperty().set(-scene.getWidth());  
            //wordsVB.translateXProperty().set(0);
            
            
            Timeline timeline = new Timeline();
            KeyValue kv1 = new KeyValue(sentencesVB.translateXProperty(), -scene.getWidth(), Interpolator.EASE_IN);
            KeyFrame kf1 = new KeyFrame(Duration.seconds(0.5), kv1);

            KeyValue kv2 = new KeyValue(wordsVB.translateXProperty(),0, Interpolator.EASE_IN);
            KeyFrame kf2 = new KeyFrame(Duration.seconds(0.5), kv2);
            timeline.getKeyFrames().addAll(kf1,kf2);
            timeline.setOnFinished(t -> {
                sentencesVB.setVisible(false);
            });
            
            timeline.play();  
                    
            getTreeItemViewOfSentencePoolStage().setVisible(true);
        });
        Region reg=new Region(); 
        HBox firstLineHB=new HBox( searchL,searchTF,reg,backButton);
        firstLineHB.setPrefHeight(35);
        firstLineHB.setPadding(new Insets(0,0,0, 10));
        firstLineHB.setAlignment(Pos.CENTER_LEFT);
        firstLineHB.setSpacing(10); 
        
            
        VBox.setVgrow(treeTableViewSentencePool, Priority.ALWAYS); 
        //treeTableViewSentencePool.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        treeTableViewSentencePool.setId("treeTableView"); 
        
        
        TreeItem rootTI=new TreeItem( ); 
        TreeTableColumn<Object, String> treeTableColumn1 = new TreeTableColumn<>("No");
        TreeTableColumn<Object, Object> treeTableColumn2 = new TreeTableColumn<>("Sentence");
        TreeTableColumn<Object, String> treeTableColumn3 = new TreeTableColumn<>("Degree");
        TreeTableColumn<Object, String> treeTableColumn4 = new TreeTableColumn<>("Date");
         
        treeTableColumn1.prefWidthProperty().bind(treeTableViewSentencePool.widthProperty().multiply(0.07)); 
        treeTableColumn1.setCellValueFactory((cellData) -> { 
            TreeItem<Object> rowItem = cellData.getValue(); 
            
            if (rowItem != null && (rowItem.getValue() instanceof MainSentence)){
                return new ReadOnlyObjectWrapper<>(1+rootTI.getChildren().indexOf(cellData.getValue())+""); 
            }  
            return new ReadOnlyObjectWrapper<>("");  
        });
         
        //treeTableColumn2.setMinWidth(400);  
        treeTableColumn2.prefWidthProperty().bind(treeTableViewSentencePool.widthProperty().multiply(0.65));
        treeTableColumn2.setCellValueFactory((cellData) -> {  
            Object mainOrTargetSentence=null;
            TreeItem<Object> rowItem = cellData.getValue(); 
            if (rowItem != null && (rowItem.getValue() instanceof MainSentence)){
                mainOrTargetSentence=(MainSentence)rowItem.getValue();  
            }  
            else if (rowItem != null && (rowItem.getValue() instanceof TargetSentence)){
                mainOrTargetSentence=(TargetSentence)rowItem.getValue();  
            }   
            return new SimpleObjectProperty(mainOrTargetSentence);  
        });
        
        treeTableColumn2.setCellFactory((param) -> {
            return new TreeTableCell<Object, Object>(){
                TextFlow tf=new TextFlow(); 
                Text text = new Text();
                @Override
                protected void updateItem(Object item, boolean empty) { 
                    super.updateItem(item, empty);  
                    setGraphic(null);
                    if (!isEmpty()) {   
                        if (item != null && (item instanceof MainSentence)){
                            if(!searchTF.getText().isEmpty()){   
                                String searchedText=searchTF.getText().toLowerCase(Locale.ENGLISH); 
                                tf.getChildren().clear();
                                
                                MainSentence ms=(MainSentence)item;
                                
                                String orginalSentence=ms.getSentence();
                                String lowerSentence=orginalSentence.toLowerCase(Locale.ENGLISH);
                                tf.setTextAlignment(TextAlignment.JUSTIFY);
                                text.setText(orginalSentence);
                                //text.setLineSpacing(1);
                                text.setTextAlignment(TextAlignment.JUSTIFY);
                                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13)); 
                                text.wrappingWidthProperty().bind(treeTableColumn2.widthProperty().subtract(10) ); 
                                //tf.prefWidthProperty().bind(treeTableColumn2.widthProperty().subtract(5));
                                treeTableColumn2.widthProperty().addListener((observable) -> { 
                                    //System.out.println(treeTableColumn2.getWidth()+" -> "+text.getLayoutBounds().getHeight());  
                                    tf.setPrefHeight(text.getLayoutBounds().getHeight());  
                                }); 
                                tf.setPrefHeight(text.getLayoutBounds().getHeight());
                                 
                                        
                                int tempIndex=0;
                                int index=lowerSentence.indexOf(searchedText,tempIndex);
                                while(index>-1){ 
                                    if(index>0){
                                        String previousString=orginalSentence.substring(0, index);
                                        String highlightedString=orginalSentence.substring(index, index+searchedText.length());

                                        int newStartIndexOfSentence=index+searchedText.length(); 
                                        lowerSentence=lowerSentence.substring(newStartIndexOfSentence);
                                        orginalSentence=orginalSentence.substring(newStartIndexOfSentence);

                                        Text previousText=new Text(previousString); 
                                        Text highlightedText=new Text(highlightedString);
                                        
                                        previousText.setTextAlignment(TextAlignment.JUSTIFY); 
                                        previousText.setFill(Color.color(0.1, 0.1, 0.3));
                                        previousText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13)); 
                                        //previousText.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                                        
                                        //highlightedText.wrappingWidthProperty().bind(param.widthProperty().subtract(5));  
                                        highlightedText.setTextAlignment(TextAlignment.JUSTIFY); 
                                        highlightedText.setFill(Color.color(0.1, 0.7, 0.1));
                                        highlightedText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13)); 

                                        tf.getChildren().addAll(previousText,highlightedText); 
                                    }
                                    else{
                                        String highlightedString=orginalSentence.substring(0, searchedText.length()); 
                                        lowerSentence=lowerSentence.substring(searchedText.length());
                                        orginalSentence=orginalSentence.substring(searchedText.length());
                                        Text highlightedText=new Text(highlightedString);
                                        highlightedText.setTextAlignment(TextAlignment.JUSTIFY); 
                                        highlightedText.setFill(Color.color(0.1, 0.7, 0.1));
                                        highlightedText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));  
                                        //highlightedText.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                                        tf.getChildren().add(highlightedText);
                                    }
                                    index=lowerSentence.indexOf(searchedText); 
                                }
                                if(orginalSentence.length()>0){ 
                                    Text latestText=new Text(orginalSentence);
                                    latestText.setTextAlignment(TextAlignment.JUSTIFY); 
                                    latestText.setFill(Color.color(0.1, 0.1, 0.3));
                                    latestText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13)); 
                                    //latestText.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                                    tf.getChildren().add(latestText);
                                }
                                if(tf.getChildren().size()>0){   
                                    setGraphic(tf);  
                                } 
                            }
                            else{
                               
                                MainSentence ms=(MainSentence)item; 
                                text.setText(ms.getSentence());   
                                text.setFill(Color.color(0.1, 0.1, 0.3));
                                text.setTextAlignment(TextAlignment.JUSTIFY); 
                                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
                                text.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                                setGraphic(text);  
                            } 
                        }  
                        else if (item != null && (item instanceof TargetSentence)){
                            text.setTextAlignment(TextAlignment.JUSTIFY); 
                            text.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.ITALIC, 13));
                            TargetSentence ts=(TargetSentence)item;   
                            text.setText(ts.getSentence());   
                            text.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                            setGraphic(text);    
                        } 
                    }  
                }  
            }; 
        });
        
        
        treeTableColumn3.getStyleClass().add("column4");
        treeTableColumn3.prefWidthProperty().bind(treeTableViewSentencePool.widthProperty().multiply(0.10));
        treeTableColumn3.setCellValueFactory((cellData) -> { 
            TreeItem<Object> rowItem = cellData.getValue(); 
            if (rowItem != null && (rowItem.getValue() instanceof MainSentence)){
                int degree=((MainSentence)rowItem.getValue()).getDegree();
                String star="★";
                for(int i=1;i<degree;i++){
                    star+=" ★"; 
                }
                return new SimpleStringProperty(star);
            }   
            return new SimpleStringProperty(""); 
        });
    
        treeTableColumn4.getStyleClass().add("column5");
        treeTableColumn4.prefWidthProperty().bind(treeTableViewSentencePool.widthProperty().multiply(0.18));
        treeTableColumn4.setCellValueFactory((cellData) -> { 
            TreeItem<Object> rowItem = cellData.getValue(); 
            if (rowItem != null && (rowItem.getValue() instanceof MainSentence)){
                long epoch=((MainSentence)rowItem.getValue()).getDate()*1000L; 
                date.setTime(epoch);
                String formattedDate = dateFormat.format(date); 
                return new SimpleStringProperty(""+formattedDate);
            }   
            return new SimpleStringProperty(""); 
        });
        
        treeTableViewSentencePool.getColumns().add(treeTableColumn1);
        treeTableViewSentencePool.getColumns().add(treeTableColumn2);
        treeTableViewSentencePool.getColumns().add(treeTableColumn3);
        treeTableViewSentencePool.getColumns().add(treeTableColumn4); 
        treeTableViewSentencePool.setShowRoot(false);
        treeTableViewSentencePool.setRoot(rootTI);
        
        rootVB.getChildren().addAll(firstLineHB,treeTableViewSentencePool);  
        return rootVB; 
    } 
    
    private void createSentencePool(){
        
        sentencePoolStage=new Stage(StageStyle. DECORATED);
        sentencePoolStage.setTitle("Sentece Pool");  
        sentencePoolStage.getIcons().add( new Image("/Icon/poolIcon.jpg"));
        TreeTableView<Object> treeTableViewSentencePool = new TreeTableView<>(); 
        
        treeTableViewSentencePool.setRowFactory( (param) -> {  //Bozulmaları önlüyor
            return  new TreeTableRow<Object>(){  
                @Override
                protected void updateItem(Object item, boolean empty) {  
                    super.updateItem(item, empty); 
                    setMinHeight(25);  
                } 
            };
        });   
        
        
        Label searchL=new Label("Search :");
        searchL.setPadding(Insets.EMPTY);
        searchL.setAlignment(Pos.CENTER);
        searchL.setPrefHeight(25); 
        searchL.setFont(Font.font("verdana",FontWeight.BOLD, FontPosture.ITALIC, 12));
        TextField searchTF=new TextField(); 
        searchTF.prefWidthProperty().bind(sentencePoolStage.widthProperty().subtract(200));
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> { 
            if(newValue.isEmpty()){
                ArrayList<MainSentence> mss=_mainSentenceDal.getDetail();
                putMainSentencesToTreeTableView(mss,treeTableViewSentencePool);
            }
            else{
                ArrayList<MainSentence> mss=_mainSentenceDal.getDetailIncludingInSentence(newValue);
                putMainSentencesToTreeTableView(mss,treeTableViewSentencePool); 
            }  
            treeTableViewSentencePool.refresh(); 
        });
        
        HBox firstLineHB=new HBox( searchL,searchTF);
        firstLineHB.setPrefHeight(35);
        firstLineHB.setPadding(new Insets(0,0,0, 10));
        firstLineHB.setAlignment(Pos.CENTER_LEFT);
        firstLineHB.setSpacing(10);
        
        VBox rootVB=new VBox();  
        rootVB.setMinSize(950, 500); 
        
            
        VBox.setVgrow(treeTableViewSentencePool, Priority.ALWAYS); 
        //treeTableViewSentencePool.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        treeTableViewSentencePool.setId("treeTableView"); 
        
        
        TreeItem rootTI=new TreeItem( ); 
        TreeTableColumn<Object, String> treeTableColumn1 = new TreeTableColumn<>("No");
        TreeTableColumn<Object, Object> treeTableColumn2 = new TreeTableColumn<>("Sentence");
        TreeTableColumn<Object, String> treeTableColumn3 = new TreeTableColumn<>("Degree");
        TreeTableColumn<Object, String> treeTableColumn4 = new TreeTableColumn<>("Date");
         
        treeTableColumn1.prefWidthProperty().bind(treeTableViewSentencePool.widthProperty().multiply(0.07)); 
        treeTableColumn1.setCellValueFactory((cellData) -> { 
            TreeItem<Object> rowItem = cellData.getValue(); 
            
            if (rowItem != null && (rowItem.getValue() instanceof MainSentence)){
                return new ReadOnlyObjectWrapper<>(1+rootTI.getChildren().indexOf(cellData.getValue())+""); 
            }  
            return new ReadOnlyObjectWrapper<>("");  
        });
         
        //treeTableColumn2.setMinWidth(400);  
        treeTableColumn2.prefWidthProperty().bind(treeTableViewSentencePool.widthProperty().multiply(0.65));
        treeTableColumn2.setCellValueFactory((cellData) -> {  
            Object mainOrTargetSentence=null;
            TreeItem<Object> rowItem = cellData.getValue(); 
            if (rowItem != null && (rowItem.getValue() instanceof MainSentence)){
                mainOrTargetSentence=(MainSentence)rowItem.getValue();  
            }  
            else if (rowItem != null && (rowItem.getValue() instanceof TargetSentence)){
                mainOrTargetSentence=(TargetSentence)rowItem.getValue();  
            }   
            return new SimpleObjectProperty(mainOrTargetSentence);  
        });
        
        treeTableColumn2.setCellFactory((param) -> {
            return new TreeTableCell<Object, Object>(){
                TextFlow tf=new TextFlow(); 
                Text text = new Text();
                @Override
                protected void updateItem(Object item, boolean empty) { 
                    super.updateItem(item, empty);  
                    setGraphic(null);
                    if (!isEmpty()) {  
                        if (item != null && (item instanceof MainSentence)){
                            if(!searchTF.getText().isEmpty()){  
                                String searchedText=searchTF.getText().toLowerCase(Locale.ENGLISH); 
                                tf.getChildren().clear();
                                
                                MainSentence ms=(MainSentence)item;
                                
                                String orginalSentence=ms.getSentence();
                                String lowerSentence=orginalSentence.toLowerCase(Locale.ENGLISH);
                                tf.setTextAlignment(TextAlignment.JUSTIFY);
                                text.setText(orginalSentence);
                                //text.setLineSpacing(1);
                                text.setTextAlignment(TextAlignment.JUSTIFY);
                                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13)); 
                                text.wrappingWidthProperty().bind(treeTableColumn2.widthProperty().subtract(10) ); 
                                //tf.prefWidthProperty().bind(treeTableColumn2.widthProperty().subtract(5));
                                treeTableColumn2.widthProperty().addListener((observable) -> { 
                                    //System.out.println(treeTableColumn2.getWidth()+" -> "+text.getLayoutBounds().getHeight());  
                                    tf.setPrefHeight(text.getLayoutBounds().getHeight());  
                                }); 
                                tf.setPrefHeight(text.getLayoutBounds().getHeight());
                                 
                                        
                                int tempIndex=0;
                                int index=lowerSentence.indexOf(searchedText,tempIndex);
                                while(index>-1){ 
                                    if(index>0){
                                        String previousString=orginalSentence.substring(0, index);
                                        String highlightedString=orginalSentence.substring(index, index+searchedText.length());

                                        int newStartIndexOfSentence=index+searchedText.length(); 
                                        lowerSentence=lowerSentence.substring(newStartIndexOfSentence);
                                        orginalSentence=orginalSentence.substring(newStartIndexOfSentence);

                                        Text previousText=new Text(previousString); 
                                        Text highlightedText=new Text(highlightedString);
                                        
                                        previousText.setTextAlignment(TextAlignment.JUSTIFY); 
                                        previousText.setFill(Color.color(0.1, 0.1, 0.3));
                                        previousText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13)); 
                                        //previousText.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                                        
                                        //highlightedText.wrappingWidthProperty().bind(param.widthProperty().subtract(5));  
                                        highlightedText.setTextAlignment(TextAlignment.JUSTIFY); 
                                        highlightedText.setFill(Color.color(0.1, 0.7, 0.1));
                                        highlightedText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13)); 

                                        tf.getChildren().addAll(previousText,highlightedText); 
                                    }
                                    else{
                                        String highlightedString=orginalSentence.substring(0, searchedText.length()); 
                                        lowerSentence=lowerSentence.substring(searchedText.length());
                                        orginalSentence=orginalSentence.substring(searchedText.length());
                                        Text highlightedText=new Text(highlightedString);
                                        highlightedText.setTextAlignment(TextAlignment.JUSTIFY); 
                                        highlightedText.setFill(Color.color(0.1, 0.7, 0.1));
                                        highlightedText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));  
                                        //highlightedText.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                                        tf.getChildren().add(highlightedText);
                                    }
                                    index=lowerSentence.indexOf(searchedText); 
                                }
                                if(orginalSentence.length()>0){ 
                                    Text latestText=new Text(orginalSentence);
                                    latestText.setTextAlignment(TextAlignment.JUSTIFY); 
                                    latestText.setFill(Color.color(0.1, 0.1, 0.3));
                                    latestText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13)); 
                                    //latestText.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                                    tf.getChildren().add(latestText);
                                }
                                if(tf.getChildren().size()>0){   
                                    setGraphic(tf);  
                                } 
                            }
                            else{
                               
                                MainSentence ms=(MainSentence)item; 
                                text.setText(ms.getSentence());   
                                text.setFill(Color.color(0.1, 0.1, 0.3));
                                text.setTextAlignment(TextAlignment.JUSTIFY); 
                                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
                                text.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                                setGraphic(text);  
                            } 
                        }  
                        else if (item != null && (item instanceof TargetSentence)){
                            text.setTextAlignment(TextAlignment.JUSTIFY); 
                            text.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.ITALIC, 13));
                            TargetSentence ts=(TargetSentence)item;   
                            text.setText(ts.getSentence());   
                            text.wrappingWidthProperty().bind(param.widthProperty().subtract(5)); 
                            setGraphic(text);    
                        } 
                    }  
                }  
            }; 
        });
        
        
        treeTableColumn3.getStyleClass().add("column4");
        treeTableColumn3.prefWidthProperty().bind(treeTableViewSentencePool.widthProperty().multiply(0.10));
        treeTableColumn3.setCellValueFactory((cellData) -> { 
            TreeItem<Object> rowItem = cellData.getValue(); 
            if (rowItem != null && (rowItem.getValue() instanceof MainSentence)){
                int degree=((MainSentence)rowItem.getValue()).getDegree();
                String star="★";
                for(int i=1;i<degree;i++){
                    star+=" ★"; 
                }
                return new SimpleStringProperty(star);
            }   
            return new SimpleStringProperty(""); 
        });
    
        treeTableColumn4.getStyleClass().add("column5");
        treeTableColumn4.prefWidthProperty().bind(treeTableViewSentencePool.widthProperty().multiply(0.18));
        treeTableColumn4.setCellValueFactory((cellData) -> { 
            TreeItem<Object> rowItem = cellData.getValue(); 
            if (rowItem != null && (rowItem.getValue() instanceof MainSentence)){
                long epoch=((MainSentence)rowItem.getValue()).getDate()*1000L; 
                date.setTime(epoch);
                String formattedDate = dateFormat.format(date); 
                return new SimpleStringProperty(""+formattedDate);
            }   
            return new SimpleStringProperty(""); 
        });
        
        treeTableViewSentencePool.getColumns().add(treeTableColumn1);
        treeTableViewSentencePool.getColumns().add(treeTableColumn2);
        treeTableViewSentencePool.getColumns().add(treeTableColumn3);
        treeTableViewSentencePool.getColumns().add(treeTableColumn4); 
        treeTableViewSentencePool.setShowRoot(false);
        treeTableViewSentencePool.setRoot(rootTI);
        
        rootVB.getChildren().addAll(firstLineHB,treeTableViewSentencePool); 
        
        Scene sentencePoolScene=new Scene(rootVB); 
        
        sentencePoolStage.setScene(sentencePoolScene);  
        
        
//     
//        
//        Text [] ts1=new Text[6];
//        Text [] ts2=new Text[12];
//        
//        for(int i=0;i<6;i++){
//            ts1[i]=new Text("lolo");
//            ts1[i].setFill(Color.color(0.1, 0.1, 0.3));
//            ts1[i].setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
//        }
//
//        for(int i=0;i<12;i++){
//            ts2[i]=new Text("lo");
//            ts2[i].setFill(Color.color(0.1, 0.1, 0.3));
//            ts2[i].setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
//        }       
//        
//        TextFlow tf1=new TextFlow(ts1);
//        TextFlow tf2=new TextFlow(ts2);
//        
//        Text tx=new Text("loolloolloolloolloollool");
//        tx.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
//    
//        
//        Stage stg=new Stage();
//        StackPane sp=new StackPane();
//        Scene sc=new Scene( sp  ); 
//        stg.setScene(sc);
//        stg.setWidth(600);
//        stg.setHeight(600);
//        
//        VBox vb1=new VBox( tf1 );
//        vb1.setStyle("-fx-background-color:  #4cfc4c;");
//        vb1.setLayoutX(0); 
//        vb1.setPrefSize(100, 100);
//        
//        VBox vb2=new VBox( tf2);
//        vb2.setLayoutX(stg.getWidth()); 
//        vb2.setPrefSize(100, 100);
//        vb2.setStyle("-fx-background-color:  #4c684c;");
//        sp.getChildren().addAll(vb1);  
//        stg.show();
//        
//        vb2.translateXProperty().set(-sc.getWidth());
//
//        sp.getChildren().add(vb2);
//
//        Timeline timeline = new Timeline();
//        KeyValue kv = new KeyValue(vb2.translateXProperty(), 0, Interpolator.LINEAR);
//        KeyFrame kf = new KeyFrame(Duration.seconds(5), kv);
//        timeline.getKeyFrames().add(kf);
//        timeline.setOnFinished(t -> {
//            //sp.getChildren().remove(vb1);
//        });
//        timeline.play();  
    }
    
    public Object addWordOrSentence(String mainText,String targetText,int degree, TranslationResponse translationResponse){
        if(!translationResponse.isSentence()){
 
            MainWord mw=new MainWord();
            mainText=mainText.toLowerCase(Locale.ENGLISH);
            String word=Helper.capitalize(mainText,Locale.ENGLISH);
            mw.setWord(word);
            mw.setDate((int) Instant.now().getEpochSecond());
            mw.setDegree(degree);
            long msId=_mainWordDal.add(mw);
            mw.setId(msId);
            
            TargetWord tw=new TargetWord(); 
            targetText=Helper.capitalize(targetText);
            tw.setWord(targetText);
            tw.setMainWordId(msId);
            tw.setDate((int) Instant.now().getEpochSecond()); 
            
            if(translationResponse.getVerbs()!=null){
                String verb="";
                for(TranslationWord transW :translationResponse.getVerbs()) 
                   verb+=Helper.capitalize(transW.getContent())+", "; 
                tw.setVerb(verb);
            }

            if(translationResponse.getNoun()!=null){
                String noun="";
                for(TranslationWord transW :translationResponse.getNoun()) 
                   noun+=Helper.capitalize(transW.getContent())+", "; 
                tw.setNoun(noun);
            }

            if(translationResponse.getAdjectives()!=null){
                String adjective="";
                for(TranslationWord transW :translationResponse.getAdjectives()) 
                   adjective+=Helper.capitalize(transW.getContent())+", "; 
                tw.setAdjective(adjective);
            }

            if(translationResponse.getAdverbs()!=null){
                String adverb="";
                for(TranslationWord transW :translationResponse.getAdverbs()) 
                   adverb+=Helper.capitalize(transW.getContent())+", "; 
                tw.setAdverb(adverb);
            }

            if(translationResponse.getPrepositions()!=null){
                String prepostion="";
                for(TranslationWord transW :translationResponse.getPrepositions()) 
                   prepostion+=Helper.capitalize(transW.getContent())+", "; 
                tw.setPreposition(prepostion);
            }

            if(translationResponse.getConjunctions()!=null){
                String conjunction="";
                for(TranslationWord transW :translationResponse.getConjunctions()) 
                   conjunction+=Helper.capitalize(transW.getContent())+", "; 
                tw.setConjunction(conjunction);
            } 
            
            long twId=_targetWordDal.add(tw);
            tw.setId(twId);
            mw.setTargetWord(tw);
            
            return mw;
        }
        
        else{
            MainSentence ms=new MainSentence();
            ms.setSentence(translatedText);
            ms.setDate((int) Instant.now().getEpochSecond());
            ms.setDegree(5);
            long msId=_mainSentenceDal.add(ms);
            ms.setId(msId);
            
            TargetSentence ts=new TargetSentence();
            ts.setMainSentenceId(msId); 
            ts.setSentence(targetText); 
            ts.setDate((int) Instant.now().getEpochSecond());
            long tsId=_targetSentenceDal.add(ts); 
            ts.setId(tsId);
            
            ms.setTargetSentence(ts);
            
            String [] words=Helper.parseSentence(translatedText);
            for(String word:words){
                long mwId=_mainWordDal.isWordExist(word);
                if(mwId!=-1){
                    _mainWordsAndSentenceDal.add(mwId, msId);
                }
            }  
            
            return ms;
        } 
    }
    
    private void putMainWordsToTreeTableView(ArrayList<MainWord> mainwords){
        StackPane sp=((StackPane)wordPoolStage.getScene().getRoot());
        VBox vb=((VBox)sp.getChildren().get(0));
        TreeTableView tiw=(TreeTableView)vb.getChildren().get(1);
        tiw.getRoot().getChildren().clear();
        for(MainWord mw:mainwords){  
            TreeItem mainTI=new TreeItem(mw); 
            if(mw.getTargetWord().getVerb()!=null){
                TreeItem nodeTI=new TreeItem("1,"+mw.getTargetWord().getVerb());
                mainTI.getChildren().add(nodeTI);
            }
            if(mw.getTargetWord().getNoun()!=null){
                TreeItem nodeTI=new TreeItem("2,"+mw.getTargetWord().getNoun());
                mainTI.getChildren().add(nodeTI);
            }
            if(mw.getTargetWord().getAdjective()!=null){
                TreeItem nodeTI=new TreeItem("3,"+mw.getTargetWord().getAdjective());
                mainTI.getChildren().add(nodeTI);
            }
            if(mw.getTargetWord().getAdverb()!=null){
                TreeItem nodeTI=new TreeItem("4,"+mw.getTargetWord().getAdverb());
                mainTI.getChildren().add(nodeTI);
            }
            if(mw.getTargetWord().getPreposition()!=null){
                TreeItem nodeTI=new TreeItem("5,"+mw.getTargetWord().getPreposition());
                mainTI.getChildren().add(nodeTI);
            }
            if(mw.getTargetWord().getConjunction()!=null){
                TreeItem nodeTI=new TreeItem("6,"+mw.getTargetWord().getConjunction());
                mainTI.getChildren().add(nodeTI);
            }  
            tiw.getRoot().getChildren().add(mainTI);
        }
    } 
    
    private void putMainSentencesToTreeTableView(ArrayList<MainSentence> mainsentences,TreeTableView treeTableView){ 
        treeTableView.getRoot().getChildren().clear();
        for(MainSentence ms:mainsentences){  
            TreeItem mainSentenceTI=new TreeItem(ms); 
            TreeItem targetSentenceTI=new TreeItem(ms.getTargetSentence());
            mainSentenceTI.getChildren().add(targetSentenceTI);
            treeTableView.getRoot().getChildren().add(mainSentenceTI);
        }       
    }
    
    private TreeTableView getTreeItemViewOfSentencePoolStage(){
        VBox vb=((VBox)sentencePoolStage.getScene().getRoot());
        TreeTableView tiw=(TreeTableView)vb.getChildren().get(1);  /// buna bir çözüm bull
        return tiw;
    } 
    
    public static void main(String[] args)   {  
        Platform.setImplicitExit(false);
        launch(args);  
    } 
}

