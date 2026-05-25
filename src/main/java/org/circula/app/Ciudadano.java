package org.circula.app;

public class Ciudadano {
    public String correo;
    public String password;
    public String nombre;
    public int puntos;

    // Constructor para inicializar el objeto
    public Ciudadano(String correo, String password, String nombre) {
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
        this.puntos = 0; // Inicia con 0 puntos
    }
}