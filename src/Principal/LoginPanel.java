package Principal;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        // Layout principal para centrar la "tarjeta"
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255)); // Fondo suave general

        
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        // Título
        JLabel titulo = new JLabel("Iniciar Sesión");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 70, 140));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JTextField txtUsuario = new JTextField(20);
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JPasswordField txtPass = new JPasswordField(20);
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Botón
        JButton btnLogin = new JButton("ENTRAR");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(0, 70, 140));
        btnLogin.setFocusPainted(false);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(150, 40));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Espaciadores
        cardPanel.add(titulo);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(lblUsuario);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(txtUsuario);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(lblPass);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(txtPass);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(btnLogin);

        add(cardPanel);

        // Acción
        btnLogin.addActionListener(e -> {
            if(txtUsuario.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Introduce un usuario", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Usuario usuarioSimulado = new Usuario(txtUsuario.getText());
            JOptionPane.showMessageDialog(this, "¡Bienvenido, " + usuarioSimulado.getNombre() + "!");
            mainFrame.loginExitoso(usuarioSimulado);
            // Limpiar campos
            txtUsuario.setText("");
            txtPass.setText("");
        });
    }
}