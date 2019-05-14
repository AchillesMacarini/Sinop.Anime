package com.teknestige.entidades;

import android.os.Parcel;
import android.os.Parcelable;


public class Usuario{

    private String email;
    private String nickname;
    private String biograph;
    private String senha;
    private String dataCadastro;
    private Integer qtdTags;

    public Usuario() {}

    public Usuario(String email, String nickname, String biograph, String senha, String dataCadastro, Integer qtdTags) {
        this.email = email;
        this.nickname = nickname;
        this.biograph = biograph;
        this.senha = senha;
        this.dataCadastro = dataCadastro;
        this.qtdTags = qtdTags;
    }

    protected Usuario(Parcel in) {
        email = in.readString();
        nickname = in.readString();
        biograph = in.readString();
        dataCadastro = in.readString();
        if (in.readByte() == 0) {
            qtdTags = null;
        } else {
            qtdTags = in.readInt();
        }
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBiograph() {
        return biograph;
    }

    public void setBiograph(String biograph) {
        this.biograph = biograph;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Integer getQtdTags() {
        return qtdTags;
    }

    public void setQtdTags(Integer qtdTags) {
        this.qtdTags = qtdTags;
    }


    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(nickname);
        parcel.writeString(biograph);
        parcel.writeString(dataCadastro);
        if (qtdTags == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(qtdTags);
        }
    }

}
