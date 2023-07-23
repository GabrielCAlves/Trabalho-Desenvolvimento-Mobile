package com.example.consultasqx.dao;

import com.example.consultasqx.model.Medico;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOMedico {

    private DatabaseReference databaseReference;

    public DAOMedico(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Medico.class.getSimpleName());
    }

}
