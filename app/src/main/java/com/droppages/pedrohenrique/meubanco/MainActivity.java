package com.droppages.pedrohenrique.meubanco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    DBOpenHelper helper = new DBOpenHelper(MainActivity.this);
    String login, senha;
    FirebaseAnalytics analytics;
    UsuarioDB usuario;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new inicio()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Dashboard()).commit();
                    return true;
                case R.id.navigation_notifications:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Banco()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation =   findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Transição de lauyout
        overridePendingTransition(R.anim.login_to_main, R.anim.main_to_login);
        frameLayout = findViewById(R.id.frameLayout);
        analytics = FirebaseAnalytics.getInstance(this);

        // Recebendo dados do login
        Intent it = getIntent();
        login = it.getStringExtra("login");
        senha = it.getStringExtra("senha");

        // Recebendo dados do usuarios / Boas vindas
        usuario = recebeUsuario(login, senha);
        adicionaSessao(usuario);
        Toast.makeText(this, "Olá " + usuario.getNome(), Toast.LENGTH_LONG).show();

        // Inicializando primeira atividade
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new inicio()).commit();
        }
    }

    private UsuarioDB recebeUsuario(String login, String senha){
        return helper.dadosPorLogin(login, senha);
    }

    private void adicionaSessao(UsuarioDB usuarioDB){
        SharedPreferences preferences = getSharedPreferences("dadosusuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("id", usuarioDB.getId());
        editor.putString("nome", usuarioDB.getNome());
        editor.putString("login", usuarioDB.getLogin());
        editor.putString("senha", usuarioDB.getSenha());

        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Bundle bundle = new Bundle();
            bundle.putString("login", "login");
            bundle.putString("usuario", usuario.getNome());
            analytics.logEvent("login", bundle);
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
