package proyecto.proyecto1kennethfrauli;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;

public class PrimaryController {
    @FXML
    private TextField nameField;
    @FXML
    private Button nextButton;
    @FXML
    private Hyperlink aboutLink;
    @FXML
    private void switchToAbout() throws IOException {
        App.setRoot("about");
    }
    @FXML
    private void switchToDifficulties() throws IOException {
        String playerName = nameField.getText().trim();

        if (playerName.isEmpty()) {
            System.out.println("Por favor, ingrese su nombre antes de continuar.");
            nameField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        } else {
            nameField.setStyle("");
            System.out.println("Jugador: " + playerName);
            App.setRoot("difficulties");
        }
    }
    
}
