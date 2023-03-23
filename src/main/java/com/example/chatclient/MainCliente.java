package com.example.chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainCliente extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ClienteServer clienteServer = new ClienteServer();
        FXMLLoader fxmlLoader = new FXMLLoader(MainCliente.class.getResource("ClienteGUI.fxml"));
        fxmlLoader.setController(new ClienteGUIController());
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}