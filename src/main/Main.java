package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Scene mainScene;

    private MenuBar menuBar;

    private Menu file;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../resources/configuration.fxml"));
        mainScene = new Scene(root, WIDTH, HEIGHT);

        makeMenus();

        primaryStage.setTitle("Drawing application");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void makeMenus() {
        menuBar = new MenuBar();

        file = new Menu();

        menuBar.getMenus().addAll(file);
    }
}
