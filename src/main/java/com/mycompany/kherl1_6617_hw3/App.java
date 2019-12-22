/*

CSCI 6617 S2 Fall 2019
Java Programming
KRIKOR HERLOPIAN
Kherl1@unh.newhaven.edu
Instructor: Sheehan

App.java
The entry point for the Application is this class.

 */
package com.mycompany.kherl1_6617_hw3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static javafx.application.Application.launch;
import javafx.stage.Screen;

public class App extends Application {

    //get the screen width and height, so that we design our application based on it.
    static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(Common.loadFXML("home"), 1000, 800);
        stage.setScene(scene);
        stage.setMinHeight(screenHeight - 30);
        stage.setMinWidth(App.screenWidth / 1.4);
        stage.setMaxHeight(screenHeight - 30);
        stage.setMaxWidth(App.screenWidth / 1.4);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(Common.loadFXML(fxml));
    }

    public static void main(String[] args) {
        launch();
    }
}
