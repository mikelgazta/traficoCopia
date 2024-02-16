package com.example.traficoandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traficoandroid.R;
import com.example.traficoandroid.models.DataItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CamarasAdapter extends RecyclerView.Adapter<CamarasAdapter.CamaraViewHolder> {

    private List<DataItem> dataItems;

    public void setData(List<DataItem> dataItems) {
        this.dataItems = dataItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CamaraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camara, parent, false);
        return new CamaraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CamaraViewHolder holder, int position) {
        DataItem dataItem = dataItems.get(position);
        holder.bind(dataItem);
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public static class CamaraViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloCamara;
        private ImageView imagenCamara;

        public CamaraViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloCamara = itemView.findViewById(R.id.tituloCamara);
            imagenCamara = itemView.findViewById(R.id.imagenCamara);
        }

        public void bind(DataItem dataItem) {
            // Parsear el JSON para obtener los datos relevantes de la cámara
            String titulo = dataItem.getValueFromJson("titulo");
            String urlImagen = dataItem.getValueFromJson("urlImagen");

            tituloCamara.setText(titulo);
            // Asegúrate de que la URL de la imagen no sea nula ni esté vacía
            if (urlImagen != null && !urlImagen.isEmpty()) {
                Picasso.get().load(urlImagen).into(imagenCamara);
            } else {
                // Manejar la ausencia de una URL de imagen válida
                // Puedes establecer una imagen predeterminada desde tus recursos drawable
                imagenCamara.setImageResource(R.drawable.ic_camera); // Reemplaza con tu imagen predeterminada
            }
        }
    }

}
