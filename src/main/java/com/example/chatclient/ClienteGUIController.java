package com.example.chatclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.SocketException;

public class ClienteGUIController {

    @FXML
    private TextField claveTextEdit;

    @FXML
    private TextField idEnviarMensajeTextEdit;

    @FXML
    private TextField identificarseIDTextEdit;

    @FXML
    private TableView<?> listaDeEnvioTableView;

    @FXML
    private TableView<?> listaDeUsuariosTableView;

    @FXML
    private TextArea mensajesRecibidos;

    ClienteServer clienteServer = new ClienteServer();


    @FXML
    void initialize() throws SocketException {
    }

    @FXML
    void Conectarse(ActionEvent event) {
        clienteServer.connectUsuario(identificarseIDTextEdit.getText());
    }

    @FXML
    void agregarUsuario(ActionEvent event) {

    }

    @FXML
    void eliminarUsuarios(ActionEvent event) {

    }

    @FXML
    void escribirMemsajeAUsuarios(ActionEvent event) {

    }

}
