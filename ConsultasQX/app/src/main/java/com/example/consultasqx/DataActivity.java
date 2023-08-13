package com.example.consultasqx;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DataActivity extends AppCompatActivity {

    private TextView text1, text2;
    private Button buttonData, buttonTempo, buttonLancarDados, buttonRemoverDados, buttonVoltar;

    //private ArrayList<String> selectedDates = new ArrayList<>();
    //private ArrayAdapter<String> datesAdapter;

    private ArrayList<String> selectedDates;
    private ArrayList<ArrayList<String>> data_hora;
    private ArrayList<DateTimeEntry> dataHoraEntries = new ArrayList<>();

    private LinearLayout dateTimeListLayout;
    String horario = "", key;

    FirebaseAuth autenticacao;
    private FirebaseFirestore db;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        text1 = findViewById(R.id.showText1);
        text2 = findViewById(R.id.showText2);
        buttonData = findViewById(R.id.buttonData);
        buttonTempo = findViewById(R.id.buttonTempo);
        buttonVoltar = findViewById(R.id.buttonVoltar);

        buttonLancarDados = findViewById(R.id.buttonLancarDados);
        buttonRemoverDados = findViewById(R.id.buttonRemoverDados);

        dateTimeListLayout = findViewById(R.id.dateTimeListLayout);
        //datesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, selectedDates);

        //selectedDates = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        key = user.getUid();

        db = FirebaseFirestore.getInstance();

        intent = getIntent();

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTheme(R.style.CustomMaterialDatePickerTheme); // Aqui, defina o tema personalizado

        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();

        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "Material_Range");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        long startDateMillis = selection.first;
                        long endDateMillis = selection.second;

                        selectedDates = new ArrayList<>();
                        Calendar calendar = Calendar.getInstance();

                        calendar.setTimeInMillis(startDateMillis);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                        while (calendar.getTimeInMillis() <= endDateMillis) {
                            selectedDates.add(dateFormat.format(calendar.getTime()));
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                        }

                        selectedDates.add(dateFormat.format(calendar.getTime())); // Adiciona a data final
                        selectedDates.remove(0);

                        // Now you have an ArrayList<String> selectedDates with the selected date range
                        // You can use this ArrayList as needed

                        // Update the UI or perform other actions here
                        text1.setGravity(2);
                        updateUI(selectedDates);
                    }
                });
            }
        });

        buttonTempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimeDialog();
            }
        });

        buttonLancarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUIConsulta(user);
            }
        });

        /*buttonRemoverDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });*/

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void updateUI(ArrayList<String> selectedDates) {
        StringBuilder datesText = new StringBuilder();
        for (String date : selectedDates) {
            datesText.append(date).append("\n");
        }
        text1.setText(datesText.toString());
    }

    private void updateUIConsulta(FirebaseUser user) {
        if (user != null) {

            ArrayList<String> datas = new ArrayList<>();
            ArrayList<String> horarios = new ArrayList<>();

            for(int i = 0; i < dataHoraEntries.size(); ++i){
                if(!datas.contains(dataHoraEntries.get(i).getDate())){
                    datas.add(dataHoraEntries.get(i).getDate());
                }
                if(!horarios.contains(dataHoraEntries.get(i).getTime())){
                    horarios.add(dataHoraEntries.get(i).getTime());
                }
            }

            Map<String, Object> doc = new HashMap<>();
            doc.put("convenios", intent.getStringArrayListExtra("convenios"));
            doc.put("cpf", intent.getStringExtra("cpf"));
            doc.put("crm", intent.getStringExtra("crm"));
            doc.put("datas", datas);
            doc.put("especialidades", intent.getStringArrayListExtra("especialidades"));
            doc.put("horarios", horarios);
            doc.put("id", user.getUid());
            doc.put("latitude", Double.parseDouble(intent.getStringExtra("latitude")));
            doc.put("longitude", Double.parseDouble(intent.getStringExtra("longitude")));
            doc.put("nome", intent.getStringExtra("nome"));
            doc.put("nome_clinica", intent.getStringExtra("nome_clinica"));

            DocumentReference docRef = db.collection("Medico").document(key);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // O documento existe, então você pode atualizar os dados aqui
                            // Por exemplo, você pode usar docRef.update(...) para atualizar campos específicos
                            db.collection("Medico").document(key)
                                    .update("nome", intent.getStringExtra("nome"),
                                            "cpf", intent.getStringExtra("cpf"),
                                            "nome_clinica", intent.getStringExtra("nome_clinica"),
                                            "latitude", Double.parseDouble(intent.getStringExtra("latitude")),
                                            "longitude", Double.parseDouble(intent.getStringExtra("longitude")),
                                            "datas", datas,
                                            "horarios", horarios)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(DataActivity.this, "Dados atualizados", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DataActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // O documento não existe, então você pode adicionar os dados aqui
                            // Por exemplo, você pode usar db.collection("Usuarios").document(userId).set(...) para adicionar o documento
                            db.collection("Medico").document(user.getUid()).set(doc)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(DataActivity.this, "Agenda adicionada", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DataActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "Falha ao verificar o documento: ", task.getException());
                    }
                }
            });
        } else {
            Toast.makeText(this, "Usuário nulo", Toast.LENGTH_SHORT).show();
        }


    }

    public void delete(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DataActivity.this);

        builder.setTitle("Tem certeza de que deseja excluir sua agenda do banco de dados?");
        builder.setCancelable(false);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                apagarAgenda();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        builder.show();

    }

    private void apagarAgenda(){
        db.collection("Medico").document(key)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DataActivity.this, "Agenda excluída com sucesso", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DataActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openTimeDialog(){
            TimePickerDialog timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int horas, int minutos) {
                    if(horas < 10 && minutos < 10){
                        horario = "0"+String.valueOf(horas)+":0"+String.valueOf(minutos);
                        text2.setText(horario);
                    }else if(minutos < 10){
                        horario = String.valueOf(horas)+":0"+String.valueOf(minutos);
                        text2.setText(horario);
                    }else if(horas < 10){
                        horario = "0"+String.valueOf(horas)+":"+String.valueOf(minutos);
                        text2.setText(horario);
                    }else{
                        horario = String.valueOf(horas)+":"+String.valueOf(minutos);
                        text2.setText(horario);
                    }
                    /*for(int i = 0; i < selectedDates.size(); ++i){
                        data_hora.add(selectedDates.get(i));
                    }
                    data_hora.add(selectedDates.add(horario));*/

                    DateTimeEntry entry;
                    for(int i = 0; i < selectedDates.size(); ++i){
                        entry = new DateTimeEntry(selectedDates.get(i), horario);
                        dataHoraEntries.add(entry);
                        addDateTimeEntryView(entry);
                    }
                    //addDateTimeEntryView(entry);
                    //DateTimeEntry entry = new DateTimeEntry(selectedDates.get(0), horario);
                    //dataHoraEntries.add(entry);
                }
            }, 00, 00, true);

            timeDialog.show();
    }

    private void addDateTimeEntryView(DateTimeEntry entry) {
        View entryView = getLayoutInflater().inflate(R.layout.datetime_entry_item, null);

        TextView dateTimeTextView = entryView.findViewById(R.id.dateTimeTextView);
        Button removeButton = entryView.findViewById(R.id.removeButton);

        String dateTimeText = entry.getDate() + "    " + entry.getTime();
        dateTimeTextView.setText(dateTimeText);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataHoraEntries.remove(entry);
                dateTimeListLayout.removeView(entryView);
            }
        });

        dateTimeListLayout.addView(entryView);
    }

    /*private void removeSelectedDateTime() {
        // Aqui você pode implementar a lógica para remover o item selecionado da lista
    }*/
}
