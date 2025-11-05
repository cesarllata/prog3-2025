package LogIn;

import java.awt.*;
import javax.swing.*;

/**
 * Simula un registro de usuario simple (no persiste en ninguna parte).
 */
public class RegisterDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField usuarioField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private boolean registered = false;
    private String registeredUser = "";

    public RegisterDialog(Frame parent) {
        super(parent, "Register", true);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.add(new JLabel("Usuario:"));
        usuarioField = new JTextField();
        panel.add(usuarioField);

        panel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Confirmar contraseña:"));
        confirmField = new JPasswordField();
        panel.add(confirmField);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRegister = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");
        botones.add(btnCancelar);
        botones.add(btnRegister);

        add(panel, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnRegister.addActionListener(e -> doRegister());
        btnCancelar.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(btnRegister);
        setSize(350, 200);
        setLocationRelativeTo(getParent());
    }

    private void doRegister() {
        String usuario = usuarioField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String conf = new String(confirmField.getPassword());

        if (usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellene usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pass.equals(conf)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simulación de registro OK
        registered = true;
        registeredUser = usuario;
        JOptionPane.showMessageDialog(this, "Registro correcto. Usuario: " + usuario, "Registro", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public boolean isRegistered() {
        return registered;
    }

    public String getRegisteredUser() {
        return registeredUser;
    }
}
