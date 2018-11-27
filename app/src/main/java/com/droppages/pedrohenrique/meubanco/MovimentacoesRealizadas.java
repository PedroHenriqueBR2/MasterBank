package com.droppages.pedrohenrique.meubanco;

public class MovimentacoesRealizadas {
    private int id;
    private String nome;
    private String valor;

    public MovimentacoesRealizadas(int id, String nome, String valor){
        this.nome = nome;
        this.valor = valor;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
