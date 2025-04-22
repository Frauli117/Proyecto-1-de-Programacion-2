package proyecto.proyecto1kennethfrauli;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class App extends Application {

    private static Stage primaryStage;
    public static String playerName;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        setRoot("primary");
        primaryStage.setTitle("Batalla Naval");
        primaryStage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 1200, 800));
    }

    public static void main(String[] args) {
        launch(args);
    }
}