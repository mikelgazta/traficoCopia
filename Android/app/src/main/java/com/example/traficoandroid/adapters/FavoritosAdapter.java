package com.example.traficoandroid.adapters;

/*
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.traficoandroid.R;
import com.example.traficoandroid.models.DataItem;

import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.FavoritoViewHolder> {

    private List<DataItem> favoritos;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onToggleFavorito(DataItem item);
    }

    public FavoritosAdapter(Context context, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setFavoritos(List<DataItem> favoritos) {
        this.favoritos = favoritos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_favorito, parent, false);
        return new FavoritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritoViewHolder holder, int position) {
        holder.bind(favoritos.get(position));
    }

    @Override
    public int getItemCount() {
        return favoritos != null ? favoritos.size() : 0;
    }

    class FavoritoViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloView;
        private TextView descripcionView;
        private ImageView starView;

        FavoritoViewHolder(View itemView) {
            super(itemView);
            tituloView = itemView.findViewById(R.id.tituloView);
            descripcionView = itemView.findViewById(R.id.descripcionView);
            starView = itemView.findViewById(R.id.starView);
        }

        void bind(DataItem item) {
            tituloView.setText(item.getTitle());
            descripcionView.setText(item.getDescription());

            // Configurar la estrella para el estado de favorito
            starView.setImageResource(item.isFavorito() ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);

            starView.setOnClickListener(v -> {
                // Cambiar el estado de favorito cuando se toca la estrella
                item.setFavorito(!item.isFavorito());
                notifyItemChanged(getAdapterPosition());
                listener.onToggleFavorito(item);
            });
        }
    }
}*/

