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
import java.util.Random;

/**
 * FXML Controller class
 *
 * @author Kenneth
 */
public class GameController {

    @FXML private GridPane playerBoard;
    @FXML private ListView<String> shipList;

    public static int boardSize = 10;
    public static final int[][] playerMatrix = new int[12][12];
    private List<int[]> placedShips = new ArrayList<>();
    public static final int[][] enemyMatrix = new int[12][12];
    public static List<List<int[]>> playerShips = new ArrayList<>();
    public static List<List<int[]>> enemyShips = new ArrayList<>();
    
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
    private Button playButton;
    
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
        boolean horizontal = horizontalRadio.isSelected();
        
        if (!canPlaceShip(playerMatrix, row, col, shipSize, horizontal)) {
            messageLabel.setText("Posicion invalida, intenta otra casilla.");
            return;
        }

        placeShipOnBoard(row, col, shipSize, horizontal);
        shipList.getItems().remove(selectedShip);
        
        messageLabel.setText("");
    }

    private int getShipSize(String shipName) {
        if (shipName.contains("(4)")) return 4;
        if (shipName.contains("(3)")) return 3;
        if (shipName.contains("(2)")) return 2;
        return 1;
    }

    private boolean canPlaceShip(int[][] matrix, int row, int col, int size, boolean horizontal) {

        if (horizontal) {
            if (col + size > boardSize) return false;
            for (int i = 0; i < size; i++) {
                if (matrix[row][col + i] == 1) return false;
            }
        } else {
            if (row + size > boardSize) return false;
            for (int i = 0; i < size; i++) {
                if (matrix[row + i][col] == 1) return false;
            }
        }

        return true;
    }

    private void placeShipOnBoard(int row, int col, int size, boolean horizontal) {
        
        List<int[]> shipCoordinates = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int r = horizontal ? row : row + i;
            int c = horizontal ? col + i : col;
            playerMatrix[r][c] = 1;

            Button cell = getCellAt(r, c);
            if (cell != null) {
                cell.setStyle("-fx-background-color: gray;");
            }
            shipCoordinates.add(new int[]{r, c});
        }
        placedShips.add(new int[]{row, col, size});
        playerShips.add(shipCoordinates); 
    }

    private Button getCellAt(int row, int col) {
        for (javafx.scene.Node node : playerBoard.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (Button) node;
            }
        }
        return null;
    }
    
    private void placeShipsRandomly(int[][] matrix) {
        enemyShips.clear();

        int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
        Random random = new Random();

        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = random.nextInt(boardSize);
                int col = random.nextInt(boardSize);
                boolean horizontal = random.nextBoolean();

                List<int[]> candidate = new ArrayList<>();
                boolean valid = true;

                for (int i = 0; i < size; i++) {
                    int r = row + (horizontal ? 0 : i);
                    int c = col + (horizontal ? i : 0);

                    if (r >= boardSize || c >= boardSize || matrix[r][c] == 1) {
                        valid = false;
                        break;
                    }

                    candidate.add(new int[]{r, c});
                }

                if (valid) {
                    for (int[] coord : candidate) {
                        matrix[coord[0]][coord[1]] = 1;
                    }
                    enemyShips.add(candidate);
                    placed = true;
                }
            }
        }
    }
    
    @FXML
    private void switchToDifficulties() throws IOException {
        App.setRoot("difficulties");
    }
    
    @FXML
    private void switchToPlay() throws IOException {
        if (placedShips.size() < 10) {
            messageLabel.setText("Debes colocar todos los barcos antes de jugar.");
        return;
        }
        placeShipsRandomly(enemyMatrix);
        
        App.setRoot("play");
    }
    
    public static void resetGameState() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                playerMatrix[i][j] = 0;
                enemyMatrix[i][j] = 0;
            }
        }
        playerShips.clear();
        enemyShips.clear();
    }
    
}
