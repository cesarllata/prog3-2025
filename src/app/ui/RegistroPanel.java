package app.ui;

import app.dao.UsuarioDAO;
import app.models.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegistroPanel extends JPanel {
    private MainFrame mainFrame;

    public RegistroPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout()); // Centra la tarjeta en el medio
        setBackground(new Color(240, 248, 255));

        // --- PANEL DE TARJETA (CUADRADO GRANDE) ---
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        
        // Aumentamos los márgenes internos: 40 arriba/abajo, 60 laterales
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));

        // Dimensiones más grandes para acomodar todos los campos cómodamente
        cardPanel.setPreferredSize(new Dimension(500, 700));
        cardPanel.setMaximumSize(new Dimension(500, 800));

        // --- COMPONENTES ---
        JLabel titulo = new JLabel("Crear Nueva Cuenta");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Título más grande
        titulo.setForeground(new Color(0, 70, 140));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        // Campos estilizados
        JTextField txtNombre = createStyledField();
        JTextField txtEmail = createStyledField();
        JPasswordField txtPass = createStyledPasswordField();
        JTextField txtTarjeta = createStyledField();
        JTextField txtCVV = createStyledField();
        
        // Restricciones de entrada
        txtTarjeta.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || txtTarjeta.getText().length() >= 16) e.consume();
            }
        });
        txtCVV.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || txtCVV.getText().length() >= 3) e.consume();
            }
        });

        JButton btnRegistrar = new JButton("REGISTRARSE");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setBackground(new Color(0, 120, 60)); // Verde éxito
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setMaximumSize(new Dimension(300, 50)); // Botón más grande
        
        JButton btnIrLogin = new JButton("¿Ya tienes cuenta? Inicia Sesión");
        btnIrLogin.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        btnIrLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIrLogin.setForeground(new Color(0, 70, 140));
        btnIrLogin.setContentAreaFilled(false);
        btnIrLogin.setBorderPainted(false);
        btnIrLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrLogin.addActionListener(e -> mainFrame.mostrarPanel("LOGIN"));

        // --- AÑADIR ELEMENTOS CON ESPACIADO ---
        cardPanel.add(titulo);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        addLabelAndField(cardPanel, "Nombre Completo:", txtNombre, labelFont);
        addLabelAndField(cardPanel, "Correo Electrónico:", txtEmail, labelFont);
        addLabelAndField(cardPanel, "Contraseña:", txtPass, labelFont);
        addLabelAndField(cardPanel, "Nº Tarjeta (16 dígitos):", txtTarjeta, labelFont);
        addLabelAndField(cardPanel, "CVV (3 dígitos):", txtCVV, labelFont);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(btnRegistrar);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(btnIrLogin);

        add(cardPanel);

        // --- LÓGICA DE REGISTRO ---
        btnRegistrar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            String tarjeta = txtTarjeta.getText().trim();
            String cvv = txtCVV.getText().trim();

            if(nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) { 
                JOptionPane.showMessageDialog(this, "Por favor, completa los campos básicos."); 
                return; 
            }
            if(tarjeta.length() != 16) { 
                JOptionPane.showMessageDialog(this, "La tarjeta debe tener exactamente 16 dígitos."); 
                return; 
            }
            if(cvv.length() != 3) { 
                JOptionPane.showMessageDialog(this, "El CVV debe tener 3 dígitos."); 
                return; 
            }

            Usuario nuevo = new Usuario(nombre, email, pass, "Sin telefono", tarjeta);
            UsuarioDAO dao = new UsuarioDAO();
            Usuario registrado = dao.registrar(nuevo);

            if (registrado != null) {
                JOptionPane.showMessageDialog(this, "¡Cuenta creada con éxito! Te hemos regalado 50.00€ de bienvenida.");
                mainFrame.loginExitoso(registrado);
                limpiarCampos(txtNombre, txtEmail, txtPass, txtTarjeta, txtCVV);
            } else {
                JOptionPane.showMessageDialog(this, "Error: El email ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Método auxiliar para no repetir código de etiquetas y campos
    private void addLabelAndField(JPanel panel, String text, JComponent field, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private JTextField createStyledField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private void limpiarCampos(JTextField... campos) {
        for (JTextField f : campos) f.setText("");
    }
}