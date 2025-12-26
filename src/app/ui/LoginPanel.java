package app.ui;

import app.dao.UsuarioDAO;
import app.models.Usuario;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout()); // Centra el cardPanel en la pantalla
        setBackground(new Color(240, 248, 255));

        // --- PANEL DE TARJETA (EL CUADRADO) ---
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        
        // Aumentamos los márgenes internos (arriba, izquierda, abajo, derecha)
        // Pasamos de 30,40 a 50,60 para que sea más espacioso
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(50, 60, 50, 60)
        ));

        // Forzamos un ancho preferido más grande (450px de ancho, por ejemplo)
        cardPanel.setPreferredSize(new Dimension(450, 550));
        cardPanel.setMaximumSize(new Dimension(450, 600));

        // --- COMPONENTES ---
        JLabel titulo = new JLabel("Iniciar Sesión");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Título más grande
        titulo.setForeground(new Color(0, 70, 140));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel lblUsuario = new JLabel("Usuario o Email");
        lblUsuario.setFont(labelFont);
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField txtUsuario = new JTextField(20);
        // Aumentamos la altura de los campos de texto
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(labelFont);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField txtPass = new JPasswordField(20);
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnLogin = new JButton("ENTRAR");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(0, 70, 140));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Botón más ancho y alto
        btnLogin.setMaximumSize(new Dimension(250, 50));
        
        JButton btnIrRegistro = new JButton("¿No tienes cuenta? Regístrate");
        btnIrRegistro.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        btnIrRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIrRegistro.setForeground(new Color(0, 70, 140));
        btnIrRegistro.setContentAreaFilled(false);
        btnIrRegistro.setBorderPainted(false);
        btnIrRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrRegistro.addActionListener(e -> mainFrame.mostrarPanel("REGISTRO"));

        // --- AÑADIR AL PANEL CON ESPACIADOS MAYORES ---
        cardPanel.add(titulo);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 40))); // Más espacio bajo el título
        cardPanel.add(lblUsuario);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(txtUsuario);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25))); // Más espacio entre campos
        cardPanel.add(lblPass);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(txtPass);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        cardPanel.add(btnLogin);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(btnIrRegistro);

        add(cardPanel);

        // --- LÓGICA (Se mantiene igual) ---
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