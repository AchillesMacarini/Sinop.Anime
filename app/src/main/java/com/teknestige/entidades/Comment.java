package com.teknestige.entidades;

public class Comment {
    private String conteudo;
    private String email;
    private String anime;
    private String id;

    public Comment(String conteudo , String email , String anime , String id) {
        this.conteudo = conteudo;
        this.email = email;
        this.anime = anime;
        this.id = id;
    }

    public Comment() {

    }


    public String getConteudo() {
        return conteudo;
    }

    public String getEmail() {
        return email;
    }

    public String getAnime() {
        return anime;
    }

    public String getIdCom() {
        return id;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public void setEmailCom(String email) {
        this.email = email;
    }

    public void setAnime(String anime) {
        this.anime = anime;
    }

    public void setIdCom(String id) {
        this.id = id;
    }

}
