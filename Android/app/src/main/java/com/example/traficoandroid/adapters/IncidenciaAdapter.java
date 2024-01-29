package com.example.traficoandroid.adapters;

public class IncidenciaAdapter extends RecyclerView.Adapter<IncidenciaAdapter.ViewHolder> {
    private List<Incidencia> listaIncidencias;

    // Constructor
    public IncidenciaAdapter(List<Incidencia> listaIncidencias) {
        this.listaIncidencias = listaIncidencias;
    }

    // Resto de métodos del adaptador (onCreateViewHolder, onBindViewHolder, getItemCount, ViewHolder, etc.)

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Incidencia incidencia = listaIncidencias.get(position);

        // Establecer los datos en la vista
        holder.tituloTextView.setText(incidencia.getTitulo());
        holder.fechaTextView.setText(incidencia.getFecha());
        holder.descripcionTextView.setText(incidencia.getDescripcion());
    }

    // ViewHolder con las vistas
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tituloTextView;
        public TextView fechaTextView;
        public TextView descripcionTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            // Inicializar las vistas
            tituloTextView = itemView.findViewById(R.id.tituloIncidencia);
            fechaTextView = itemView.findViewById(R.id.fechaIncidencia);
            descripcionTextView = itemView.findViewById(R.id.descripcionIncidencia);
        }
    }
}
