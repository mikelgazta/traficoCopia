package com.example.traficoandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traficoandroid.R;
import com.example.traficoandroid.models.DataItem;
import java.util.ArrayList;
import java.util.List;

public class IncidenciasAdapter extends RecyclerView.Adapter<IncidenciasAdapter.IncidenciaViewHolder> {

    private List<DataItem> dataItems;

    // Constructor
    public IncidenciasAdapter() {
        this.dataItems = new ArrayList<>();
    }

    // Método para actualizar la lista de data items
    public void setData(List<DataItem> newDataItems) {
        dataItems.clear();
        dataItems.addAll(newDataItems);
        notifyDataSetChanged();
    }

    @Override
    public IncidenciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incidencia, parent, false);
        return new IncidenciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IncidenciaViewHolder holder, int position) {
        DataItem dataItem = dataItems.get(position);
        holder.bind(dataItem);
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    // ViewHolder class
    static class IncidenciaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tituloTextView;
        private final TextView descripcionTextView;

        public IncidenciaViewHolder(View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloIncidenciaTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionIncidenciaTextView);
        }

        public void bind(DataItem dataItem) {
            tituloTextView.setText(dataItem.getValueFromJson("titulo"));
            descripcionTextView.setText(dataItem.getValueFromJson("descripcion"));
            // Aquí puedes configurar otros datos en tu vista, según la estructura de tu DataItem
        }
    }
}

