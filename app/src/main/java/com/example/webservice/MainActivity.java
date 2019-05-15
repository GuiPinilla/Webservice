package com.example.webservice;

/*
    ATENÇÃO - MANTENHA O PACOTE DA LINHA SUPERIOR SEM ALTERAR
*/

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    LinearLayout grupoEntrada;
    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText entrada;
    Button btBsucar;
    ListView listViewAlunos;

    List<Aluno> listaAlunosAPI;
    ArrayList<String> listaNomeAlunos = new ArrayList<String>();

    ArrayAdapter<String> adapter;

    String tipoRequisicao = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();

        grupoEntrada = findViewById(R.id.grupoEntrada);
        entrada = findViewById(R.id.entrada);
        radioGroup = findViewById(R.id.opcoes);
        btBsucar = (Button) findViewById(R.id.btBuscar);
        listViewAlunos = findViewById(R.id.listViewAlunos);

        btBsucar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (tipoRequisicao.equals("ra")) {
                        listaAlunosAPI = new HttpService("ra",
                                entrada.getText().toString()).execute().get();

                    } else if (tipoRequisicao.equals("curso")) {
                        listaAlunosAPI = new HttpService("curso",
                                entrada.getText().toString()).execute().get();

                    } else {
                        listaAlunosAPI = new HttpService().execute().get();

                    }
                    entrada.setText("");

                    listaNomeAlunos.clear();
                    for (Aluno aluno : listaAlunosAPI) {
                        listaNomeAlunos.add(aluno.getNome());
                    }

                    adapter = new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1, listaNomeAlunos);

                    listViewAlunos.setAdapter(adapter);

                    grupoEntrada.setVisibility(GONE);


                    Toast.makeText(getApplicationContext(),
                            listaNomeAlunos.size() + " registros recuperado(s)",
                            Toast.LENGTH_LONG).show();

                } catch (InterruptedException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                } catch (ExecutionException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void checaOpcoes(View v) {

        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        tipoRequisicao = "";

        if (listaNomeAlunos.isEmpty() == false) {
            listaNomeAlunos.clear();
            adapter.notifyDataSetChanged();
        }

        if (radioButton.getText().equals("Por RA") || radioButton.getText().equals("Por Curso")) {
            grupoEntrada.setVisibility(VISIBLE);

            if (radioButton.getText().equals("Por RA")) {
                entrada.setHint("Digite o RA a ser pesquisado");
                tipoRequisicao = "ra";
            }

            if (radioButton.getText().equals("Por Curso")) {
                entrada.setHint("Digite o CURSO a ser pesquisado");
                tipoRequisicao = "curso";
            }

            if (radioButton.getText().equals("Todos")) {
                tipoRequisicao = "Todos";
            }

        } else {
            grupoEntrada.setVisibility(GONE);
        }
    }
}