package org.circula.app;


import org.circula.ui.*;
import javax.swing.SwingUtilities;

public class CirculaApp {
    public static void main(String[] args) {

        // 1. Inicializar las tablas y la conexión de la Base de Datos SQLite
        // Al estar BaseDatos en este mismo paquete 'app', se reconoce sin imports.
        BaseDatos.inicializar();

        // 2. Lanzar la interfaz gráfica de Swing de forma segura
        // Se ejecuta en el hilo de eventos (Event Dispatch Thread) para evitar congelamientos
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true); // Hace visible la ventana principal con sus 4 zonas
        });
    }
}