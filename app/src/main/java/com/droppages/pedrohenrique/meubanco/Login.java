package com.droppages.pedrohenrique.meubanco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Login extends AppCompatActivity {

    Button btnCadastrar, btnLogin;
    EditText login, senha;
    DBOpenHelper helper = new DBOpenHelper(Login.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Recuperando template
        login = findViewById(R.id.loginEntrar);
        senha = findViewById(R.id.senhaEntrar);

        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnLogin = findViewById(R.id.btnLogin);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Cadastrar.class);
                startActivity(it);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String l = login.getText().toString().trim();
                String s = senha.getText().toString().trim();

                if (l.length() == 0 || s.length() == 0) {
                    Toast.makeText(Login.this, "Preencha todos os campos antes de continuar", Toast.LENGTH_SHORT).show();
                } else {
                    if (helper.loginUsuario(l, s)) {
                        Intent it = new Intent(getApplicationContext(), MainActivity.class);
                        it.putExtra("login", l);
                        it.putExtra("senha", s);
                        startActivity(it);
                    } else {
                        Toast.makeText(Login.this, "Usuário e/ou senha incorreto(s).", Toast.LENGTH_SHORT).show();
                    }
                    limparCampos();
                }
            }
        });
    }

    private void limparCampos() {
        login.setText("");
        senha.setText("");
    }
}