package com.example.chatclient;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class ClienteServer extends Task {

    //ATRIBUTOS

    private DatagramSocket socket;
    private Cliente cliente;
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private  ArrayList<Cliente> listaDinamicaClientes = new ArrayList<>();
    public ObservableList<Mensaje> listaMensajes = FXCollections.observableArrayList();
    //CONSTRUCTOR

    public ClienteServer() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    //GETTERS Y SETTERS

    public ObservableList<Cliente> getListaClientes() {
        return listaClientes;
    }

    public Cliente getCliente(){
        return cliente;
    }

    public ArrayList<Cliente> getListaDinamicaClientes() {
        return listaDinamicaClientes;
    }

    public ObservableList<Mensaje> getListaMensajes() {
        return listaMensajes;
    }

    public void setListaMensajes(Mensaje nuevoMensaje) {
        this.listaMensajes.add(nuevoMensaje);
    }

    //METODOS

    //CONECTANDO EL USUARIO CON EL SERVER Y AUTENTICANDOLO
    public void connectUsuario(String id){
        try{
            cliente = new Cliente(id,true);
            String conectarse = cliente.getId() + "-AUTENTICAR";
            byte[] data = conectarse.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data,
                    data.length, InetAddress.getLocalHost(), 5000);
            socket.send(sendPacket);

            //verificando autenticacion
            if (verificandoAutenticacion()){
                //obteniendo lista de usuarios
                listaClientesDesdeServer();
            }



        }catch(Exception e){
            e.printStackTrace();
        }
    }


    // AUTENTICANDO EL USUARIO CON EL SERVER Y BASE DE DATOS
    public boolean verificandoAutenticacion(){
        while (true)

        {

            try // receive packet and display contents
            {
                byte[] data = new byte[100]; // set up packet
                DatagramPacket receivePacket = new DatagramPacket(
                        data, data.length);

                socket.receive(receivePacket); // wait for packet

                //al recibir el mensaje se envia siempre un id separado por - y el mensaje
                String[] mensaje = new String(receivePacket.getData(),
                        0, receivePacket.getLength()).split("-");

                if (mensaje[0].equals("ERROR")) {
                    new Alert(Alert.AlertType.ERROR, mensaje[1]).showAndWait();
                    cliente.conectado = false;
                    return false;
                }else if(mensaje[0].equals("MENSAJE")){
                    new Alert(Alert.AlertType.INFORMATION, mensaje[1]).showAndWait();
                    return true;
                }
            }
            catch (IOException exception)
            {
                mensajesConsola(exception + "\n");
                exception.printStackTrace();
            }
        }
    }

    //OBTENIENDO LA LISTA DE CLIENTES DESDE EL SERVER

    public void listaClientesDesdeServer(){

        while (true) {

            try // receive packet and display contents
            {
                byte[] data = new byte[100]; // set up packet
                DatagramPacket receivePacket = new DatagramPacket(
                        data, data.length);

                socket.receive(receivePacket); // wait for packet

                System.out.println("Recibiendo lista de clientes");
                String clientesRaw = String.valueOf(new String(receivePacket.getData(),
                        0, receivePacket.getLength()));
                System.out.println(clientesRaw);

                //al recibir el mensaje obtengo el id del cliente seguido de / el estado de conexion y - para separar
                String[] mensaje = clientesRaw.split("-");
                ObservableList<Cliente> clientes = FXCollections.observableArrayList();
                //the first element is the id of the client and the second is the state of connection, put it in the list
                for (int i = 0; i < mensaje.length; i++) {
                    String[] cliente = mensaje[i].split("/");
                    clientes.add(new Cliente(cliente[0], Boolean.parseBoolean(cliente[1])));
                }
                System.out.println("Lista de clientes recibida");
                System.out.println(clientes);

                listaClientes = clientes;
                break;


            } catch (IOException exception) {
                System.out.println();
            }

        }


    }


    //RECIBIENDO MENSAJES DE OTROS CLIENTES UNA VEZ CONECTADO, USANDO THREADS

    @Override
    protected Object call() {

        System.out.println("Recibiendo mensajes de otros clientes");


        while (true) {

            try // receive packet and display contents
            {
                byte[] data = new byte[100]; // set up packet
                DatagramPacket receivePacket = new DatagramPacket(
                        data, data.length);

                socket.receive(receivePacket); // wait for packet

                String clientesRaw = String.valueOf(new String(receivePacket.getData(),
                        0, receivePacket.getLength()));
                //al recibir el mensaje obtengo un - para separar
                String[] mensaje = clientesRaw.split("-");
                //NUEVOS USUARIOS CREADOS O ELIMINADOS O CONECTADOS
                if(mensaje[0].equals("NUEVOS USUARIOS")) {
                    listaClientes.removeAll(listaClientes);
                    //the first element is the id of the client and the second is the state of connection, put it in the list
                    for (int i = 1; i < mensaje.length; i++) {
                        String[] cliente = mensaje[i].split("/");
                        listaClientes.add(new Cliente(cliente[0], Boolean.parseBoolean(cliente[1])));
                    }
                }

                if (mensaje[0].equals("MENSAJE")) {
                    Mensaje mensajeNuevo = new Mensaje(mensaje[1], mensaje[2]);


                    //Get the instance of the controller from the main class
                    ClienteGUIController controller = MainCliente.getController();
                    System.out.println(controller);

                    Platform.runLater(() -> {
                        controller.recargarMensajesEntrantes(mensajeNuevo);
                    });

                }



            } catch (IOException exception) {
                System.out.println("Fin de la ejecucion del thread");
            }

        }


    }





    private void mensajesConsola(String mensaje){
        System.out.println(mensaje);
    }


    public void agregarUsuarioAListaDeEnvio(String id) {

        for (Cliente cliente : listaClientes) {
            if (cliente.getId().equals(id)) {
                listaDinamicaClientes.add(cliente);
            }
        }
    }


    public void eliminarListaDeEnvio() {
        listaDinamicaClientes.clear();
    }

    public void enviarMensaje(String text) {
        //TOMAMOS TODOS LOS USUARIOS DE LA LISTA DE ENVIO Y ENVIAMOS EL MENSAJE A CADA UNO
        for (Cliente cliente : listaDinamicaClientes) {
            try {
                String mensaje = "MENSAJE-" +cliente.getId()+ "-" + text;
                byte[] data = mensaje.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(data,
                        data.length, InetAddress.getLocalHost(), 5000);
                socket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void desconectarUsuario(String id){
        //ENVIAR MENSAJE DE DESCONEXION AL SERVIDOR
        try {
            String mensaje = "DESCONECTAR-" + id;
            byte[] data = mensaje.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data,
                    data.length, InetAddress.getLocalHost(), 5000);
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
