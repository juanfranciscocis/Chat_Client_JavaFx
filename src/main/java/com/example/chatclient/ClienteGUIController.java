package com.example.chatclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.SocketException;
import java.util.ArrayList;

public class ClienteGUIController {


    @FXML
    private TableView<Mensaje> mensajesTableView;

    @FXML
    private Button conectarseBoton;

    @FXML
    private ChoiceBox<String> idEnviarMensajeTextEdit;

    @FXML
    private TextField mensajeAEnviar;

    @FXML
    private TextField identificarseIDTextEdit;

    @FXML
    private TableView<Cliente> listaDeEnvioTableView;

    @FXML
    public  TableView<Cliente> listaDeUsuariosTableView;

    ClienteServer clienteServer = new ClienteServer();

    ObservableList<Cliente> usuariosList;

    ObservableList<Mensaje> mensajesList;

    boolean conectado = false;



    @FXML
    void initialize() throws SocketException {
        crearTablaMensajes();
    }

    @FXML
    void Conectarse(ActionEvent event) {
        clienteServer.connectUsuario(identificarseIDTextEdit.getText());
        if (clienteServer.getCliente().getConectado()){
            conectado = true;
            recargarClientes(event);
            new Thread(clienteServer).start();
            //DESACTIVO EL BOTON DE CONECTARSE
            identificarseIDTextEdit.setDisable(true);
            conectarseBoton.setDisable(true);
        }
    }

    @FXML
    void recargarListaChoiceBox(MouseEvent event) {
        cargarUsuariosEnChoiceBox();
    }
    void cargarUsuariosEnChoiceBox(){
        usuariosList = clienteServer.getListaClientes();
        idEnviarMensajeTextEdit.getItems().clear();
        for (Cliente cliente: usuariosList) {
            //AGREGO LOS USUARIOS A LA LISTA DE ENVIO SOLO SI ESTAN CONECTADOS
            if (cliente.getConectado()){
                idEnviarMensajeTextEdit.getItems().add(cliente.getId());
            }
        }
    }

    @FXML
    void agregarUsuario(ActionEvent event) {
        //AL PRESIONAR EL BOTON OBTENGO EL ID DEL CHOICE BOX Y LO AGREGO A LA LISTA DE ENVIO
        String id = idEnviarMensajeTextEdit.getValue();
        if (id == null){
            new Alert(Alert.AlertType.ERROR, "No se ha seleccionado ningun usuario");
            return;
        }
        clienteServer.agregarUsuarioAListaDeEnvio(id);
        poblarListaDeEnvio(clienteServer.getListaDinamica());
    }

     void poblarListaDeEnvio(ArrayList<Cliente> listaDeEnvio){
        ObservableList<Cliente> listaDeEnvioObservable = FXCollections.observableArrayList(listaDeEnvio);
        try {
            listaDeEnvioTableView.getColumns().clear();
            listaDeEnvioTableView.getItems().clear();
        } catch (Exception e) {
            System.out.println("No se pudo limpiar la tabla");
            new Alert(Alert.AlertType.ERROR, "No se pudo limpiar la tabla");
        }

        try {
            TableColumn<Cliente, String> usuarioID = new TableColumn<>("USUARIO");
            usuarioID.setCellValueFactory(new PropertyValueFactory<>("id"));
            TableColumn<Cliente, Boolean> conectado = new TableColumn<>("CONEXION");
            conectado.setCellValueFactory(new PropertyValueFactory<>("conectado"));
            listaDeEnvioTableView.getColumns().addAll(usuarioID,conectado);
            listaDeEnvioTableView.setItems(listaDeEnvioObservable);
        } catch (Exception e) {
            System.out.println("No se pudo poblar la tabla");
        }



    }

    @FXML
    void eliminarUsuarios(ActionEvent event) {
        //AL PRESIONAR EL BOTON ELIMINO LA LISTA DE ENVIO DEL CLIENTE SERVER
        clienteServer.eliminarListaDeEnvio();
        poblarListaDeEnvio(clienteServer.getListaDinamica());

    }

    @FXML
    void escribirMemsajeAUsuarios(ActionEvent event) {

        System.out.println("Enviando mensaje");

        if (!conectado){
            new Alert(Alert.AlertType.ERROR, "No estas conectado").showAndWait();
            return;
        }
        if (clienteServer.getListaDinamica().size() == 0){
            new Alert(Alert.AlertType.ERROR, "No hay usuarios en la lista de envio").showAndWait();
            return;
        }
        if (mensajeAEnviar.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "No hay mensaje para enviar").showAndWait();
            return;
        }
        clienteServer.enviarMensaje(mensajeAEnviar.getText());
        mensajeAEnviar.setText("");
        System.out.println("Mensaje enviado");



    }

    void cargarMensajesTableView(){

        try {
            crearTablaMensajes();
            mensajesTableView.setItems(mensajesList);
        } catch (IllegalStateException e) {
               System.out.println();
        }
    }

    void crearTablaMensajes(){
        System.out.println("Cargando tabla de mensajes");

        try {
            mensajesTableView.getColumns().clear();
            mensajesTableView.getItems().clear();
        } catch (Exception e) {
            System.out.println("No se pudo limpiar la tabla");
            new Alert(Alert.AlertType.ERROR, "No se pudo limpiar la tabla");
        }


        try {

            mensajesList = clienteServer.getListaMensajes();
            System.out.println("MENSAJESLIST:" + mensajesList);


            TableColumn<Mensaje, String> usuarioID = new TableColumn<>("idEnvio");
            usuarioID.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
            TableColumn<Mensaje, String> conectado = new TableColumn<>("mensaje");
            conectado.setCellValueFactory(new PropertyValueFactory<>("mensaje"));
            mensajesTableView.getColumns().addAll(usuarioID, conectado);

        } catch (Exception e) {
            System.out.println("No se pudo poblar la tabla");
        }
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
        } catch (IllegalStateException e) {
            System.out.println("No se pudo poblar la tabla");
            new Alert(Alert.AlertType.ERROR, "No se pudo poblar la tabla");
        }




    }


}
