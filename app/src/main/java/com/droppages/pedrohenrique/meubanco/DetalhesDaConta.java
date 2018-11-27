package com.droppages.pedrohenrique.meubanco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class DetalhesDaConta extends AppCompatActivity {

    DBOpenHelper helper = new DBOpenHelper(DetalhesDaConta.this);
    Button btnSalvar;
    EditText txtValor;
    TextView lblBank;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_da_conta);
        btnSalvar = findViewById(R.id.btnSalvar);
        txtValor  = findViewById(R.id.txtValor);
        lblBank   = findViewById(R.id.lblBank);

        // Detalhes do usuario
        SharedPreferences preferences = getSharedPreferences("dadosusuario", Context.MODE_PRIVATE);
        final int idUsuario = preferences.getInt("id", 1);
        Intent intent = getIntent();
        final String banco = intent.getStringExtra("banco");

        lblBank.setText(banco);
        // Animação de entrada
        overridePendingTransition(R.anim.main_to_detail, R.anim.detail_to_main);

        // Preenchendo o spinner de escolha
        spinner = findViewById(R.id.spnAcao);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.acoes, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Registra movimentação
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarMovimentacao(idUsuario, banco);
            }
        });

    }

    private void cadastrarMovimentacao(int usuario, String banco){
        MovimentacaoDB mov = new MovimentacaoDB();
        float valor = Float.parseFloat(txtValor.getText().toString());
        String acao = spinner.getSelectedItem().toString();
        if (acao.equals("Saida")){ valor *= -1; }

        mov.setUsuario(usuario);
        mov.setBanco(idBanco(banco));
        mov.setValor(valor);
        helper.novaMovimentacao(mov);
        Toast.makeText(DetalhesDaConta.this, "Movimentação registrada", Toast.LENGTH_SHORT).show();
        txtValor.setText("");
    }

    private int idBanco(String banco){
        return helper.idPorBanco(banco);
    }

    /*
    private void guardaChart(){
        // Variaveis do template
        pieChart = (PieChart) findViewById(R.id.pieChart);

        // Configurações iniciais do gráfico
        pieChart.setUsePercentValues(true); // Valores percentual
        pieChart.getDescription().setEnabled(false); // Descrição
        pieChart.setExtraOffsets(5, 10, 5, 5); // Espécie de equilibrador de layout

        pieChart.setDragDecelerationFrictionCoef(0.95f); // Animação de giro do gráfico

        pieChart.setDrawHoleEnabled(false); // Margin interna no gráfico
        pieChart.setHoleColor(Color.WHITE); // Cor da margin interna
        pieChart.setTransparentCircleRadius(61f); // Transparência do radius a ser aplicado

        // Entrada de dados a serem inseridas no gráfico
        List<PieEntry> entradas = new ArrayList<PieEntry>();
        entradas.add(new PieEntry(34f, "Saque"));
        entradas.add(new PieEntry(44f, "Deposito"));
        entradas.add(new PieEntry(31f, "Transferência"));

        // Animação inicial
        pieChart.animateY(3000, Easing.EasingOption.EaseInOutCubic);

        // Formatação dos dados
        PieDataSet dataSet = new PieDataSet(entradas, "Movimentações");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // Transformando valores formatados em valores de PieChart
        PieData data = new PieData(dataSet);
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
    }
    */

}
