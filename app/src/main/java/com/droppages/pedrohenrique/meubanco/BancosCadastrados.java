package com.droppages.pedrohenrique.meubanco;

public class BancosCadastrados {
    private String nome;
    private String saldo;

    public BancosCadastrados(String nome, String saldo){
        this.nome = nome;
        this.saldo = saldo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

}
