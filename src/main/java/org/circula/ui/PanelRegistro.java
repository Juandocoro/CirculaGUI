package org.circula.ui;

import org.circula.app.BaseDatos;
import javax.swing.*;
import java.awt.*;

public class PanelRegistro extends JPanel { // 👈 1. CORREGIDO: Declaración de la clase y herencia de JPanel

    public PanelRegistro(VentanaPrincipal principal) {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Crear Nueva Cuenta - Circula"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNombre = new JTextField(20);
        JTextField txtCorreo = new JTextField(20);
        JPasswordField txtPass = new JPasswordField(20);
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"Eco-Ciudadano", "Héroe Reciclador"});
        JButton btnRegistrar = new JButton("Crear Cuenta");
        JButton btnVolver = new JButton("Volver al Login");

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nombre Completo:"), gbc); // 👈 2. CORREGIDO: Se quitó la coma suelta y se agregó gbc

        gbc.gridx = 1;
        add(txtNombre, gbc); // 👈 3. CORREGIDO: Se agregó gbc para posicionar el cuadro de texto

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Correo Electrónico:"), gbc);
        gbc.gridx = 1; add(txtCorreo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; add(txtPass, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Tipo de Perfil:"), gbc);
        gbc.gridx = 1; add(cbRol, gbc);

        // Botones estructurados limpiamente con Insets válidos
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        add(btnRegistrar, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(btnVolver, gbc);

        btnRegistrar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String pass = new String(txtPass.getPassword());
            String rol = (String) cbRol.getSelectedItem();

            if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }
            if (BaseDatos.registrarUsuario(correo, pass, nombre, rol)) {
                JOptionPane.showMessageDialog(this, "¡Registro Exitoso!");
                principal.cambiarPantalla("LOGIN", "");
            } else {
                JOptionPane.showMessageDialog(this, "El correo ya se encuentra registrado.");
            }
        });

        btnVolver.addActionListener(e -> principal.cambiarPantalla("LOGIN", ""));
    }
} // 👈 4. CORREGIDO: Llave de cierre de la clase principal