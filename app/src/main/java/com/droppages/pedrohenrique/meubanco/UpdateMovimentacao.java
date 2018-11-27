package com.droppages.pedrohenrique.meubanco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateMovimentacao extends AppCompatActivity {

    DBOpenHelper helper = new DBOpenHelper(this);
    EditText txtUpdateMov;
    Spinner spnUpdateMov;
    Button btnUpdateMov;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movimentacao);

        overridePendingTransition(R.anim.main_to_detail, R.anim.detail_to_main);

        // Recuperando template
        txtUpdateMov = findViewById(R.id.txtUpdateMov);
        btnUpdateMov = findViewById(R.id.btnUpdateMov);
        spnUpdateMov = findViewById(R.id.spnUpdateMov);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.acoes, android.R.layout.simple_spinner_dropdown_item);
        spnUpdateMov.setAdapter(adapter);

        Intent it = getIntent();
        id = Integer.parseInt(it.getStringExtra("id"));
    }

    public void update(View view){
        MovimentacaoDB db = new MovimentacaoDB();

        float valor = Float.parseFloat(txtUpdateMov.getText().toString());
        String acao = spnUpdateMov.getSelectedItem().toString();

        if (acao.equals("Saida")){ valor *= -1; }
        db.setId(id);
        db.setValor(valor);
        helper.updateMovimentacao(db);

        Toast.makeText(this, "Registro atualizado", Toast.LENGTH_SHORT).show();
        this.finish();
    }

}
