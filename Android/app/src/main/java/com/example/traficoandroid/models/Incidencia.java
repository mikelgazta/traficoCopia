package com.example.traficoandroid.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Incidencia {
    private long id;
    private String tipo;
    private String causa;
    private Date comienzo;
    private String nvlIncidencia;
    private String carretera;
    private String direccion;
    private String latitud;
    private String longitud;
    private String usuario;

    // Constructor
    public Incidencia(long id, String tipo, String causa, Date comienzo, String nvlIncidencia,
                      String carretera, String direccion, String latitud, String longitud, String usuario) {
        this.id = id;
        this.tipo = tipo;
        this.causa = causa;
        this.comienzo = comienzo;
        this.nvlIncidencia = nvlIncidencia;
        this.carretera = carretera;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.usuario = usuario;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public Date getComienzo() {
        return comienzo;
    }

    public void setComienzo(Date comienzo) {
        this.comienzo = comienzo;
    }

    public String getNvlIncidencia() {
        return nvlIncidencia;
    }

    public void setNvlIncidencia(String nvlIncidencia) {
        this.nvlIncidencia = nvlIncidencia;
    }

    public String getCarretera() {
        return carretera;
    }

    public void setCarretera(String carretera) {
        this.carretera = carretera;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }


    // Método para formatear la fecha como String (puedes ajustar el formato según tus necesidades)
    public String getFormattedComienzo() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(comienzo);
    }

    // Ejemplo de un método que devuelve la información completa de la incidencia como String
    public String getInformacionCompleta() {
        return "ID: " + id +
                "\nTipo: " + tipo +
                "\nCausa: " + causa +
                "\nComienzo: " + getFormattedComienzo() +
                // ... (otros campos)
                "\nUsuario: " + usuario;
    }
}
