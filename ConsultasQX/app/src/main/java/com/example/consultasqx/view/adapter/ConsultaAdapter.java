package com.example.consultasqx.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.consultasqx.R;
import com.example.consultasqx.model.Consulta;
import com.example.consultasqx.view.ConsultaGeral;

import java.util.ArrayList;

public class ConsultaAdapter extends RecyclerView.Adapter<ConsultaAdapter.ViewHolder> implements Filterable {

    ArrayList<Consulta> consultaList;
    ArrayList<Consulta> consultasListFilter;

    public ConsultaAdapter(ArrayList<Consulta> consultaList){
        this.consultaList = consultaList;
        this.consultasListFilter = consultaList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nome;
        private final TextView especialidade;
        private final TextView tipo_consulta;
        private final TextView convenio;
        private final TextView horario;
        private final TextView data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.nome);
            especialidade = itemView.findViewById(R.id.especialidade);
            tipo_consulta = itemView.findViewById(R.id.tipo_consulta);
            convenio = itemView.findViewById(R.id.convenio);
            horario = itemView.findViewById(R.id.horario);
            data = itemView.findViewById(R.id.data);
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null || charSequence.length() == 0){
                    filterResults.values = consultasListFilter;
                    filterResults.count = consultasListFilter.size();
                }else{
                    String search = charSequence.toString().toLowerCase();
                    ArrayList<Consulta> consultas = new ArrayList<>();

                    for (Consulta consulta: consultasListFilter) {
                        if(consulta.getNomeMedico().toLowerCase().contains(search) || consulta.getEspecialidade().contains(search) ){
                            consultas.add(consulta);
                        }
                    }

                    filterResults.values = consultas;
                    filterResults.count = consultas.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                consultaList = (ArrayList<Consulta>) results.values;
                notifyDataSetChanged();
            }
        };

    }

    @NonNull
    @Override
    public ConsultaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_consulta_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultaAdapter.ViewHolder holder, int position) {

        holder.nome.setText(consultaList.get(position).getNomeMedico());
        holder.especialidade.setText(consultaList.get(position).getEspecialidade());
        holder.tipo_consulta.setText("Convenio: ");
        holder.convenio.setText(consultaList.get(position).getConvenio());
        holder.data.setText(consultaList.get(position).getData());
        holder.horario.setText(consultaList.get(position).getHorario());

        holder.itemView.setOnClickListener(view -> {
            Consulta consulta = consultaList.get(holder.getAdapterPosition());

            Intent intent = new Intent(view.getContext(), ConsultaGeral.class);

            intent.putExtra("id_medico", consulta.getMedico());
            intent.putExtra("id_consulta", consulta.getUid());

            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return consultaList.size();
    }


}