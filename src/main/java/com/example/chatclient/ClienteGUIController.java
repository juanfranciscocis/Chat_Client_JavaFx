package com.example.chatclient;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.SocketException;

public class ClienteGUIController {



    @FXML
    private TextField idEnviarMensajeTextEdit;

    @FXML
    private TextField identificarseIDTextEdit;

    @FXML
    private TableView<?> listaDeEnvioTableView;

    @FXML
    private TableView<Cliente> listaDeUsuariosTableView;

    @FXML
    private TextArea mensajesRecibidos;

    ClienteServer clienteServer = new ClienteServer();

    ObservableList<Cliente> usuariosList;

    boolean conectado = false;


    @FXML
    void initialize() throws SocketException {
    }

    @FXML
    void Conectarse(ActionEvent event) {
        clienteServer.connectUsuario(identificarseIDTextEdit.getText());
        if (clienteServer.getCliente().getConectado()){
            conectado = true;
            recargarClientes(event);
        }
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


    void recargarClientes(ActionEvent event) {

        if (!conectado){
            new Alert(Alert.AlertType.ERROR, "No estas conectado");
            return;
        }



        try {
            listaDeUsuariosTableView.getColumns().clear();
            listaDeUsuariosTableView.getItems().clear();


        } catch (Exception e) {
            System.out.println("No se pudo limpiar la tabla");
            new Alert(Alert.AlertType.ERROR, "No se pudo limpiar la tabla");
        }

        try {

            usuariosList = clienteServer.getListaClientes();


            TableColumn<Cliente, String> usuarioID = new TableColumn<>("USUARIO");
            usuarioID.setCellValueFactory(new PropertyValueFactory<>("id"));
            TableColumn<Cliente, Boolean> conectado = new TableColumn<>("CONEXION");
            conectado.setCellValueFactory(new PropertyValueFactory<>("conectado"));
            listaDeUsuariosTableView.getColumns().addAll(usuarioID,conectado);
            listaDeUsuariosTableView.setItems(usuariosList);
        } catch (Exception e) {
            System.out.println("No se pudo poblar la tabla");
            new Alert(Alert.AlertType.ERROR, "No se pudo poblar la tabla");
        }




    }

}
