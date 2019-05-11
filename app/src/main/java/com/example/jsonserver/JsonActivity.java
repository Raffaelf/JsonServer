package com.example.jsonserver;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownloadJsonAsyncTask().execute("http://10.7.4.25:3000/Pessoa");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Pessoa pessoa = (Pessoa) l.getAdapter().getItem(position);

        Intent intent = new Intent(this, InformacoesActivity.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }

    class DownloadJsonAsyncTask extends AsyncTask<String, Void, List<Pessoa>> {
        ProgressDialog dialog;

        //Exibe pop-up indicando que está sendo feito o download do JSON
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(JsonActivity.this, "Aguarde", "Fazendo download do JSON");
        }

        //Acessa o serviço do JSON e retorna a lista de pessoas
        @Override
        protected List<Pessoa> doInBackground(String... params) {
            String urlString = params[0];
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(urlString);
            try {
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String json = getStringFromInputStream(instream);

                    instream.close();
                    List<Pessoa> pessoas = getPessoas(json);
                    return pessoas;
                }
            } catch (Exception e) {
                Log.e("Erro", "Falha ao acessar Web service ", e);
            }
            return null;
        }

        //Retorna uma lista de pessoas com as informações retornadas do JSON
        private List<Pessoa> getPessoas(String jsonString) {
            List<Pessoa> pessoas = new ArrayList<Pessoa>();
            try {
                JSONArray pessoasJson = new JSONArray(jsonString);
                JSONObject pessoa;

                for (int i = 0; i < pessoasJson.length(); i++) {
                    pessoa = new JSONObject(pessoasJson.getString(i));
                    Log.i("PESSOA ENCONTRADA: ", "id=" + pessoa.getString("id"));

                    Pessoa objetoPessoa = new Pessoa();
                    objetoPessoa.setNome(pessoa.getString("name"));
                    objetoPessoa.setCpf(pessoa.getString("cpf"));
                    pessoas.add(objetoPessoa);
                }

            } catch (JSONException e) {
                Log.e("Erro", "Erro no parsing do JSON", e);
            }
            return pessoas;
        }

        //Depois de executada a chamada do serviço
        @Override
        protected void onPostExecute(List<Pessoa> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result.size() > 0) {
                ArrayAdapter<Pessoa> adapter = new ArrayAdapter<Pessoa>(JsonActivity.this, android.R.layout.simple_list_item_1, result);
                setListAdapter(adapter);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(JsonActivity.this)
                        .setTitle("Erro")
                        .setMessage("Não foi possível acessar as informações!!")
                        .setPositiveButton("OK", null);
                builder.create().show();
            }
        }

        //Converte objeto InputStream para String
        private String getStringFromInputStream(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }
    }
}