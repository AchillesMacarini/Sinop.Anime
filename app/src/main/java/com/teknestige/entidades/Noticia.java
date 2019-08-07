package com.teknestige.entidades;

import java.util.Date;

public class Noticia {
    private String manchete;
    private String conteudo;
    private String data;


    public Noticia(){}

    public Noticia(String manchete, String conteudo, String data) {
        this.manchete = manchete;
        this.conteudo = conteudo;
        this.data = data;
    }

    public String getManchete() {
        return manchete;
    }

    public void setManchete(String manchete) {
        this.manchete = manchete;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
