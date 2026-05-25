package org.circula.ui;

import org.circula.app.BaseDatos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelCiudadano extends JPanel {
    private VentanaPrincipal principal;
    private JLabel lblPuntos;
    private String correoLogueado = "";

    public PanelCiudadano(VentanaPrincipal principal) {
        this.principal = principal;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Encabezado con Saldo
        JPanel bannerSuperior = new JPanel(new BorderLayout());
        bannerSuperior.setBackground(new Color(230, 245, 230));
        lblPuntos = new JLabel("Eco-Puntos: 0 🌟", SwingConstants.RIGHT);
        lblPuntos.setFont(new Font("Arial", Font.BOLD, 14));
        bannerSuperior.add(new JLabel("♻️ Panel de Gestión Sostenible - Ciudadano"), BorderLayout.WEST);
        bannerSuperior.add(lblPuntos, BorderLayout.EAST);
        add(bannerSuperior, BorderLayout.NORTH);

        // Cuerpo: Formulario a la izquierda, Premios a la derecha
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 15, 15));

        // Formulario de Solicitudes
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Crear Solicitud de Recogida"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 5, 5, 5);

        JTextField txtDir = new JTextField();
        JTextField txtRes = new JTextField();
        JButton btnPedir = new JButton("🚀 Publicar Solicitud");

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; panelForm.add(txtDir, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Residuos:"), gbc);
        gbc.gridx = 1; panelForm.add(txtRes, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.insets = new Insets(15, 5, 5, 5);
        panelForm.add(btnPedir, gbc);
        panelCentral.add(panelForm);

        // Catálogo de Premios
        JPanel panelPremios = new JPanel(new BorderLayout());
        panelPremios.setBorder(BorderFactory.createTitledBorder("Catálogo de Eco-Canjes"));
        String[] columnasPremios = {"Premio", "Eco-Puntos"};
        Object[][] datosPremios = {
                {"Bolsa Ecológica de Tela", 50},
                {"Termo Metálico Reutilizable", 100},
                {"Bono de Transporte Urbano", 150}
        };
        JTable tablaPremios = new JTable(new DefaultTableModel(datosPremios, columnasPremios));
        panelPremios.add(new JScrollPane(tablaPremios), BorderLayout.CENTER);

        JButton btnCanjear = new JButton("🎁 Canjear Premio Seleccionado");
        panelPremios.add(btnCanjear, BorderLayout.SOUTH);
        panelCentral.add(panelPremios);

        add(panelCentral, BorderLayout.CENTER);

        JButton btnSalir = new JButton("Cerrar Sesión");
        add(btnSalir, BorderLayout.SOUTH);

        // LÓGICA OPERATIVA DE LOS BOTONES
        btnPedir.addActionListener(e -> {
            if (txtDir.getText().trim().isEmpty() || txtRes.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete la dirección y los residuos.");
                return;
            }
            BaseDatos.crearSolicitud(correoLogueado, txtDir.getText().trim(), txtRes.getText().trim());
            JOptionPane.showMessageDialog(this, "¡Solicitud puesta en cola para los recicladores!");
            txtDir.setText(""); txtRes.setText("");
        });

        btnCanjear.addActionListener(e -> {
            int fila = tablaPremios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un premio del catálogo.");
                return;
            }
            String premio = (String) tablaPremios.getValueAt(fila, 0);
            int costo = (int) tablaPremios.getValueAt(fila, 1);

            if (BaseDatos.canjearPremio(correoLogueado, costo)) {
                JOptionPane.showMessageDialog(this, "¡Canjeado con éxito! Disfruta tu: " + premio);
                actualizarDatos(correoLogueado);
            } else {
                JOptionPane.showMessageDialog(this, "No tienes Eco-Puntos suficientes para este premio.", "Saldo Insuficiente", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnSalir.addActionListener(e -> principal.cambiarPantalla("LOGIN", ""));
    }

    // 🔄 MÉTODO CORREGIDO: Ahora llama a obtenerDatosUsuario sin errores
    public void actualizarDatos(String correo) {
        this.correoLogueado = correo;
        int[] datos = BaseDatos.obtenerDatosUsuario(correo); // 👈 ¡Arreglado aquí!
        lblPuntos.setText("Eco-Puntos: " + datos[0] + " 🌟");
    }
}
