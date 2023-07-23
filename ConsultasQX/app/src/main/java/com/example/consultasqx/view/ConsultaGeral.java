package com.example.consultasqx.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consultasqx.MapsActivity;
import com.example.consultasqx.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ConsultaGeral extends AppCompatActivity {

    TextView txtNome;
    TextView txtEspecialidade;
    TextView txtCrm;
    TextView txtData;
    TextView txtConvenio;

    String id;
    String id_medico;
    String nome, especialidade, crm, data, horario, convenio, nomeClinica;
    Double latitude, longitude;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_geral);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        id = (String) getIntent().getExtras().get("id_consulta");


        getConsultaData();

    }

    private void getConsultaData(){

        Toast.makeText(this, "Carregando dados da consulta...", Toast.LENGTH_SHORT).show();

        db.collection("Consulta").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();

                if (documentSnapshot != null && documentSnapshot.exists()){

                    nome = documentSnapshot.getString("nome_medico");
                    crm = documentSnapshot.getString("crm");
                    especialidade = documentSnapshot.getString("especialidade");
                    convenio = documentSnapshot.getString("convenio");
                    data = documentSnapshot.getString("data");
                    horario = documentSnapshot.getString("horario");
                    latitude = documentSnapshot.getDouble("latitude");
                    longitude = documentSnapshot.getDouble("longitude");
                    nomeClinica = documentSnapshot.getString("nome_clinica");


                    initComponentes();
                }

            }
        }).addOnFailureListener(e -> Toast.makeText(ConsultaGeral.this, "Erro: "+e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    public void initComponentes(){
        txtNome = findViewById(R.id.nomeMedico);
        txtEspecialidade = findViewById(R.id.especialidade);
        txtCrm= findViewById(R.id.crm);
        txtData = findViewById(R.id.data);
        txtConvenio = findViewById(R.id.convenio);


        txtNome.setText(nome);
        txtEspecialidade.setText(especialidade);
        txtCrm.setText(crm);
        String date = data+" - "+horario;
        txtData.setText(date);
        txtConvenio.setText(convenio);
    }

    public void abrirLocal(View view){
        Intent intent = new Intent(view.getContext(), MapsActivity.class);

        intent.putExtra("id_medico", id_medico);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("nome_clinica", nomeClinica);

        startActivity(intent);
    }

    public void voltarMedicoPerfil(View view){
        finish();
    }


}