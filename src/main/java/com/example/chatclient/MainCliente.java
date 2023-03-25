package com.example.chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainCliente extends Application {

    static ClienteServer clienteServer = new ClienteServer();
    static ClienteGUIController clienteGUIController = new ClienteGUIController();


    public static ClienteGUIController getController() {
        return new MainCliente().clienteGUIController;
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(clienteGUIController);
        FXMLLoader fxmlLoader = new FXMLLoader(MainCliente.class.getResource("ClienteGUI.fxml"));
        fxmlLoader.setController(clienteGUIController);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }




    public static void main(String[] args) {
        launch();
    }
}