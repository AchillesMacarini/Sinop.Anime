package com.teknestige.entidades;

import java.util.Date;


public class Anime {
    private String nome;
    private String status;
    private String estudio;
    private String diretor;
    private String sinopse;
    private Date data_lan;
    private int num_ep;
    private String classificacao;

    public Anime(){    }


    public Anime(String nome, String status, String estudio, String diretor, String sinopse, Date data_lan, int num_ep, String classificacao) {
        this.nome = nome;
        this.status = status;
        this.estudio = estudio;
        this.diretor = diretor;
        this.sinopse = sinopse;
        this.data_lan = data_lan;
        this.num_ep = num_ep;
        this.classificacao = classificacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public Date getData_lan() {
        return data_lan;
    }

    public void setData_lan(Date data_lan) {
        this.data_lan = data_lan;
    }

    public int getNum_ep() {
        return num_ep;
    }

    public void setNum_ep(int num_ep) {
        this.num_ep = num_ep;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }



}
