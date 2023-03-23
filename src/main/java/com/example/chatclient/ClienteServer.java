package com.example.chatclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClienteServer {

    private DatagramSocket socket;
    private Cliente cliente;

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    public ClienteServer() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Cliente> getListaClientes() {
        return listaClientes;
    }

    public Cliente getCliente(){
        return cliente;
    }

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

    private void mensajesConsola(String mensaje){
        System.out.println(mensaje);
    }




}
