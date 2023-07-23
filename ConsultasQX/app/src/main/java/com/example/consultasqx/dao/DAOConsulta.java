package com.example.consultasqx.dao;


import com.example.consultasqx.model.Consulta;
import com.example.consultasqx.model.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOConsulta {

    private DatabaseReference databaseReference;

    public DAOConsulta(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Consulta.class.getSimpleName());
    }

    public Task<Void> add(Consulta consulta){
        return databaseReference.push().setValue(consulta);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap){
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public void update(Consulta consulta){
        databaseReference.child("Consulta").child(consulta.getUid()).setValue(consulta);
    }

    public void remove(String id) {
        databaseReference.child("Consulta").child(id).removeValue();
    }

}
