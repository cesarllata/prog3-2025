package app.ui;

import app.dao.UsuarioDAO;
import app.models.Usuario;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel titulo = new JLabel("Iniciar Sesión");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 70, 140));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUsuario = new JLabel("Usuario o Email");
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField txtUsuario = new JTextField(20);
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField txtPass = new JPasswordField(20);
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JButton btnLogin = new JButton("ENTRAR");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(0, 70, 140));
        btnLogin.setFocusPainted(false);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(150, 40));
        
        // --- BOTÓN NUEVO: IR A REGISTRO ---
        JButton btnIrRegistro = new JButton("¿No tienes cuenta? Regístrate");
        btnIrRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIrRegistro.setForeground(new Color(0, 70, 140));
        btnIrRegistro.setContentAreaFilled(false);
        btnIrRegistro.setBorderPainted(false);
        btnIrRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrRegistro.addActionListener(e -> mainFrame.mostrarPanel("REGISTRO"));

        cardPanel.add(titulo);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(lblUsuario);
        cardPanel.add(txtUsuario);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(lblPass);
        cardPanel.add(txtPass);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(btnLogin);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(btnIrRegistro); // Añadido aquí

        add(cardPanel);

        btnLogin.addActionListener(e -> {
            String user = txtUsuario.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            if(user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Rellena todos los campos");
                return;
            }
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuarioEncontrado = dao.login(user, pass);
            if (usuarioEncontrado != null) {
                JOptionPane.showMessageDialog(this, "¡Bienvenido, " + usuarioEncontrado.getNombre() + "!");
                mainFrame.loginExitoso(usuarioEncontrado);
                txtUsuario.setText("");
                txtPass.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
