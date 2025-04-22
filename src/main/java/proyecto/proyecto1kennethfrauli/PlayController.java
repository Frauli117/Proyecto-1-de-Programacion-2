/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto.proyecto1kennethfrauli;

import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.Node;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * FXML Controller class
 *
 * @author Kenneth
 */
public class PlayController {

    @FXML
    private GridPane playerBoard;
    @FXML
    private GridPane enemyBoard;
    @FXML
    private Label messageLabel;

    private boolean playerTurn = true;

    private boolean[][] enemyHits = new boolean[12][12];
    private boolean[][] playerHits = new boolean[12][12];

    @FXML
    private void initialize() {
        generatePlayerBoard();
        generateEnemyBoard();
    }

    private void generatePlayerBoard() {
        for (int row = 0; row < GameController.boardSize; row++) {
            for (int col = 0; col < GameController.boardSize; col++) {
                Button cell = new Button();
                cell.setPrefSize(35, 35);
                playerBoard.add(cell, col, row);
            }
        }
    }
    

    private void generateEnemyBoard() {
        for (int row = 0; row < GameController.boardSize; row++) {
            for (int col = 0; col < GameController.boardSize; col++) {
                Button cell = new Button();
                cell.setPrefSize(35, 35);
                int finalRow = row;
                int finalCol = col;
                cell.setOnAction(e -> handlePlayerShot(cell, finalRow, finalCol));
                enemyBoard.add(cell, col, row);
            }
        }
    }
    
    private void handlePlayerShot(Button cell, int row, int col) {
        if (!playerTurn || enemyHits[row][col]) return;

        enemyHits[row][col] = true;

        if (GameController.enemyMatrix[row][col] == 1) {
            if (isShipSunk(GameController.enemyShips, row, col, enemyHits)) {
                markSunkShip(GameController.enemyShips, row, col, enemyBoard);
                if (allShipsSunk(GameController.enemyMatrix, enemyHits)) {
                    EndController.setResultMessage("¡Felicidades " + App.playerName + "! ¡Has ganado!");
                    try {
                        App.setRoot("end");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                messageLabel.setText("¡Hundido!");
            } else {
                cell.setStyle("-fx-background-color: orange;");
                messageLabel.setText("¡Tocado!");
            }
        } else {
            cell.setStyle("-fx-background-color: blue;");
            messageLabel.setText("¡Agua!");
        }

        cell.setDisable(true);
        playerTurn = false;

        new Thread(() -> {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            javafx.application.Platform.runLater(this::enemyTurn);
        }).start();
    }
    
    private void enemyTurn() {
        messageLabel.setText("Turno del enemigo");
        Random random = new Random();
        int row, col;

        do {
            row = random.nextInt(GameController.boardSize);
            col = random.nextInt(GameController.boardSize);
        } while (playerHits[row][col]);

        playerHits[row][col] = true;

        for (Node node : playerBoard.getChildren()) {
            if (!(node instanceof Button)) continue;
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);

            int r = (nodeRow != null) ? nodeRow : 0;
            int c = (nodeCol != null) ? nodeCol : 0;

            if (r == row && c == col) {
                Button cell = (Button) node;

                if (GameController.playerMatrix[row][col] == 1) {
                    if (isShipSunk(GameController.playerShips, row, col, playerHits)) {
                        markSunkShip(GameController.playerShips, row, col, playerBoard);
                        if (allShipsSunk(GameController.playerMatrix, playerHits)) {
                            EndController.setResultMessage("¡Game Over....El enemigo ha ganado!");
                            try {
                                App.setRoot("end");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        messageLabel.setText("¡La computadora hundio uno de tus barcos!");
                    } else {
                        cell.setStyle("-fx-background-color: orange;");
                        messageLabel.setText("¡El enemigo te dio!");
                    }
                } else {
                    cell.setStyle("-fx-background-color: blue;");
                    messageLabel.setText("¡EL enemigo fallo!");
                }
                break;
            }
        }

        playerTurn = true;
    }
    
    private boolean isShipSunk(List<List<int[]>> ships, int row, int col, boolean[][] hits) {
        for (List<int[]> ship : ships) {
            for (int[] coord : ship) {
                if (coord[0] == row && coord[1] == col) {
                    boolean allHit = true;
                    for (int[] part : ship) {
                        if (!hits[part[0]][part[1]]) {
                            allHit = false;
                            break;
                        }
                    }
                    return allHit;
                }
            }
        }
        return false;
    }
    
    private void markSunkShip(List<List<int[]>> ships, int row, int col, GridPane board) {
        for (List<int[]> ship : ships) {
            for (int[] coord : ship) {
                if (coord[0] == row && coord[1] == col) {
                    for (int[] part : ship) {
                        paintCellRed(board, part[0], part[1]);
                    }
                    return;
                }
            }
        }
    }
    

    private void paintCellRed(GridPane board, int row, int col) {
        for (Node node : board.getChildren()) {
            if (!(node instanceof Button)) continue;
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);

            int r = (nodeRow != null) ? nodeRow : 0;
            int c = (nodeCol != null) ? nodeCol : 0;

            if (r == row && c == col) {
                Button cell = (Button) node;
                cell.setStyle("-fx-background-color: red;");
                break;
            }
        }
    }
    
    private boolean allShipsSunk(int[][] matrix, boolean[][] hits) {
        for (int row = 0; row < GameController.boardSize; row++) {
            for (int col = 0; col < GameController.boardSize; col++) {
                if (matrix[row][col] == 1 && !hits[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @FXML
    private void onFinishGame() {
        int sunkPlayer = countSunkShips(GameController.playerShips, playerHits);
        int sunkEnemy = countSunkShips(GameController.enemyShips, enemyHits);

        System.out.println("Jugador hundidos: " + sunkEnemy);
        System.out.println("Enemigo hundidos: " + sunkPlayer);

        if (sunkEnemy > sunkPlayer) {
            EndController.setResultMessage("¡Felicidades " + App.playerName + "! ¡Has ganado por hundir mas barcos!");
        } else if (sunkPlayer > sunkEnemy) {
            EndController.setResultMessage("¡Game Over... El enemigo gano por hundir mas barcos!");
        } else {
            int damagedPlayer = countDamagedParts(GameController.playerShips, playerHits);
            int damagedEnemy = countDamagedParts(GameController.enemyShips, enemyHits);

            System.out.println("Jugador dañados: " + damagedEnemy);
            System.out.println("Enemigo dañados: " + damagedPlayer);

            if (damagedEnemy > damagedPlayer) {
                EndController.setResultMessage("¡Felicidades " + App.playerName + "! ¡Has ganado por causar mas daño!");
            } else if (damagedPlayer > damagedEnemy) {
                EndController.setResultMessage("¡Game Over... El enemigo gano por causar mas daño!");
            } else {
                EndController.setResultMessage("¡Empate tecnico!");
            }
        }

        try {
            App.setRoot("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int countSunkShips(List<List<int[]>> ships, boolean[][] hits) {
        int sunk = 0;
        for (List<int[]> ship : ships) {
            boolean allHit = true;
            for (int[] part : ship) {
                if (!hits[part[0]][part[1]]) {
                    allHit = false;
                    break;
                }
            }
            if (allHit) sunk++;
        }
        return sunk;
    }

    private int countDamagedParts(List<List<int[]>> ships, boolean[][] hits) {
        int damage = 0;
        for (List<int[]> ship : ships) {
            for (int[] part : ship) {
                if (hits[part[0]][part[1]]) {
                    damage++;
                }
            }
        }
        return damage;
    }

}
