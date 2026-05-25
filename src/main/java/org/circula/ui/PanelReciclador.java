package org.circula.ui;


import org.circula.app.BaseDatos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.util.List;

public class PanelReciclador extends JPanel {
    private VentanaPrincipal principal;
    private JLabel lblGanancias;
    private DefaultTableModel modeloTabla;
    private String correoLogueado = "";

    public PanelReciclador(VentanaPrincipal principal) {
        this.principal = principal;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Banner de Estado
        JPanel bannerSuperior = new JPanel(new BorderLayout());
        bannerSuperior.setBackground(new Color(230, 235, 250));
        lblGanancias = new JLabel("Monedero Virtual: $0 COP 💰", SwingConstants.RIGHT);
        lblGanancias.setFont(new Font("Arial", Font.BOLD, 14));
        bannerSuperior.add(new JLabel("🚛 Central de Logística y Rutas de Reciclaje"), BorderLayout.WEST);
        bannerSuperior.add(lblGanancias, BorderLayout.EAST);
        add(bannerSuperior, BorderLayout.NORTH);

        // Tabla de Solicitudes
        String[] columnas = {"ID", "Ciudadano", "Dirección", "Descripción"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel de Acciones Operativas
        JPanel panelAcciones = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton btnRuta = new JButton("🗺️ Ver Ruta Maps");
        JButton btnCompletar = new JButton("✔️ Finalizar Servicio");
        JButton btnRefrescar = new JButton("🔄 Actualizar");
        JButton btnSalir = new JButton("Salir");

        panelAcciones.add(btnRuta);
        panelAcciones.add(btnCompletar);
        panelAcciones.add(btnRefrescar);
        panelAcciones.add(btnSalir);
        add(panelAcciones, BorderLayout.SOUTH);

        // ESCUCHADORES DE ACCIONES LOGÍSTICAS
        btnRefrescar.addActionListener(e -> actualizarDatos(correoLogueado));

        btnRuta.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione una orden de la lista."); return; }
            String direccion = (String) tabla.getValueAt(fila, 2);
            try {
                String url = "https://www.google.com/maps/search/?api=1&query=" + direccion.replace(" ", "+");
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo abrir el mapa.");
            }
        });

        btnCompletar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione el servicio a completar."); return; }
            int idSolicitud = Integer.parseInt((String) tabla.getValueAt(fila, 0));

            String[] opcionesMateriales = {"Plástico", "Cartón", "Metal", "Vidrio"};
            String material = (String) JOptionPane.showInputDialog(this,
                    "Seleccione el material clasificado:", "Báscula de Pesaje",
                    JOptionPane.QUESTION_MESSAGE, null, opcionesMateriales, opcionesMateriales[0]);

            if (material == null) return;

            String pesoTxt = JOptionPane.showInputDialog(this, "Ingrese el peso total en Kilogramos (Kg):");
            if (pesoTxt == null || pesoTxt.isEmpty()) return;

            try {
                double peso = Double.parseDouble(pesoTxt);
                if (BaseDatos.completarSolicitud(idSolicitud, peso, material, correoLogueado)) {
                    JOptionPane.showMessageDialog(this, "¡Servicio Certificado! Fondos cargados.");
                    actualizarDatos(correoLogueado);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al cerrar la orden.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un peso numérico válido.");
            }
        });

        btnSalir.addActionListener(e -> principal.cambiarPantalla("LOGIN", ""));
    }

    // Este proceso se invoca automáticamente desde VentanaPrincipal al cambiar de pantalla
    public void actualizarDatos(String correo) {
        this.correoLogueado = correo;
        int[] datos = BaseDatos.obtenerDatosUsuario(correo);
        lblGanancias.setText("Monedero Virtual: $" + datos[1] + " COP 💰");

        modeloTabla.setRowCount(0);
        List<String[]> solicitudes = BaseDatos.obtenerSolicitudesPendientesG();
        for (String[] fila : solicitudes) {
            modeloTabla.addRow(fila);
        }
    }

}