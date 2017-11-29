/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author dewikharismawati
 */
public class GestureManagerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Label currentUser;
    
    @FXML
    private Button backBtn;
    
    @FXML
    private Button deleteGesture;
    
    @FXML
    private TableView gestureTable;
    
    @FXML
    private TableColumn<ArrayList<Gesture>, String> gestureColumn;
    
    
    
    ArrayList<Gesture> gestures;
    
    ObservableList<String> gesturelist;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        currentUser.setText(UserManager.getCurrentUsername());
        setCellFactories();
        populateTable();
    } 
    
    @FXML
    private void handleBackBtn(ActionEvent event) throws IOException, Exception{
        
        if(event.getSource() == backBtn){
            
            
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Parent mainPage = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            
            Scene scene = new Scene(mainPage);
            
            stage.setScene(scene);
            stage.show();
            
        }
    }
    
    private void setCellFactories(){
            gestureColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Gesture>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Gesture>, String> p) {
                return new SimpleObjectProperty(p.getValue());
            }
            });         
    }
    
    
    @FXML
    private void populateTable(){
        gestureTable.getItems().clear();
        gesturelist = FXCollections.observableArrayList();
        
        gestures = UserManager.getCurrentUser().getGestures();
        
        gestures.forEach((gesture) -> {
            gesturelist.add(gesture.name);
        });
        
        gestureTable.getItems().addAll(gesturelist);
        
    }
    
    @FXML
    private void deleteGestureFromList() throws IOException, Exception {
        
        String selectedGesture = (String) gestureTable.getSelectionModel().getSelectedItem();
        
        try {
            gestureTable.getItems().remove(selectedGesture);
            UserManager.removeGestureByNameFromCurrentUser(selectedGesture);
        } catch (Exception ex) {
            Logger.getLogger(GestureManagerController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Delete Gesture Error");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }  
    }    
}
