package Principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegistroPanel extends JPanel {
    private MainFrame mainFrame;

    public RegistroPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));

        JLabel titulo = new JLabel("Crear Nueva Cuenta");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(0, 70, 140));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtNombre = createStyledField("Nombre Completo");
        JTextField txtEmail = createStyledField("Correo Electrónico");
        JPasswordField txtPass = createStyledPasswordField("Contraseña");
        JTextField txtTarjeta = createStyledField("Número de Tarjeta (16 dígitos)");
        JTextField txtCVV = createStyledField("CVV (3 dígitos)");
        
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
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setBackground(new Color(0, 120, 60));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setMaximumSize(new Dimension(200, 40));
        
        // --- BOTÓN NUEVO: IR A LOGIN ---
        JButton btnIrLogin = new JButton("¿Ya tienes cuenta? Inicia Sesión");
        btnIrLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIrLogin.setForeground(new Color(0, 70, 140));
        btnIrLogin.setContentAreaFilled(false);
        btnIrLogin.setBorderPainted(false);
        btnIrLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrLogin.addActionListener(e -> mainFrame.mostrarPanel("LOGIN"));

        cardPanel.add(titulo);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(new JLabel("Nombre:")); cardPanel.add(txtNombre);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(new JLabel("Email:")); cardPanel.add(txtEmail);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(new JLabel("Contraseña:")); cardPanel.add(txtPass);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(new JLabel("Nº Tarjeta (16 dígitos):")); cardPanel.add(txtTarjeta);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(new JLabel("CVV (3 dígitos):")); cardPanel.add(txtCVV);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(btnRegistrar);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(btnIrLogin); // Añadido aquí

        add(cardPanel);

        btnRegistrar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            String tarjeta = txtTarjeta.getText().trim();
            String cvv = txtCVV.getText().trim();

            if(nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(this, "Completa campos básicos."); return; }
            if(tarjeta.length() != 16) { JOptionPane.showMessageDialog(this, "Tarjeta 16 dígitos."); return; }
            if(cvv.length() != 3) { JOptionPane.showMessageDialog(this, "CVV 3 dígitos."); return; }

            Usuario nuevo = new Usuario(nombre, email, pass, "Sin telefono", tarjeta);
            UsuarioDAO dao = new UsuarioDAO();
            Usuario registrado = dao.registrar(nuevo);

            if (registrado != null) {
                JOptionPane.showMessageDialog(this,"Cuenta creada. ¡Te regalamos 50.00€!");
                mainFrame.loginExitoso(registrado);
                txtNombre.setText(""); txtEmail.setText(""); txtPass.setText(""); txtTarjeta.setText(""); txtCVV.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error: Email ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JTextField createStyledField(String tooltip) {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setToolTipText(tooltip);
        return field;
    }
    private JPasswordField createStyledPasswordField(String tooltip) {
        JPasswordField field = new JPasswordField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setToolTipText(tooltip);
        return field;
    }
}