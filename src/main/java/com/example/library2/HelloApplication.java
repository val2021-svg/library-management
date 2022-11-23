package com.example.library2;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class HelloApplication extends Application {

    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage; // connect primary stage

        mainWindow();
    }

    public void mainWindow() {
        try {
            // view
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Parent pane = loader.load();

            // controller
            HelloController mainWindowController = loader.getController();
            mainWindowController.setMain(this);

            // scene on stage
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}