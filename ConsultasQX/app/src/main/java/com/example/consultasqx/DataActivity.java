package com.example.consultasqx;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataActivity extends AppCompatActivity {

    private TextView text1, text2;
    private Button buttonData, buttonTempo, buttonVoltar;

    //private ArrayList<String> selectedDates = new ArrayList<>();
    //private ArrayAdapter<String> datesAdapter;

    private ArrayList<String> selectedDates;
    private ArrayList<ArrayList<String>> data_hora;
    private ArrayList<DateTimeEntry> dataHoraEntries = new ArrayList<>();

    private LinearLayout dateTimeListLayout;
    String horario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        text1 = findViewById(R.id.showText1);
        text2 = findViewById(R.id.showText2);
        buttonData = findViewById(R.id.buttonData);
        buttonTempo = findViewById(R.id.buttonTempo);
        buttonVoltar = findViewById(R.id.buttonVoltar);

        dateTimeListLayout = findViewById(R.id.dateTimeListLayout);
        //datesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, selectedDates);

        //selectedDates = new ArrayList<>();

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
