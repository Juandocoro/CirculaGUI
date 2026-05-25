package org.circula.app;

public class Recolector {
    public String correo;
    public String password;
    public String nombre;
    public double ganancias;

    public Recolector(String correo, String password, String nombre) {
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
        this.ganancias = 0.0; // Inicia con 0 ganancias
    }
}
