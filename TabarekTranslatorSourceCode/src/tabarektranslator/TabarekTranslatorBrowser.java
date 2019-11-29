// 
//package tabarektranslator;
// 
// 
// 
// 
// 
// 
//import java.awt.event.WindowEvent;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Level;
//import java.util.logging.Logger;
// 
// 
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.beans.InvalidationListener;
//import javafx.beans.Observable;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.concurrent.Worker;
// 
// 
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
// 
// 
//import javafx.stage.Stage;
// 
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeDriverService;
//import org.openqa.selenium.chrome.ChromeOptions;
// 
// 
// 
//  
//
///**
// *
// * @author ThekME
// */
//public class TabarekTranslatorBrowser extends Application {
//    //WebEngine webEngine=(new WebView()).getEngine() ; 
//    WebDriver driver;
//    JavascriptExecutor js  ;
//    @Override
//    public void start(Stage primaryStage) throws IOException {
// 
//      
//       /* System.setProperty("webdriver.gecko.driver", "C:\\Users\\ThekME\\Desktop\\geckodriver-v0.23.0-win64\\geckodriver.exe");
//        FirefoxOptions options = new FirefoxOptions()
//        .setProfile(new FirefoxProfile());
//        options.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors");  
//        WebDriver driver = new FirefoxDriver(options);*/
//        //System.setProperty("webdriver.chrome.driver", "C:\\Users\\ThekME\\Desktop\\chromedriver_win32\\chromedriver.exe");
//        
//        
//        //driver.get("https://translate.google.com ");
//        
//         
//     
//       
//
//        //webEngine.load("https://translate.google.com/#en/tr/play"); 
//       
//   System.setProperty("webdriver.chrome.driver", "C:\\Users\\ThekME\\Desktop\\chromedriver_win32_2\\chromedriver.exe");  //setup
//       //File  file= new File("C:\\Users\\ThekME\\Desktop\\phantomjs-2.1.1-windows\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
//      // System.setProperty("phantomjs.binary.path",file.getAbsolutePath());  
//    ChromeOptions options=new ChromeOptions();
//    ChromeDriverService services=new ChromeDriverService.Builder().withSilent(true).withVerbose(false).usingAnyFreePort().build();
//    options.addArguments("--no-startup-window", "--no-sandbox","--headless"
//                        ,"--disable-gpu","--ignore-certificate-errors","--disable-extensions",
//                        "--disable-gpu-sandbox","--disable-win32k-lockdown"
//                        ,"--hide","-disable-dev-shm-usage","--silent");
//    driver = new ChromeDriver(services,options);
//    js = (JavascriptExecutor) driver;
//    driver.navigate().to("https://www.google.com.tr/search?ei&q=translate");
//    
//    
//    System.out.println(driver.findElement(By.id("tw-target-text")).getText()+" "+driver.findElement(By.id("tw-source-text")).getText());    
//    //System.out.println(driver.findElement(By.id("result_box")).getText());    
//    //File html=new File("C:\\Users\\ThekME\\Desktop\\translate.html");
// 
//        String from="en";
//        String to="tr";
//        String text="play";
//        
//        TTDatebase.connect();
//        //Date date = new Date();       
//        //Long nowDate= java.time.Instant.now().getEpochSecond();
//        //String sql = "INSERT INTO Sentences(Sentence,Content,Date) VALUES('I|am playing football','ben futbol oynuyorum',"+nowDate+")";
//        //System.out.println(TTDatebase.setQuery(sql));
//        //TTDatebase.disconnect();
//        TextField txt=new TextField();
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        Button showbtn = new Button("show");
//        btn.setText("Say 'Hello World'");
//        showbtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//               
//                    
//                String content;
//                //content = (String)webEngine.executeScript(" document.getElementById('result_box').firstChild.innerHTML");
//               
//                // Delete cache for navigate back
//               
//                // Delete cookies  
//            }
//        });
//        btn.setOnAction(new EventHandler<ActionEvent>() { 
//            @Override
//            public void handle(ActionEvent event) { 
//                try{
//                    System.out.println("Hello World!"); 
//                    String content=txt.getText().replace("'", "&apos").replace("\"","&quot;");
//                    
//                    
//                    //js.executeScript("document.getElementById('source').value='"+content+"';"
//                     //               +"document.getElementById('gt-submit').click();  "); 
//                    js.executeScript("document.getElementById('tw-source-text-ta').value='"+content+"';");
//                     //           ); 
//                    driver.findElement(By.id("tw-source-text-ta")).sendKeys("[@=@]");
//                     
//                     
//                    
//                    
//                    
//                    //driver.navigate().to("https://translate.google.com/#en/tr/"+content);
//                    System.out.println(driver.findElement(By.id("tw-target-text")).getText());    
//                    //System.out.println(driver.findElement(By.id("result_box")).getText()); 
//                    //   engine.executeScript("document.getElementById('source').value='add'");
//                    //    engine.executeScript("document.getElementById('gt-submit').value='add'"); 
//                    //    System.out.println((String) engine.executeScript("document.getElementById('result_box').innerHTML"));
//                 
//                }catch(Exception e){
//                    System.out.println(e);
//                } 
//            }
//        });
//        
//        VBox root = new VBox();
//      
//        root.getChildren().addAll(btn,txt,showbtn);
//        
//        Scene scene = new Scene(root, 300, 250);
//        
//        primaryStage.setTitle("Hello World!");
//        primaryStage.setScene(scene);
//        primaryStage.show(); 
//    }
//
//  
//    
//    @Override
//    public void stop(){ 
//        try {
//        driver.close();
//        driver.quit();
//         //Runtime.getRuntime().exec("cmd /c start C:\\Users\\ThekME\\Desktop\\KillDrivers.bat");
//        Runtime.getRuntime().exec("taskkill /im chromedriver.exe /f");
//        } catch (IOException ex) {
//            Logger.getLogger(TabarekTranslatorBrowser.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//    } 
//}
//
//
// 
//        /*try { 
//            URL url=new URL("https://translate.google.com/#en/tr/line");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET"); 
//            con.setRequestProperty("User-Agent", "Mozilla/5.0" );
//            con.setDoOutput(true);
//            int responseCode = con.getResponseCode(); 
//            System.out.println(responseCode);
//            
//            BufferedReader in = new BufferedReader(
//            new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//            }
//            in.close(); 
//            
//            int index=response.indexOf("result_box");
//            int start=response.indexOf("<span>", index); 
//            int end=response.indexOf("</span>", index); 
//            
//            System.out.println(response.substring(index, index+200));
//          
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(TabarekTranslatorBrowser.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(TabarekTranslatorBrowser.class.getName()).log(Level.SEVERE, null, ex);
//        }
// 
//         */