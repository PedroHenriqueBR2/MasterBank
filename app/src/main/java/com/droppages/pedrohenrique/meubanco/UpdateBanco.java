package com.droppages.pedrohenrique.meubanco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateBanco extends AppCompatActivity {

    EditText txtUpdateBanco;
    Button btnUpdateBanco;
    DBOpenHelper helper = new DBOpenHelper(this);
    int usuario;
    String bancoIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_banco);

        overridePendingTransition(R.anim.main_to_detail, R.anim.detail_to_main);

        // Recuperando template
        txtUpdateBanco = findViewById(R.id.txtUpdateBanco);
        btnUpdateBanco = findViewById(R.id.btnUpdateBanco);

        SharedPreferences preferences = getSharedPreferences("dadosusuario", Context.MODE_PRIVATE);
        usuario = preferences.getInt("id", 1);

        Intent it = getIntent();
        bancoIt = it.getStringExtra("banco");

        btnUpdateBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUpdateBanco.getText().toString().trim().length() == 0){
                    Toast.makeText(UpdateBanco.this, "Preencha o campo antes de salvar", Toast.LENGTH_SHORT).show();
                } else {
                    salvarAlteracoes();
                }
            }
        });

    }

    private void salvarAlteracoes(){
        BancoDB banco = new BancoDB();
        banco.setId(idDoBanco(bancoIt));
        banco.setNome(txtUpdateBanco.getText().toString().trim());
        helper.updateBanco(banco);

        Toast.makeText(this, "Dados atualizados", Toast.LENGTH_SHORT).show();

        this.finish();
    }

    private int idDoBanco(String banco){
        return helper.idPorBanco(banco);
    }
}
