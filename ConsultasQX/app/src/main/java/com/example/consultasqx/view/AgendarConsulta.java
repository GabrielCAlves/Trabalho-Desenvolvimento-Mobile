package com.example.consultasqx.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.consultasqx.R;
import com.example.consultasqx.Util.ConfiguraBD;
import com.example.consultasqx.dao.DAOConsulta;
import com.example.consultasqx.model.Consulta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AgendarConsulta extends AppCompatActivity {

    Button btn_marcar_consulta;

    Spinner spinnerEspecialidades;
    Spinner spinnerHorarios;
    Spinner spinnerConvenios;
    Spinner spinnerDatas;

    ArrayList<Object> especialidades;
    ArrayList<Object> horarios;
    ArrayList<Object> convenios;
    ArrayList<Object> datas;

    ArrayAdapter<Object> adapterEspecialidades;
    ArrayAdapter<Object> adapterHorarios;
    ArrayAdapter<Object> adapterConvenios;
    ArrayAdapter<Object> adapterDatas;
    String nome, nome_clinica, crm, id;
    Double latitude, longitude;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    DAOConsulta daoConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_consulta);

        db = FirebaseFirestore.getInstance();
        auth = ConfiguraBD.FirebaseAutenticacao();
        daoConsulta = new DAOConsulta();

        id = (String) getIntent().getExtras().get("id_medico");

        getMedicoData();

    }

    private void getMedicoData(){

        Toast.makeText(this, "Carregando dados do médico...", Toast.LENGTH_SHORT).show();

        db.collection("Medico").document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();

                if (documentSnapshot != null && documentSnapshot.exists()){

                    nome = (String) documentSnapshot.get("nome");
                    crm = (String) documentSnapshot.get("crm");
                    nome_clinica = (String) documentSnapshot.get("nome_clinica");
                    latitude = documentSnapshot.getDouble("latitude");
                    longitude = documentSnapshot.getDouble("longitude");
                    datas = (ArrayList<Object>) documentSnapshot.get("datas");
                    horarios = (ArrayList<Object>) documentSnapshot.get("horarios");
                    convenios = (ArrayList<Object>) documentSnapshot.get("convenios");
                    especialidades = (ArrayList<Object>) documentSnapshot.get("especialidades");


                    initAdapters();
                    initSpinners();
                }

            }
        }).addOnFailureListener(e -> Toast.makeText(AgendarConsulta.this, "Erro: "+e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    public void initAdapters(){
        adapterEspecialidades = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, especialidades);
        adapterHorarios = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, horarios);
        adapterConvenios = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, convenios);
        adapterDatas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, datas);
    }

    public void initSpinners(){
        btn_marcar_consulta = findViewById(R.id.btn_marcar_consulta);
        spinnerEspecialidades = findViewById(R.id.spinner_especialidade);
        spinnerHorarios = findViewById(R.id.spinner_horario);
        spinnerConvenios = findViewById(R.id.spinner_convenio);
        spinnerDatas = findViewById(R.id.spinner_data);

        //inicializando spinner de especialidades
        spinnerEspecialidades.setAdapter(adapterEspecialidades);

        //inicializando spinner de horarios
        spinnerHorarios.setAdapter(adapterHorarios);

        //inicializando spinner de convenios
        spinnerConvenios.setAdapter(adapterConvenios);

        //inicializando spinner de datas
        spinnerDatas.setAdapter(adapterDatas);

    }

    public void voltar(View view){
        finish();
    }

    public boolean checkSpinnerEspecialidades() {
        String check;
        check = (String) spinnerEspecialidades.getSelectedItem();

        return check != null;
    }

    public boolean checkSpinnerHorarios(){
        String check;
        check = (String) spinnerHorarios.getSelectedItem();

        return check != null;
    }

    public boolean checkSpinnerData(){
        String check;
        check = (String) spinnerDatas.getSelectedItem();

        return check != null;
    }

    public void addConsulta( View view ){
        if(checkSpinnerData()){
            if (checkSpinnerEspecialidades()){
                if (checkSpinnerHorarios()){

                    Consulta consulta = new Consulta();
                    String uid = consulta.setUid(UUID.randomUUID().toString());

                    consulta.setPaciente(auth.getUid());
                    consulta.setMedico(id);
                    consulta.setNomeMedico(nome);
                    consulta.setConvenio((String) spinnerConvenios.getSelectedItem());
                    consulta.setEspecialidade((String) spinnerEspecialidades.getSelectedItem());
                    consulta.setHorario((String) spinnerHorarios.getSelectedItem());
                    consulta.setData(((String) spinnerDatas.getSelectedItem()));
                    consulta.setCrm(crm);
                    consulta.setNome_clinica(nome_clinica);
                    consulta.setLatitude(latitude);
                    consulta.setLongitude(longitude);

                    Map<String, Object> doc = new HashMap<>();
                    doc.put("id", uid);
                    doc.put("id_medico", consulta.getMedico());
                    doc.put("id_paciente", consulta.getPaciente());
                    doc.put("convenio", consulta.getConvenio());
                    doc.put("especialidade", consulta.getEspecialidade());
                    doc.put("horario", consulta.getHorario());
                    doc.put("nome_medico", consulta.getNomeMedico());
                    doc.put("crm", consulta.getCrm());
                    doc.put("data", consulta.getData());
                    doc.put("nome_clinica", consulta.getNome_clinica());
                    doc.put("latitude", consulta.getLatitude());
                    doc.put("longitude", consulta.getLongitude());

                    db.collection("Consulta").document(consulta.getUid()).set(doc)
                            .addOnCompleteListener(task -> {
                                Toast.makeText(AgendarConsulta.this, "Agendamento realizado", Toast.LENGTH_SHORT).show();
                                finish();
                            }).addOnFailureListener(e -> Toast.makeText(AgendarConsulta.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());

                }else{
                    Toast.makeText(this, "Selecione um horário", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Selecione uma especialidade", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Selecione uma data", Toast.LENGTH_SHORT).show();
        }

    }

}
