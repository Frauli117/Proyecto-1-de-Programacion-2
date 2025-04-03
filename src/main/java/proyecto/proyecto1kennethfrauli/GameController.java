/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto.proyecto1kennethfrauli;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Kenneth
 */
public class GameController {

    /**
     * Initializes the controller class.
     */
    @FXML private GridPane playerBoard;
    @FXML private ListView<String> shipList;

    private static int boardSize = 10;
    private static final int[][] playerMatrix = new int[12][12];
    private List<int[]> placedShips = new ArrayList<>();
    @FXML
    private Label messageLabel;
    @FXML
    private Button backButton;
    @FXML
    private RadioButton horizontalRadio;
    @FXML
    private RadioButton verticalRadio;
    @FXML
    private ToggleGroup directionGroup;
    
    @FXML
    private void initialize() {
        generateBoard();
        loadShips();
    }
    
    public static void setBoardSize(int size) {
        boardSize = size;
    }

    private void generateBoard() {
        playerBoard.getChildren().clear();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                final int r = row;
                final int c = col;

                Button cell = new Button();
                cell.setPrefSize(40, 40);
                cell.setOnMouseClicked(e -> placeShip(e, r, c));
                playerBoard.add(cell, col, row);
            }
        }
    }

    private void loadShips() {
        shipList.getItems().addAll("Acorazado (4)", "Crucero (3)", "Crucero (3)", 
                                   "Destructor (2)", "Destructor (2)", "Destructor (2)", 
                                   "Submarino (1)", "Submarino (1)", "Submarino (1)", "Submarino (1)");

        shipList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void placeShip(MouseEvent event, int row, int col) {
        String selectedShip = shipList.getSelectionModel().getSelectedItem();
        if (selectedShip == null) {
            messageLabel.setText("Selecciona un barco antes de colocarlo.");
            return;
        }

        int shipSize = getShipSize(selectedShip);
        if (!canPlaceShip(row, col, shipSize)) {
            messageLabel.setText("Posicion invalida, intenta otra casilla.");
            return;
        }

        placeShipOnBoard(row, col, shipSize);
        shipList.getItems().remove(selectedShip);
        
        messageLabel.setText("");
    }

    private int getShipSize(String shipName) {
        if (shipName.contains("(4)")) return 4;
        if (shipName.contains("(3)")) return 3;
        if (shipName.contains("(2)")) return 2;
        return 1;
    }

    private boolean canPlaceShip(int row, int col, int size) {
        boolean horizontal = horizontalRadio.isSelected();

        if (horizontal) {
            if (col + size > boardSize) return false;
            for (int i = 0; i < size; i++) {
                if (playerMatrix[row][col + i] == 1) return false;
            }
        } else {
            if (row + size > boardSize) return false;
            for (int i = 0; i < size; i++) {
                if (playerMatrix[row + i][col] == 1) return false;
            }
        }

        return true;
    }

    private void placeShipOnBoard(int row, int col, int size) {
        boolean horizontal = horizontalRadio.isSelected();

        for (int i = 0; i < size; i++) {
            int r = horizontal ? row : row + i;
            int c = horizontal ? col + i : col;

            playerMatrix[r][c] = 1;

            Button cell = getCellAt(r, c);
            if (cell != null) {
                cell.setStyle("-fx-background-color: gray;");
            }
        }

        placedShips.add(new int[]{row, col, size});
    }

    private Button getCellAt(int row, int col) {
        for (javafx.scene.Node node : playerBoard.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (Button) node;
            }
        }
        return null;
    }
    
    @FXML
    private void switchToDifficulties() throws IOException {
        App.setRoot("difficulties");
    }  
    
}
