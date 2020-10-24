//windows 
package tabarektranslator;

 
import com.sun.javafx.css.StyleManager;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.systemTray.Separator;
import java.awt.Dimension;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
 
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
 
 
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
 
 
import java.util.logging.Level;
import java.util.logging.Logger;
 
 
 
import javafx.application.Application;  
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
 
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
 
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
 
import javafx.stage.Stage; 
import javafx.stage.StageStyle;
import javax.swing.JMenu;
import javax.swing.JPanel;
 
 
 
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
    volatile String  text ;
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
    String SystemCopyData; 
    
    TextTransfer clip=new TextTransfer();  
    TranslationResponse translation;
    Text textComputer=new Text();
    String toLang="tr";
    String fromLang="en";
     
    HBox progressHB;
    Stage progressStage;
    Dimension screenSize;
    
    NativeMouseMotionListener nativeMouseMotionListener;
    NativeMouseInputListener nativeMouseListener ;
    NativeKeyListener nativeKeyListener;
    
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Override
    public void start(Stage primaryStage) throws Exception { 
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
       
        MenuItem exit=new MenuItem("Exit");
        exit.setCallback(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                systemTray.shutdown();
                //Platform.exit();
                System.exit(0);
            } 
        });
        
        MenuItem options=new MenuItem("Options");
        MenuItem myPool=new MenuItem("My Pool");
        MenuItem dictionary=new MenuItem("Dictionary");
        MenuItem about=new MenuItem("About");
        dorkbox.systemTray.Checkbox enableIcon=new dorkbox.systemTray.Checkbox ("Enable T Icon"); 
        enableIcon.setChecked(true);
        enableIcon.setCallback((e) -> { 
            if(enableIcon.getChecked()==false){
                try {
                    systemTray.setImage(USER_DIR+"/src/Icon/first_icon.png"); 
                } catch ( Exception ex) {
                    System.out.println("Sistem tepsisi resim bulunamadı.."+ex.getMessage()); 
                }
                enableIcon.setChecked(false); 
                GlobalScreen.removeNativeMouseListener(nativeMouseListener);  
            }
            else{ 
                try {
                    systemTray.setImage(USER_DIR+"/src/Icon/second.png");  // for exe -> systemTray.setImage("Icon/second.png");  
                } catch ( Exception ex) {
                    System.out.println("Sistem tepsisi resim bulunamadı.."+ex.getMessage()); 
                }
                enableIcon.setChecked(true);
                GlobalScreen.addNativeMouseListener(nativeMouseListener); 
            } 
        });
        
        Menu mainMenu = systemTray.getMenu();   
        mainMenu.add(myPool);
        mainMenu.add(dictionary); 
        mainMenu.add(options);
        mainMenu.add(about );
        mainMenu.add(new Separator());    
        mainMenu.add(enableIcon );
        mainMenu.add(new Separator());         
        mainMenu.add(exit);
        
       
        
           
        // ----------------------- END --------------------------------------------
        
        // ------------------------- GEREKLİ STAGE  ------------------------------------
        Stage stg=new Stage(StageStyle.UTILITY); 
        stg.setOpacity(0);
        stg.setHeight(0);
        stg.setWidth(0);
        stg.show(); 
        
        // ------------------------- END GEREKLİ STAGE  ------------------------------------
        
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
                if(resizeState==0)
                    translationStage.setOpacity(0);
            }
        });
           
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        titleBarHBox.getChildren().add(region1);
        
        Label titleBarLanguageLabel=new Label();
        titleBarLanguageLabel.setText( capitalize(fromLang) +" To "+capitalize(toLang));
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
            titleBarLanguageLabel.setText(capitalize(fromLang)+" To "+capitalize(toLang)); 
            showTool(translationStage.getX(), translationStage.getY(), mainTranslationTA.getText());
        });
        titleBarHBox.getChildren().add(swapButton);
        
        CheckBox favoriteCB=new CheckBox();
        favoriteCB.setId("check-box");
        titleBarHBox.getChildren().add(favoriteCB);
        
        Button speechButton=new Button();
        speechButton.setId("speechButton");
        titleBarHBox.getChildren().add(speechButton);
        
        // ------------------------- END Tool Title Bar ------------------------------------
        
        //------------------------- Main Tool Translation Text Area  -------------------------
        
        mainTranslationTA=new TextArea(); 
        mainTranslationTA.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {  
               
            }
        });
        
        
        //mainTranslationTA.setMaxSize(600,500); 
        mainTranslationTA.setMinSize(50,30); 
        //mainTranslationTA.setPrefSize(50,50);
        mainTranslationTA.setId("mainTranslationTA"); 
        mainLayoutVBox.getChildren().add(mainTranslationTA);
        mainLayoutVBox.setMargin(mainTranslationTA, new Insets(4, 4, 4, 4));
         
        
         //------------------------- END of Tool Main Translation Text Area  -------------------------
        
        //-------------------  Tool Word Types Tab Pane  ------------------------  show text  
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
            showTool(shortcutPopupIcon.getX()-2, shortcutPopupIcon.getY()-2,text);
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
                        showTool(mousePos.getX(),mousePos.getY(),text); 
                    });
                }
                else if(nke.getKeyCode()==NativeKeyEvent.VC_ESCAPE){  
                    Platform.runLater(() -> {
                        if(shortcutPopupIcon.getOpacity()>0)
                            shortcutPopupIcon.setOpacity(0); 
                        else
                            translationStage.setOpacity(0); 
                    }); 
                } 
            } 
        };
        
        nativeMouseListener=new NativeMouseInputAdapter () {  //  Os mouse listener 
            Timer timer;  
            boolean clickState=false; 
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
                            clickState=false;
                            processClipboard();
                             
                            Platform.runLater(() -> {
                                showIcon(nme.getX() ,nme.getY() );
                            });
                            process=0;
                        }
                    }
                    else{
                        if(timer!=null){
                            timer.cancel();  
                        } 
                        if(clickState){   
                            //System.out.println("geldi");
                            clickState=false;  
                            processClipboard();
                           
                                showIcon(nme.getX() ,nme.getY() );
                            
                        }
                        else{  
                            clickState=true; 
                            timer= new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {  
                                    clickState=false; 
                                }
                            },700);  
                        }  
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
                   System.out.print("[ OK ]\n");
               }
               catch (Exception ex) {
                       System.out.print("[ !! ]\n");
                       ex.printStackTrace();
        }
    }
    
    public void processClipboard(){ 
        clip.setClipboardContents(""); 
        text=getSelectedText();  
       
        clip.setClipboardContents(SystemCopyData); 
       // System.out.println(" + "+clip.getClipboardOldContents());
    }
    
   
    public static void main(String[] args)   {
        Platform.setImplicitExit(false);
        launch(args);  
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
    
   
    
    public void showIcon(int posX,int posY){ 
        if (text!=null){ 
            text=text.trim();
            if(!text.isEmpty()){  
                
                if(mainTranslationTA.isFocused()){
                    translationStage.setAlwaysOnTop(false);
                }
                else if(!translatorPopupEnteredState) {
                        translationStage.setOpacity(0);
                }    
                 
                shortcutPopupIcon.setX(posX+12);
                shortcutPopupIcon.setY(posY+12); 
                shortcutPopupIcon.setOpacity(0.7); 
                shortcutPopupIcon.setHeight(22); 
                shortcutPopupIcon.setWidth(22);   
              // System.out.println(text );//  show
            }  
        } 
    }
   
    
    public void processTool(double posX,double posY,String txt ){
        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }*/
       // System.out.print("yep1-"+txt+"-");
        if(txt!=null&&!txt.isEmpty()){
            System.out.print("yep2-");
            translation=translator.translate(txt,fromLang,toLang);   //translate function get halka açık
            mainTranslationTA.setText(translation.getTranslation());  
            
            textComputer.setLineSpacing(0);
            textComputer.setWrappingWidth(0); 
            textComputer.setText(translation.getTranslation());
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
                System.out.println( bigHeight+  " "+bigContentWidth + " " +bigOtherWordsWidth +" -- " );   
                 
            } 
            else{
                
            } 
        }     
    }
    
    void showTool(double posX,double posY,String txt){  
        shortcutPopupIcon.setOpacity(0);
        translationStage.setAlwaysOnTop(true); 
        translationStage.setOpacity(0);
        progressStage.setOpacity(1); 
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        progressStage.setX(mousePos.getX()+12);
        progressStage.setY(mousePos.getY()+12); 
        System.out.print("I-");
        if(WordTypesTabPane.getParent()!=null){  
            mainLayoutVBox.getChildren().remove(2);  
        }  
        System.out.print("II-");
        executorService.execute(processToolTask(posX+12, posY+12,txt));
        System.out.print("III-");
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
                if(txt!=null&&!txt.isEmpty()){
                    System.out.print("IV-");
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
                    progressStage.setOpacity(0); 
                    //translationStage.setAlwaysOnTop(false);
                }
                else{
                    System.out.println("başarısız");
                    progressStage.setOpacity(0); 
                }
            }

            @Override
            protected void failed() {
                System.out.println("failed -başarısız");
                progressStage.setOpacity(0); 
            }  
        };
    } 
    private String capitalize(String txt) {
        return Character.toUpperCase(txt.charAt(0)) + txt.substring(1);
    }
}
