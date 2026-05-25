package org.circula.ui;


import org.circula.app.BaseDatos;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private String correoUsuarioLogueado = "";

    // Instancias de los paneles modulares
    private PanelLogin zonaLogin;
    private PanelRegistro zonaRegistro;
    private PanelCiudadano zonaCiudadano;
    private PanelReciclador zonaReciclador;

    public VentanaPrincipal() {
        setTitle("CirculaApp - Versión Modular");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // 1. Inicializamos los paneles pasando "this" para que ellos puedan llamar a cambiarPantalla()
        zonaLogin = new PanelLogin(this);
        zonaRegistro = new PanelRegistro(this);
        zonaCiudadano = new PanelCiudadano(this);
        zonaReciclador = new PanelReciclador(this);

        // 2. Los agregamos al contenedor
        panelContenedor.add(zonaLogin, "LOGIN");
        panelContenedor.add(zonaRegistro, "REGISTRO");
        panelContenedor.add(zonaCiudadano, "CIUDADANO");
        panelContenedor.add(zonaReciclador, "RECICLADOR");

        add(panelContenedor);
        cardLayout.show(panelContenedor, "LOGIN");
    }

    // Proceso centralizado para cambiar de vista desde cualquier panel
    public void cambiarPantalla(String nombrePantalla, String correo) {
        this.correoUsuarioLogueado = correo;

        // Antes de mostrar, le decimos al panel que se refresque si es necesario
        if (nombrePantalla.equals("CIUDADANO")) zonaCiudadano.actualizarDatos(correo);
        if (nombrePantalla.equals("RECICLADOR")) zonaReciclador.actualizarDatos(correo);

        cardLayout.show(panelContenedor, nombrePantalla);
    }

    public String getCorreoUsuario() { return correoUsuarioLogueado; }
}