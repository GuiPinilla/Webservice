package com.example.webservice;



/*
    ATENÇÃO - MANTENHA O PACOTE DA LINHA SUPERIOR SEM ALTERAR
*/

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HttpService extends AsyncTask<Void, Void, List<Aluno>> {
    private String stringUrl;

    public HttpService() {
        this.stringUrl = "sua_url/query_todos.php";
        Log.d("url", this.stringUrl);
    }

    public HttpService(String campo, String parametro) {
        if (campo == "ra") {
            this.stringUrl = "sua_url/query_ra.php?ra=" + parametro;
        } else {
            this.stringUrl = "sua_url/query_curso.php?curso=" + parametro;
        }
    }

    @Override
    protected List<Aluno> doInBackground(Void... voids) {

        StringBuilder resposta = new StringBuilder();
        try {
            URL url = new URL(stringUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("charset", "UFT-8");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.connect();

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                resposta.append(scanner.nextLine());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Type alunoType = new TypeToken<ArrayList<Aluno>>() {
        }.getType();
        List<Aluno> listaAlunos;
        listaAlunos = new Gson().fromJson(resposta.toString(), alunoType);
        return listaAlunos;
    }
}