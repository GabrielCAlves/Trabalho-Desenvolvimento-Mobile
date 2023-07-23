package com.example.consultasqx.view.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consultasqx.R;
import com.example.consultasqx.model.Medico;
import com.example.consultasqx.view.MedicoPerfil;

import java.util.ArrayList;

public class MedicoAdapter extends RecyclerView.Adapter<MedicoAdapter.ViewHolder> implements Filterable{

    ArrayList<Medico> medicosList;
    ArrayList<Medico> medicosListFilter;

    public MedicoAdapter(ArrayList<Medico> medicosList){
        this.medicosList = medicosList;
        this.medicosListFilter = medicosList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nome;
        private final TextView crm;

        public ViewHolder(View itemView) {

            super(itemView);

            nome = itemView.findViewById(R.id.nome);
            crm = itemView.findViewById(R.id.crm);
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null || charSequence.length() == 0){
                    filterResults.values = medicosListFilter;
                    filterResults.count = medicosListFilter.size();
                }else{
                    String search = charSequence.toString().toLowerCase();
                    ArrayList<Medico> medicos = new ArrayList<>();

                    for (Medico medico: medicosListFilter) {
                        if(medico.getNome().toLowerCase().contains(search)){
                            medicos.add(medico);
                        }
                    }

                    filterResults.values = medicos;
                    filterResults.count = medicos.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                medicosList = (ArrayList<Medico>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //cria o layout de cada linha (card)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_medico_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    //exibe as insformações na linha (card)
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nome.setText(medicosList.get(position).getNome());
        holder.crm.setText(medicosList.get(position).getCrm());

        holder.itemView.setOnClickListener(view -> {
            Medico medico = medicosList.get( holder.getAdapterPosition() );

            Intent intent = new Intent(view.getContext(), MedicoPerfil.class);

            intent.putExtra("id", medico.getId());

            view.getContext().startActivity(intent);
        });

    }

    public int getItemCount() {
        return medicosList.size();
    }


}
