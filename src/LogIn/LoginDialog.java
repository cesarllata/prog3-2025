package LogIn;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Simula una ventana de inicio de sesión.
 * No requiere base de datos.
 * Usuario por defecto: admin
 * Contraseña por defecto: 1234
 */
public class LoginDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField usuarioField;
    private JPasswordField passwordField;
    private boolean loginExitoso = false; // Indica si el login fue correcto

    public LoginDialog(Frame parent) {
        super(parent, "Iniciar sesión", true); // Ventana modal
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel central con campos
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCampos.add(new JLabel("Usuario:"));
        usuarioField = new JTextField();
        panelCampos.add(usuarioField);
        panelCampos.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        panelCampos.add(passwordField);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel();
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Listeners
        btnAceptar.addActionListener(e -> verificarLogin());
        btnCancelar.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnAceptar);

        // Tamaño y posición
        setSize(300, 150);
        setLocationRelativeTo(getParent());
    }

    private void verificarLogin() {
        String usuario = usuarioField.getText().trim();
        String contrasena = new String(passwordField.getPassword());

        // Login simulado
        if (usuario.equals("admin") && contrasena.equals("1234")) {
            loginExitoso = true;
            JOptionPane.showMessageDialog(this, "Inicio de sesión correcto. Bienvenido, " + usuario + "!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Devuelve el usuario ingresado en el último intento.
     */
    public String getUsuario() {
        return usuarioField.getText().trim();
    }

    public boolean isLoginExitoso() {
        return loginExitoso;
    }
}
