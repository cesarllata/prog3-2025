package Principal;

import javax.swing.*;
import java.awt.*;

public class RegistroPanel extends JPanel {
    private MainFrame mainFrame;

    public RegistroPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ---- Añadido: barra superior con botón atrás ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JButton backBtn = new JButton("← Volver");
        backBtn.setToolTipText("Volver al menú principal");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        backBtn.setFocusPainted(false);

        backBtn.setOpaque(true);
        backBtn.setContentAreaFilled(true);
        backBtn.setBackground(new Color(230, 230, 230));
        backBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        backBtn.addActionListener(e -> {
            if (this.mainFrame != null) {
                this.mainFrame.mostrarPanel("MENU");
                return;
            }
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof MainFrame) {
                ((MainFrame) w).mostrarPanel("MENU");
            }
        });

        topBar.add(backBtn, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);
        // -------------------------------------------------

        JTextField txtNombre = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtPass = new JPasswordField();

        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(new JLabel("Nombre:"));
        centerPanel.add(txtNombre);
        centerPanel.add(new JLabel("Email:"));
        centerPanel.add(txtEmail);
        centerPanel.add(new JLabel("Contraseña:"));
        centerPanel.add(txtPass);

        add(centerPanel, BorderLayout.CENTER);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> {

            // Aquí podrías guardar en BBDD o archivo
            Usuario nuevo = new Usuario(txtNombre.getText());

            JOptionPane.showMessageDialog(this,"Usuario registrado con éxito");
            
            // Iniciar sesión automáticamente tras registro
            mainFrame.loginExitoso(nuevo);
        });

        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.add(btnRegistrar);

        add(southPanel, BorderLayout.SOUTH);
    }
}
