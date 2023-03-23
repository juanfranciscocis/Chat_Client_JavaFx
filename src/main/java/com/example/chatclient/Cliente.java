package com.example.chatclient;

public class Cliente {

    String id;
    Boolean conectado = false;

    public Cliente(String id, Boolean conectado){
        this.id = id;
        this.conectado = conectado;
    }

    public String getId(){
        return id;
    }

    public Boolean getConectado(){
        return conectado;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setConectado(Boolean conectado){
        this.conectado = conectado;
    }










}
