package com.droppages.pedrohenrique.meubanco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.analytics.FirebaseAnalytics;

public class Cadastrar extends AppCompatActivity {
    EditText txtNomeCadastro, txtLoginCadastro, txtSenhaCadastro, txtSenhaRepetir;
    Button btnCadastrar;
    DBOpenHelper helper = new DBOpenHelper(Cadastrar.this);
    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        // Recuperar template
        txtNomeCadastro = findViewById(R.id.txtNomeCadastro);
        txtLoginCadastro = findViewById(R.id.txtLoginCadastro);
        txtSenhaCadastro = findViewById(R.id.txtSenhaCadastro);
        txtSenhaRepetir = findViewById(R.id.txtSenhaRepetir);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        analytics = FirebaseAnalytics.getInstance(this);

        // Cadastrar usuário
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todosOsCamposPreenchidos()){

                    String nome = txtNomeCadastro.getText().toString().trim();
                    String login = txtLoginCadastro.getText().toString().trim();
                    String senha = txtSenhaCadastro.getText().toString().trim();

                    if (!repeteUsuario(login)){
                        if (senhasIguais()){
                            UsuarioDB user = new UsuarioDB();
                            user.setNome(nome);
                            user.setLogin(login);
                            user.setSenha(senha);

                            try {
                                Bundle bundle = new Bundle();
                                bundle.putString("acao", "novo_usuario");
                                bundle.putString("usuario", txtNomeCadastro.getText().toString());
                                analytics.logEvent("cadastro_de_usuario", bundle);
                            } catch (Exception e){
                                System.out.println(e.toString());
                            }

                            helper.novoUsuario(user);

                            Toast.makeText(Cadastrar.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                            limparCampos();
                        } else {
                            Toast.makeText(Cadastrar.this, "Senhas digitadas não conferem.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Cadastrar.this, "Erro ao cadastrar usuário, login cadastrado.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Cadastrar.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean todosOsCamposPreenchidos(){
        String nome = txtNomeCadastro.getText().toString().trim();
        String login = txtLoginCadastro.getText().toString().trim();
        String senha = txtSenhaCadastro.getText().toString().trim();
        String repete = txtSenhaRepetir.getText().toString().trim();

        if (nome.length() == 0 || login.length() == 0 || senha.length() == 0 || repete.length() == 0){
            return false;
        }

        return true;
    }

    private boolean senhasIguais(){
        String senha = txtSenhaCadastro.getText().toString().trim();
        String repete = txtSenhaRepetir.getText().toString().trim();
        return senha.equals(repete);
    }

    private void limparCampos(){
        txtNomeCadastro.setText("");
        txtLoginCadastro.setText("");
        txtSenhaCadastro.setText("");
        txtSenhaRepetir.setText("");
    }

    private boolean repeteUsuario(String login){
        return helper.repeteUsuario(login);
    }
}
