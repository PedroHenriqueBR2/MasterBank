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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Dashboard extends Fragment {

    DBOpenHelper helper = new DBOpenHelper(this.getActivity());
    ListView lstMovimentacao;
    int idUsuario;

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        helper = new DBOpenHelper(this.getActivity());
        lstMovimentacao = view.findViewById(R.id.lstMovimentacao);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("dadosusuario", Context.MODE_PRIVATE);
        idUsuario = preferences.getInt("id", 1);

        lstMovimentacao.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                View v = view;
                TextView label = v.findViewById(R.id.lblIdMovimentacao);
                final String idMov = label.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Você deseja modificar o registro selecionado?");
                //define um botão como positivo
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent it = new Intent(getActivity(), UpdateMovimentacao.class);
                        it.putExtra("id", idMov);
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

        preencherLista();
        return view;
    }

    private void preencherLista(){
        ArrayAdapter adapter1 = new MovimentacaoAdapter(this.getActivity(), preencheLista(idUsuario));
        lstMovimentacao.setAdapter(adapter1);
    }


    private ArrayList<MovimentacoesRealizadas> preencheLista(int usuario){
        ArrayList<MovimentacoesRealizadas> lista = new ArrayList<>();
        ArrayList<MovimentacoesRealizadas> mov = helper.todasAsMovimentacoes(usuario);

        for (MovimentacoesRealizadas caso: mov){
            lista.add(caso);
        }

        return lista;

    }

    private int idBanco(String banco){
        return helper.idPorBanco(banco);
    }

    @Override
    public void onResume() {
        super.onResume();
        preencherLista();
    }
}
