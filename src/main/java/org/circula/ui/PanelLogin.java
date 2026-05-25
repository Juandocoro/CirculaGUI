package org.circula.ui;




import org.circula.app.BaseDatos;
import javax.swing.*;
import java.awt.*;

public class PanelLogin extends JPanel {
    public PanelLogin(VentanaPrincipal principal) {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Acceso Modular"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10); gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtCorreo = new JTextField(15);
        JPasswordField txtPass = new JPasswordField(15);
        JButton btnEntrar = new JButton("Ingresar");
        JButton btnRegistro = new JButton("Registrarse");

        gbc.gridx=0; gbc.gridy=0; add(new JLabel("Correo:"), gbc);
        gbc.gridx=1; add(txtCorreo, gbc);
        gbc.gridx=0; gbc.gridy=1; add(new JLabel("Pass:"), gbc);
        gbc.gridx=1; add(txtPass, gbc);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; add(btnEntrar, gbc);
        gbc.gridy=3; add(btnRegistro, gbc);

        btnEntrar.addActionListener(e -> {
            String rol = BaseDatos.login(txtCorreo.getText(), new String(txtPass.getPassword()));
            if(rol != null) {
                principal.cambiarPantalla(rol.equalsIgnoreCase("Eco-Ciudadano") ? "CIUDADANO" : "RECICLADOR", txtCorreo.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Error de acceso");
            }
        });

        btnRegistro.addActionListener(e -> principal.cambiarPantalla("REGISTRO", ""));
    }
}