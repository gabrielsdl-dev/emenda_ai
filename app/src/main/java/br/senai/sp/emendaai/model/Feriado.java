package br.senai.sp.emendaai.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Feriado implements Serializable {
    @SerializedName("date")
    private String data;
    @SerializedName("name")
    private String nome;
    @SerializedName("type")
    private String tipo;

    public Feriado(){}

    public Feriado(String data, String nome, String tipo){
        this.data = data;
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
