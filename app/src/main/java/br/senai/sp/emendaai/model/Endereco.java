package br.senai.sp.emendaai.model;

import com.google.gson.annotations.SerializedName;

public class Endereco {

    @SerializedName("cep")
    private String cep;

    @SerializedName("street")
    private String rua;

    @SerializedName("neighborhood")
    private String bairro;

    @SerializedName("city")
    private String cidade;

    @SerializedName("state")
    private String estado;

    public String getCep() {
        return cep;
    }

    public String getRua() {
        return rua;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }
}