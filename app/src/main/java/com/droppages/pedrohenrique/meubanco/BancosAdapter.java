package com.droppages.pedrohenrique.meubanco;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BancosAdapter extends ArrayAdapter<BancosCadastrados> {
    private Context context;
    private ArrayList<BancosCadastrados> bancos;

    public BancosAdapter(@NonNull Context context, ArrayList<BancosCadastrados> resource) {
        super(context, R.layout.linha_banco, resource);
        this.context = context;
        this.bancos = resource;
    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // super.getView(position, convertView, parent);

        View view = inflater.inflate(R.layout.linha_banco, parent, false);

        View linha = view.findViewById(R.id.linha);
        TextView banco = view.findViewById(R.id.lblBanco);
        TextView saldo = view.findViewById(R.id.lblSaldo);


        if (par(position)){
            linha.setBackgroundResource(R.color.lista_variante);
        }

        float s = Float.parseFloat(bancos.get(position).getSaldo());
        DecimalFormat df = new DecimalFormat("0.00");

        if (s < 0 && s > -0.01){
            saldo.setText("0,00");
        } else {
            saldo.setText(df.format(s));
        }

        banco.setText(bancos.get(position).getNome());

        return view;
    }

    private boolean par(int posicao){
        int divisao = posicao / 2;
        float resto = divisao * 2;
        resto = posicao - resto;

        return resto != 0;

    }
}
