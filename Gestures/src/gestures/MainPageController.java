/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;


/**
 * FXML Controller class
 *
 * @author dewikharismawati
 */
public class MainPageController implements Initializable {

    GestureRecognizer decisionTree = new AdvancedRecognizer();
    
    @FXML
    private Label label;
    
    @FXML
    private Button btnNewProfile;
    
    @FXML 
    private Button btnProfileCancel;
    
    @FXML
    private Button btnNewGesture;
    
    @FXML
    private Button btnProfileSave;
    
    @FXML
    private TextField profileName;
    
    @FXML
    private Button start;
    
    @FXML
    private Button gestureManager;
    
    @FXML
    private Label testLabel;
    
    @FXML
    private Label nameLabelTest;
    
    @FXML
    private ComboBox comboName;
    
    @FXML
    private Button deleteUserBtn;
    
    @FXML
    private Button stopBtn;
    
    @FXML
    private Button exitBtn;
    
    @FXML
    private Button settingsButton;
    
    @FXML
    private TableView<Map.Entry<Command, Gesture>> gestureMappingTable;

    @FXML
    private TableColumn<Map.Entry<Command, Gesture>, String> columnGesture;
    
    @FXML
    private TableColumn<Map.Entry<Command, Gesture>, String> columnCommand;
 
    ArrayList<User> users;
    
    Integer profileListChangedHandlerId;
    
    Integer currentUserChangedHandlerId;
          
    String newName;
    String selectedUser;
    HashMap<Command, Gesture> commandAndGesture;
    ArrayList<Gesture> gestures;
    ObservableList<String> gestureCombo;
    
    @FXML
    private void handleNewGesture(ActionEvent event) throws IOException, Exception{
        if(event.getSource() == btnNewGesture){
            if(UserManager.getCurrentUser() == null){
                showError("Error", "No User Selected", "Create or select a user first.");
                return;
            }
            LeapService.stop();
            Stage stage = (Stage) btnNewGesture.getScene().getWindow();
            
            Parent gesturePage = FXMLLoader.load(getClass().getResource("GesturePage.fxml"));
            Scene scene = new Scene(gesturePage);
            
            stage.setScene(scene);
            stage.show();
        }  
    }
    
    private void showError(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        return;
    }
    
    @FXML
    private void handleSettingsButton(ActionEvent event) throws IOException, Exception{

        if(UserManager.getCurrentUser() == null){
            showError("Error", "No User Selected", "Create or select a user first.");
            return;
        }
        LeapService.stop();
        Stage stage = (Stage) settingsButton.getScene().getWindow();

        Parent settingsPage = FXMLLoader.load(getClass().getResource("SettingsPage.fxml"));
        Scene scene = new Scene(settingsPage);

        stage.setScene(scene);
        stage.show();
        
    }
    
    @FXML
    private void handleCancelNewProfile(ActionEvent event) throws IOException, Exception{
        hideNewProfile();
    }
    
    @FXML
    private void handleStart(ActionEvent event) {
        LeapService.start(decisionTree);
        DecisionTreeViewer.start();
    }
    
    @FXML
    private void handleEndGestureTracking(ActionEvent event){
        LeapService.stop();
    }
    
    @FXML
    private void handleNewProfile(ActionEvent event){
        showNewProfile();      
    }
    
    @FXML
    private void handleSaveNewProfile(ActionEvent event) throws IOException, Exception{
       try{
            newName = profileName.getText();
            UserManager.createProfile(newName);
            populateProfileList();
            selectedUser = newName;
            hideNewProfile();
            UserManager.setCurrentUser(selectedUser);
       }catch (Exception ex) {
            Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
            showError("Error", "Create New Profile Error", ex.getMessage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(this.profileListChangedHandlerId == null){
            profileListChangedHandlerId = Event.registerHandler(Event.TYPE.USER_LIST_CHANGED, (event) -> {
                this.populateProfileList();
                try {
                    this.loadCurrentUser();
                } catch (Exception ex) {
                    Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        if(this.currentUserChangedHandlerId == null){
            currentUserChangedHandlerId = Event.registerHandler(Event.TYPE.USER_SWITCHED, (event) -> {
                try {
                    loadCurrentUser();
                } catch (Exception ex) {
                    Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        hideNewProfile();
        populateProfileList();
        setCellFactories();
        try {
            //System.out.println("initializer for main page controller.  current user: " + UserManager.getCurrentUser());
            loadCurrentUser();
        } catch (Exception ex) {
            Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    private void setCellFactories(){
        columnCommand.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Command, Gesture>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Command, Gesture>, String> p) {
                //first column -> key (Gesture)
                return new SimpleStringProperty(p.getValue().getKey().toTableString().toLowerCase());
            }
        });
        
        columnGesture.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Command, Gesture>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Command, Gesture>, String> p) {
                //second column value (Command)
//                System.out.println("test here");
                return new SimpleStringProperty(p.getValue().getValue() == null ? " - ": p.getValue().getValue().name);
            }
        });
        
        columnGesture.setOnEditCommit(new javafx.event.EventHandler<TableColumn.CellEditEvent<Map.Entry<Command, Gesture>, String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Map.Entry<Command, Gesture>, String> event) {
                //System.out.println("Value selected is " + event.getNewValue()); //To change body of generated methods, choose Tools | Templates.
                Entry entry = event.getRowValue();
                Command command = (Command)entry.getKey();
                String gestureName = event.getNewValue();
                //System.out.println("gesture: " + gestureName +  "\ncommand: " + command);
                try {
                    UserManager.mapGestureToCommand(gestureName, command);
                    UserManager.readyTree();
                } catch (Exception ex) {
                    Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
                } finally{
                    try {
                        populateTable();
                    } catch (Exception ex) {
                        Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
    
    @FXML
    public void populateTable() throws IOException, Exception{
        gestureMappingTable.getItems().clear();
        gestureCombo = FXCollections.observableArrayList();
        //hashmap data
        commandAndGesture = UserManager.getCommandsAndGestures();
        gestures = UserManager.getGestures();
        gestures.forEach((gesture) -> {
            gestureCombo.add(gesture.name);
        });
        columnGesture.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), gestureCombo));
        ObservableList<Map.Entry<Command, Gesture>> items = FXCollections.observableArrayList(commandAndGesture.entrySet());
        gestureMappingTable.getItems().addAll(items);
        gestureMappingTable.setEditable(true);
    }
    
    
    public void populateProfileList(){ 
        comboName.getItems().clear();
        users = UserManager.getAllUsers();
        for(User user : users){               
            comboName.getItems().add(user.getName()); 
        }
    }
    
    public void hideNewProfile(){
        profileName.setVisible(false);
        btnProfileCancel.setVisible(false);
        btnProfileSave.setVisible(false);
    }
    
    public void showNewProfile(){
        profileName.setVisible(true);
        profileName.setText("");
        btnProfileCancel.setVisible(true);
        btnProfileSave.setVisible(true); 
    }
    
    //action for selected user profile combobox
    @FXML
    private void handleComboProfile(ActionEvent event) throws IOException, Exception{
      selectedUser = (String) comboName.getSelectionModel().getSelectedItem();
      if(selectedUser != null){
        UserManager.setCurrentUser(selectedUser);
      }
    }
    
    public void loadCurrentUser() throws Exception{
        String currentUser = UserManager.getCurrentUsername();
        if(currentUser != null){
            comboName.getSelectionModel().select(currentUser);
            populateTable();
        } else {
            comboName.getSelectionModel().clearSelection();
            populateTable();
        }
    }
    
    @FXML
    private void handleGestureManager(ActionEvent event) throws IOException, Exception{
        if(event.getSource() == gestureManager){
            if(UserManager.getCurrentUser() == null){
                showError("Error", "No User Selected", "Create or select a user before managing gestures.");
                return;
            }
            Stage stage = (Stage) gestureManager.getScene().getWindow();
            
            Parent gestureParent = FXMLLoader.load(getClass().getResource("GestureManager.fxml"));
            Scene scene = new Scene(gestureParent);
            
            stage.setScene(scene);
            stage.show();
        }  
    }
    
        
    
    @FXML
    public void handleUserDelete(ActionEvent event) throws IOException, Exception {
        String userToDelete = UserManager.getCurrentUsername();
        if(UserManager.getCurrentUser() == null) {
            showError("Error", "No user selected", "Select a user first");
            return;
        }
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete User Confirmation");
            alert.setHeaderText("Are you sure want to DELETE user: " + userToDelete + " ?");
            alert.setContentText("You will not be able to recover deleted user!");

            Optional<ButtonType> result = alert.showAndWait();
            
            if (result.get() == ButtonType.OK){

                UserManager.deleteUser(userToDelete);
                populateProfileList();
                
                comboName.getSelectionModel().selectFirst();
                String defaultSelection = (String)comboName.getSelectionModel().getSelectedItem();
                UserManager.setCurrentUser(defaultSelection);
                
                Alert alertInfo = new Alert(AlertType.INFORMATION);
                alertInfo.setTitle("Information");
                alertInfo.setHeaderText("You have successfully deleted user " + userToDelete +" !");
                alertInfo.setContentText("First user in the list is selected as default!");
                alertInfo.showAndWait();
                
                
            }
        }catch (Exception ex){
            Logger.getLogger(GestureManagerController.class.getName()).log(Level.SEVERE, null, ex);
            showError("Error", "Delete current user error", ex.getMessage());
        }
    }
    
    public void handleStopBtn(ActionEvent event){
        LeapService.stop();
    }
    
    public void handleExitBtn(ActionEvent event){
        // get a handle to the stage
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
    
}
