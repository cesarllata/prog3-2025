package Principal;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // Asegurar layout que permita colocar la barra superior correctamente
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ---- Añadido / reemplazado: barra superior con botón atrás visible y funcional ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JButton backBtn = createBackButton();
        topBar.add(backBtn, BorderLayout.WEST);

        add(topBar, BorderLayout.NORTH);
        // ----------------------------------------------------------------------------------

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Iniciar Sesión");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;

        JLabel lblUsuario = new JLabel("Usuario:");
        JTextField txtUsuario = new JTextField(15);
        JLabel lblPass = new JLabel("Contraseña:");
        JPasswordField txtPass = new JPasswordField(15);

        gbc.gridy = 1;
        gbc.gridx = 0;
        add(lblUsuario, gbc);
        gbc.gridx = 1;
        add(txtUsuario, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(lblPass, gbc);
        gbc.gridx = 1;
        add(txtPass, gbc);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(140, 35));

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            Usuario usuarioSimulado = new Usuario(txtUsuario.getText());
            mainFrame.loginExitoso(usuarioSimulado);
        });
    }

    private JButton createBackButton() {
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
        return backBtn;
    }
}
