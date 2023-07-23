package com.example.consultasqx.view;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.consultasqx.view.adapter.MedicoAdapter;
import com.example.consultasqx.R;
import com.example.consultasqx.model.Medico;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class SearchMedicoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MedicoAdapter adapter;
    SearchView searchView;
    ArrayList<Medico> listaMedicos = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medico);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        initRecyclerView();
        initSearchView();
        attLista();
    }

    public void initSearchView(){
        searchView = findViewById(R.id.search_view);
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
        recyclerView = findViewById(R.id.recyclerViewMedicos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void attLista(){

        db.collection("Medico").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document: task.getResult()) {
                    String id = (String) document.get("id");
                    String nome = (String) document.get("nome");
                    String crm = (String) document.get("crm");

                    Medico medico = new Medico(id, nome, crm);
                    listaMedicos.add(medico);
                }
                adapter = new MedicoAdapter(listaMedicos);
                recyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(e -> Toast.makeText(SearchMedicoActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    public void voltarHomePage(View view){
        finish();
    }

}















