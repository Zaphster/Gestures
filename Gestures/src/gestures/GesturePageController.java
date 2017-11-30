/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alec
 */
public class GesturePageController implements Initializable {
    
    private GestureCapturer capturer;
    
    @FXML
    private SubScene visualizerScene;
    
    @FXML
    private Button captureBtn;
    
    @FXML
    private Button exitBtn;    
    
    @FXML
    private void handleExitBtn(ActionEvent event) throws IOException, Exception{
        
        if(event.getSource() == exitBtn){
            
            LeapService.stop();
            try{
                UserManager.readyTree();
            }catch (Exception e){
                System.out.println("Cannot get decision tree ready: " + e.getMessage());
            }
            
            Stage stage = (Stage) exitBtn.getScene().getWindow();
            Parent mainPage = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            
            Scene scene = new Scene(mainPage);
            
            stage.setScene(scene);
            stage.show();
            
        }
    }
    
    @FXML
    private void handleCaptureBtn(ActionEvent event) throws IOException, Exception{
        if(event.getSource() == captureBtn){
            
            Gesture newGesture;
            try {
                newGesture = capturer.capture();
                if(newGesture == null){
                    throw new Exception("No gesture found.  Is your hand above the controller?");
                } else {
                    TextInputDialog dialog = new TextInputDialog("Gesture Name");
                    dialog.setTitle("Gesture Name");
                    dialog.setHeaderText("Please, enter a name for the new gesture.");
                    dialog.setContentText("Please enter gesture name:");

                    Optional<String> result = dialog.showAndWait();
                    if(result.isPresent()){
                        newGesture.name = result.get();
                        System.out.println(newGesture);
                        UserManager.addGestureToCurrentUser(newGesture);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(GesturePageController.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error capturing gesture");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }    
        }  
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
            WebView browser = new WebView();
            URL vurl = getClass().getResource("Visualizer.html");
            browser.getEngine().load(vurl.toExternalForm());
            
            visualizerScene.setHeight(510);
            visualizerScene.setWidth(800);
            visualizerScene.setRoot(browser);
            
            GestureRecognizer recognizer = new GestureCapturer();
            capturer = (GestureCapturer)recognizer;
            LeapService.start(recognizer);
        
    }    
    
}
