package com.example.consultasqx.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consultasqx.MapsActivity;
import com.example.consultasqx.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class MedicoPerfil extends AppCompatActivity {

    private TextView txtNome;
    private TextView txtCrm;
    private Button btn_horarios;

    private ListView lista_especialidades;
    private ListView lista_convenios;

    private String nome, crm, nomeClinica;
    private Double latitude, longitude;
    private ArrayList<Object> especialidades;
    private ArrayList<Object> convenios;

    ArrayAdapter<Object> adapterEspecialidades;
    ArrayAdapter<Object> adapterConvenios;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_perfil);

        id = (String) getIntent().getExtras().get("id");

        getMedicoData();
    }

    private void getMedicoData(){

        Toast.makeText(this, "Carregando dados do médico...", Toast.LENGTH_SHORT).show();

        db.collection("Medico").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();

                if (documentSnapshot != null && documentSnapshot.exists()){

                    nome = documentSnapshot.getString("nome");
                    crm = documentSnapshot.getString("crm");
                    latitude = documentSnapshot.getDouble("latitude");
                    longitude = documentSnapshot.getDouble("longitude");
                    nomeClinica = documentSnapshot.getString("nome_clinica");
                    convenios = (ArrayList<Object>) documentSnapshot.get("convenios");
                    especialidades = (ArrayList<Object>) documentSnapshot.get("especialidades");

                    initAdapters();
                    initComponentes();
                }

            }
        }).addOnFailureListener(e -> Toast.makeText(MedicoPerfil.this, "Erro: "+e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    public void verHorarios(View view){
        Intent intent = new Intent(view.getContext(), AgendarConsulta.class);

        intent.putExtra("id_medico", id);


        startActivity(intent);

    }

    public void initAdapters(){
        adapterEspecialidades = new ArrayAdapter<>(this, R.layout.list_item_layout, especialidades);
        adapterConvenios = new ArrayAdapter<>(this, R.layout.list_item_layout, convenios);

    }

    public void initComponentes(){
        txtNome = findViewById(R.id.nome);
        txtCrm = findViewById(R.id.crm);
        lista_especialidades = findViewById(R.id.list_especialidades);
        lista_convenios = findViewById(R.id.list_convenios);
        btn_horarios = findViewById(R.id.verHorarios);


        txtNome.setText(nome);
        lista_especialidades.setAdapter(adapterEspecialidades);
        lista_convenios.setAdapter(adapterConvenios);
        txtCrm.setText(crm);
    }

    public void abrirLocal(View view){
        Intent intent = new Intent(view.getContext(), MapsActivity.class);

        intent.putExtra("id_medico", id);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("nome_clinica", nomeClinica);

        startActivity(intent);
    }

    public void voltarMedicoPerfil(View view){
        finish();
    }
}