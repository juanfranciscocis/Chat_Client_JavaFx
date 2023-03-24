package com.example.chatclient;

public class Mensaje {

    public String idEnvio;
    public String mensaje;


    public Mensaje(String idEnvio, String mensaje) {
        this.idEnvio = idEnvio;
        this.mensaje = mensaje;
    }

    public String getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
