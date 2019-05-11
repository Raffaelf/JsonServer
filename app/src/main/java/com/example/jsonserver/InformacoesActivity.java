package com.example.jsonserver;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InformacoesActivity extends Activity {

    private TextView txtNome;
    private TextView txtCpf;
    private Pessoa pessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);

        pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtCpf = (TextView) findViewById(R.id.txtCpf);

        txtNome.setText(pessoa.getNome());
        txtCpf.setText(pessoa.getCpf());

    }

}