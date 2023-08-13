package com.example.consultasqx.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.consultasqx.R;
import com.example.consultasqx.Util.ConfiguraBD;
import com.example.consultasqx.model.Consulta;
import com.example.consultasqx.view.adapter.ConsultaAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.Objects;

public class HistoricoConsultas extends AppCompatActivity {

    RecyclerView recyclerView;
    ConsultaAdapter adapter;
    SearchView searchView;
    ArrayList<Consulta> listaConsultas = new ArrayList<>();

    private FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_consultas);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        auth = ConfiguraBD.FirebaseAutenticacao();

        initRecyclerView();
        initSearchView();
        attLista();
    }

    public void initSearchView(){
        searchView = findViewById(R.id.search_view_consulta);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerViewConsultas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void attLista(){

        db.collection("Consulta").whereEqualTo("id_paciente", auth.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id = (String) document.get("id");
                    String nome = (String) document.get("nome_medico");
                    String especialidade = (String) document.get("especialidade");
                    String convenio = (String) document.get("convenio");
                    String data = (String) document.get("data");
                    String horario = (String) document.get("horario");


                    Consulta consulta = new Consulta(id, nome, especialidade, convenio, data, horario);
                    listaConsultas.add(consulta);
                }
                adapter = new ConsultaAdapter(listaConsultas);
                recyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(e -> Toast.makeText(HistoricoConsultas.this, "Erro: "+e.getMessage(), Toast.LENGTH_SHORT).show());


    }

    public void voltarHomePage(View v){
        finish();
    }
}
