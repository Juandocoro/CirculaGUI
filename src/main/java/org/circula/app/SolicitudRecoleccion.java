package org.circula.app;

public class SolicitudRecoleccion {
    public int id;
    public String correoCiudadano;
    public String direccion;
    public String residuoEstimado;
    public String estado; // "Pendiente" o "Completada"

    public SolicitudRecoleccion(int id, String correoCiudadano, String direccion, String residuoEstimado) {
        this.id = id;
        this.correoCiudadano = correoCiudadano;
        this.direccion = direccion;
        this.residuoEstimado = residuoEstimado;
        this.estado = "Pendiente";
    }
}