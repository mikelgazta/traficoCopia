package com.example.traficoandroid.adapters;

public class CamarasAdapter extends RecyclerView.Adapter<CamarasAdapter.CamaraViewHolder> {

    private List<Camara> listaCamaras;

    public CamarasAdapter(List<Camara> listaCamaras) {
        this.listaCamaras = listaCamaras;
    }

    @NonNull
    @Override
    public CamaraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_camara, parent, false);
        return new CamaraViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CamaraViewHolder holder, int position) {
        Camara camara = listaCamaras.get(position);

        // Configurar los elementos del ViewHolder con los datos de la cámara
        holder.tituloCamara.setText(camara.getTitulo());
        holder.imagenCamara.setImageResource(camara.getImagenResId());

        // Puedes cargar la imagen desde una URL usando una biblioteca como Picasso o Glide

        // Otros elementos según sea necesario
    }

    @Override
    public int getItemCount() {
        return listaCamaras.size();
    }

    static class CamaraViewHolder extends RecyclerView.ViewHolder {
        TextView tituloCamara;
        ImageView imagenCamara;

        CamaraViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloCamara = itemView.findViewById(R.id.tituloCamara);
            imagenCamara = itemView.findViewById(R.id.imagenCamara);
        }
    }
}

