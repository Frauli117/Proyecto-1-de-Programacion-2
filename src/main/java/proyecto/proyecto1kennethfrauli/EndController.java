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
import javafx.scene.control.Label;
import javafx.application.Platform;

/**
 * FXML Controller class
 *
 * @author Kenneth
 */
    public class EndController {

        @FXML
        private Label resultLabel;

        private static String resultMessage = "";

        public static void setResultMessage(String message) {
            resultMessage = message;
        }
        @FXML
        private void initialize() {
            resultLabel.setText(resultMessage);
        }

        @FXML
        private void playAgain() {
            try {
                GameController.resetGameState();
                App.setRoot("primary"); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @FXML
        private void exitGame() {
            Platform.exit();
        } 

}
