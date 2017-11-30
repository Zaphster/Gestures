/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Cameron
 */
public class Gestures extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent mainPage = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        Scene scene = new Scene(mainPage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @Override
    public void stop() throws Exception{
        LeapService.stop();
        super.stop();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        try {
            UserManager.loadFromFile();
//            System.out.println(UserManager.manager.makeJSONString());
        } catch (Exception ex) {
            Logger.getLogger(Gestures.class.getName()).log(Level.SEVERE, null, ex);
        }
        launch(args);
    }
}  

