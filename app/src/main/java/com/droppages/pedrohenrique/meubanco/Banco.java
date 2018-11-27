package com.droppages.pedrohenrique.meubanco;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Banco extends Fragment {

    private EditText txtBanco;
    DBOpenHelper helper = new DBOpenHelper(this.getActivity());

    // Construtor
    public Banco() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inicializa OpenHelper
        helper = new DBOpenHelper(this.getActivity());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_banco, container, false);
        txtBanco = view.findViewById(R.id.txtBanco);
        Button btnCadastrarBanco = view.findViewById(R.id.btnCadastrarBanco);

        // Recuperar dados do usuário
        SharedPreferences preferences = this.getActivity().getSharedPreferences("dadosusuario", Context.MODE_PRIVATE);
        final int idUsuario = preferences.getInt("id", 1);

        // Cadastrar novo bancoIt
        btnCadastrarBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarNovoBanco(idUsuario);
            }
        });

        return view;
    }

    private void cadastrarNovoBanco(int usuario){
        String nome = txtBanco.getText().toString();

        if (!repeteBanco(nome)){
            BancoDB banco = new BancoDB();

            banco.setNome(nome);
            banco.setUsuario(usuario);

            helper.novoBanco(banco);
            Toast.makeText(this.getActivity(), "Banco cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            txtBanco.setText("");
        } else {
            Toast.makeText(getActivity(), "Erro ao cadastrar banco, banco já cadastrado.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean repeteBanco(String banco){return helper.repeteBanco(banco);}

}
