package Principal;

import javax.swing.*;
import java.awt.*;

public class RegistroPanel extends JPanel {
    private MainFrame mainFrame;

    public RegistroPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));

        // --- Panel Tarjeta ---
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        // Título
        JLabel titulo = new JLabel("Crear Nueva Cuenta");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 70, 140));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos
        JTextField txtNombre = createStyledField("Nombre Completo");
        JTextField txtEmail = createStyledField("Correo Electrónico");
        JPasswordField txtPass = createStyledPasswordField("Contraseña");

        // Botón
        JButton btnRegistrar = new JButton("REGISTRARSE");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setBackground(new Color(0, 120, 60)); // Verde
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setMaximumSize(new Dimension(200, 40));
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Añadir componentes a la tarjeta
        cardPanel.add(titulo);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(new JLabel("Nombre:"));
        cardPanel.add(txtNombre);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(new JLabel("Email:"));
        cardPanel.add(txtEmail);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(new JLabel("Contraseña:"));
        cardPanel.add(txtPass);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(btnRegistrar);

        add(cardPanel);

        // Acción
        btnRegistrar.addActionListener(e -> {
            if(txtNombre.getText().trim().isEmpty()) return;

            Usuario nuevo = new Usuario(txtNombre.getText(), txtEmail.getText(), txtPass.getText(), "123456789");
            JOptionPane.showMessageDialog(this,"Usuario registrado con éxito");
            
            // Iniciar sesión automáticamente
            mainFrame.loginExitoso(nuevo);
            
            // Limpiar
            txtNombre.setText("");
            txtEmail.setText("");
            txtPass.setText("");
        });
    }

    // Helpers para estilo
    private JTextField createStyledField(String tooltip) {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setToolTipText(tooltip);
        return field;
    }
    
    private JPasswordField createStyledPasswordField(String tooltip) {
        JPasswordField field = new JPasswordField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setToolTipText(tooltip);
        return field;
    }
}