package com.example.jsonserver.http;

import android.app.Person;

import com.example.jsonserver.Pessoa;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProdutoHttp {
    private String urllocal = "Pessoa/";
    private String URLProdutos = APIConfig.URL + urllocal;

    public String getProdutos(){

        String resposta = "";

        try {
            URL url = new URL(URLProdutos);

            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setReadTimeout(10 * 1000);
            conexao.setConnectTimeout(15 * 1000);
            conexao.setRequestMethod("GET");
            conexao.connect();

            int responseCode = conexao.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream is = conexao.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String linha = "";

                while ((linha = reader.readLine()) != null) {
                    resposta += linha;
                }
                return resposta;
            }

        } catch (Exception e) {
            System.out.println("erro: " + e.getMessage());
        }

        return resposta;
    }
}
