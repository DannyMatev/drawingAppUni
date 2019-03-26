package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static Scene mainScene;

    public static Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../resources/configuration.fxml"));
        mainScene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("Drawing application");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        mainStage = primaryStage;
    }
}
