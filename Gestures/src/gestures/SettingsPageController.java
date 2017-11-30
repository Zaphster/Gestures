package gestures;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alec
 */

public class SettingsPageController implements Initializable {

    @FXML
    private Slider movementDelaySlider;
     
    @FXML
    private Slider clickDelaySlider;
   
    @FXML
    private Slider mouseSensitivitySlider;
    
    @FXML
    private Slider keyDelaySlider;
    
    @FXML
    private Slider gestureThresholdSlider;
    
    @FXML
    private RadioButton zAxisRadio;
    
    @FXML
    private RadioButton yAxisRadio;
    
    @FXML
    private Button applyButton; 
    
    @FXML
    private Button returnButton;
    
    @FXML
    private Button restoreButton; 
    
    @FXML
    private ToggleGroup AxisToggle;

    private UserSettings settings;
    
    /**
     * Initializes the controller class.
     */

    
    @FXML
    private void handleReturnButton(ActionEvent event) throws IOException, Exception{
        
        if(!applyButton.isDisable()){
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Save your changes?");
            alert.setTitle("Save Confirmation");
            alert.setContentText("Would you like to save your settings\nbefore you exit?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                    if (type == okButton) {
                        saveSettings();
                    }
            });
        }
        
        Stage stage = (Stage) returnButton.getScene().getWindow();
        Parent mainPage = FXMLLoader.load(getClass().getResource("MainPage.fxml"));

        Scene scene = new Scene(mainPage);

        stage.setScene(scene);
        stage.show();
        
    }
    
    @FXML
    private void handleDefaultButton(ActionEvent event) throws IOException, Exception{
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Restore Default Settings?");
        alert.setTitle("Restore Confirmation");
        alert.setContentText("This will change your settings back to the default.\nAre you sure?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    settings.setDefaultSettings();
                    showCurrentSettings();
                    
                    if(!applyButton.isDisable()){
                        applyButton.setDisable(true);
                    }
                }
        });
    }
    
    @FXML
    private void handleApplyButton(ActionEvent event) throws IOException, Exception{
        
        saveSettings();
        
        if(!applyButton.isDisable()){
            applyButton.setDisable(true);
        }
        
    }
    
    private void saveSettings(){
        settings.setMouseMovementDelay((int)Math.floor(this.movementDelaySlider.getValue()));
        settings.setMouseClickDelay((int)Math.floor(this.clickDelaySlider.getValue()));
        settings.setPadSensitivity((float)this.mouseSensitivitySlider.getValue());
        settings.setKeyPressDelay((int)Math.floor(this.keyDelaySlider.getValue()));
        settings.setGestureFoundThreshold((int)Math.floor(this.gestureThresholdSlider.getValue()));
        
        if(zAxisRadio.isSelected()){
            settings.setAxisChoice(true);
        }else{
            settings.setAxisChoice(false);
        }
        
        //UserManager.getCurrentUser().setSettings();
    }
    
    private void showCurrentSettings(){
        this.movementDelaySlider.setValue((double)settings.getMouseMovementDelay());
        this.clickDelaySlider.setValue((double)settings.getMouseClickDelay());
        this.mouseSensitivitySlider.setValue((double)settings.getPadSenstitivity());
        this.keyDelaySlider.setValue((double)settings.getKeyPressDelay());
        this.gestureThresholdSlider.setValue((double)settings.getGestureFoundThreshold());
        
        if(settings.getAxisChoice()){
            this.zAxisRadio.setSelected(true);
        }else{
            this.yAxisRadio.setSelected(true);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
       //settings = UserManager.getCurrentUser().getSettings();
       settings = new UserSettings();
       
       showCurrentSettings();
       applyButton.setDisable(true);
       
    }  

    @FXML
    private void movementDelayChange(MouseEvent event) {
        if(applyButton.isDisabled()){
            applyButton.setDisable(false);
        }
    }

    @FXML
    private void clickDelayChange(MouseEvent event) {
        if(applyButton.isDisabled()){
            applyButton.setDisable(false);
        }
    }

    @FXML
    private void mouseSensitivityChange(MouseEvent event) {
        if(applyButton.isDisabled()){
            applyButton.setDisable(false);
        }
    }

    @FXML
    private void keyDelayChange(MouseEvent event) {
        if(applyButton.isDisabled()){
            applyButton.setDisable(false);
        }
    }

    @FXML
    private void zAxisRadioChange(ActionEvent event) {
        if(applyButton.isDisabled()){
            applyButton.setDisable(false);
        }
    }

    @FXML
    private void yAxisRadioChange(ActionEvent event) {
        if(applyButton.isDisabled()){
            applyButton.setDisable(false);
        }
    }

    @FXML
    private void gestureThresholdChange(MouseEvent event) {
        if(applyButton.isDisabled()){
            applyButton.setDisable(false);
        }
    }
    
}
