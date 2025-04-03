/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto.proyecto1kennethfrauli;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Kenneth
 */
public class DifficultiesController {

    /**
     * Initializes the controller class.
     */
    @FXML 
    private Button easyButton;
    @FXML 
    private Button mediumButton;
    @FXML 
    private Button hardButton;
    @FXML 
    private Button backButton;

    @FXML
    private void setEasy() throws IOException {
        GameController.setBoardSize(8);
        App.setRoot("game");
    }

    @FXML
    private void setMedium() throws IOException {
        GameController.setBoardSize(10);
        App.setRoot("game");
    }

    @FXML
    private void setHard() throws IOException {
        GameController.setBoardSize(12);
        App.setRoot("game");
    }  
    
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }   
    
}
