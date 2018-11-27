package com.droppages.pedrohenrique.meubanco;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MovimentacaoAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<MovimentacoesRealizadas> movimentacoes;

    public MovimentacaoAdapter(@NonNull Context context, ArrayList<MovimentacoesRealizadas> resource) {
        super(context, R.layout.linha_movimentacao, resource);
        this.context = context;
        this.movimentacoes = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.linha_movimentacao, parent, false);

        View back = view.findViewById(R.id.backg);
        TextView nome = view.findViewById(R.id.lblNomeBanco);
        TextView valor = view.findViewById(R.id.lblValor);
        TextView id = view.findViewById(R.id.lblIdMovimentacao);

        nome.setText(movimentacoes.get(position).getNome());
        id.setText(Integer.toString(movimentacoes.get(position).getId()));

        float valorMov = Float.parseFloat(movimentacoes.get(position).getValor());
        DecimalFormat df = new DecimalFormat("0.00");

        valor.setText(df.format(valorMov));

        if (valorMov < 0){
            back.setBackgroundResource(R.color.red_transparent);
        } else {
            back.setBackgroundResource(R.color.green_transparent);
        }

        return view;
    }

    private boolean par(int posicao){
        int divisao = posicao / 2;
        float resto = divisao * 2;
        resto = posicao - resto;

        return resto != 0;

    }
}
