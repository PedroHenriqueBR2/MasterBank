package com.droppages.pedrohenrique.meubanco;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class inicio extends Fragment {

    DBOpenHelper helper = new DBOpenHelper(this.getActivity());
    TextView saldoTotal;
    ListView lstDados;
    int idUsuario;

    public inicio() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences preferences = this.getActivity().getSharedPreferences("dadosusuario", Context.MODE_PRIVATE);
        idUsuario = preferences.getInt("id", 1);

        // Inicializa OpenHelper
        helper = new DBOpenHelper(this.getActivity());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        lstDados = view.findViewById(R.id.lvBancos);

        saldoTotal = (TextView) view.findViewById(R.id.saldoTotal);
        preencherLista();

        // Clique no item
        lstDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v = view;
                TextView a = v.findViewById(R.id.lblBanco);
                String banco = a.getText().toString();

                Intent intent = new Intent(getContext(), DetalhesDaConta.class);
                intent.putExtra("banco", banco);
                startActivity(intent);
            }
        });

        lstDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                View v = view;
                TextView label = v.findViewById(R.id.lblBanco);
                final String texto = label.getText().toString();


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Você deseja alterar o nome do banco?");
                //define um botão como positivo
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent it = new Intent(getActivity(), UpdateBanco.class);
                        it.putExtra("banco", texto);
                        startActivity(it);
                    }
                });
                //define um botão como negativo.
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alerta = builder.create();
                alerta.show();

                return true;
            }
        });

        return view;
    }

    private void preencherLista(){
        // Lista
        ArrayAdapter adapter = new BancosAdapter(this.getActivity(), preencheBancos(idUsuario));
        lstDados.setAdapter(adapter);
        CalculaSaldoTotal(idUsuario);
    }

    private ArrayList<BancosCadastrados> preencheBancos(int usuario){
        ArrayList<BancosCadastrados> lista = new ArrayList<>();
        ArrayList<BancoDB> banco = helper.todosOsBancos(usuario);
        BancosCadastrados bc;

        for (int i = 0; i < banco.size(); i++){
            BancoDB b = banco.get(i);
            bc = new BancosCadastrados(b.getNome(), Float.toString(calculaSaldo(b.getId(), b.getUsuario())));
            lista.add(bc);
        }

        return lista;
    }

    private float calculaSaldo(int banco, int usuario){
        return helper.calculaSaldoDeBanco(banco, usuario);
    }

    private void CalculaSaldoTotal(int usuario){
        float saldo = helper.saldoTotal(usuario);
        if (saldo < 0){
            saldoTotal.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        } else {
            saldoTotal.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        DecimalFormat df = new DecimalFormat("0.00");

        String saldoFormat = df.format(saldo);

        if (saldo < 0 && saldo > -0.01){
            saldoTotal.setText("0,00");
        } else {
            saldoTotal.setText(saldoFormat);
        }
    }

    @Override
    public void onResume() {
        preencherLista();
        super.onResume();
    }
}
