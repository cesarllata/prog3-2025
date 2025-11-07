package Principal;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private MainFrame mainFrame;
    private JButton btnSalir;
    private JLabel lblEmpresa;

    // Componentes de autenticación
    private JPanel panelAuth;
    private CardLayout cardLayoutAuth;
    private JButton btnLogin;
    private JLabel lblUsuario;

    public HeaderPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, 80));
        this.setBackground(new Color(0, 70, 140));

        // --- Botón Salir ---
        btnSalir = new JButton("Salir");
        btnSalir.setPreferredSize(new Dimension(100, 40));
        btnSalir.addActionListener(e -> mainFrame.solicitarSalir());
        this.add(btnSalir, BorderLayout.WEST);

        // --- Nombre empresa ---
        lblEmpresa = new JLabel("DEUSTO BUS F.R.");
        lblEmpresa.setFont(new Font("Arial", Font.BOLD, 26));
        lblEmpresa.setForeground(Color.WHITE);
        lblEmpresa.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblEmpresa, BorderLayout.CENTER);

        // --- Panel autenticación ---
        cardLayoutAuth = new CardLayout();
        panelAuth = new JPanel(cardLayoutAuth);
        panelAuth.setOpaque(false);
        panelAuth.setPreferredSize(new Dimension(140, 60));

        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(100, 40));
        btnLogin.addActionListener(e -> mainFrame.mostrarPantallaLoginRegistro());
        panelAuth.add(btnLogin, "LOGOUT");

        lblUsuario = new JLabel();
        lblUsuario.setFont(new Font("Arial", Font.ITALIC, 18));
        lblUsuario.setForeground(Color.WHITE);
        panelAuth.add(lblUsuario, "LOGIN");

        this.add(panelAuth, BorderLayout.EAST);

        // Inicializar estado
        actualizarVistaLogin(null);
    }

    /**
     * Actualiza el panel de autenticación según el usuario actual.
     */
    public void actualizarVistaLogin(Usuario usuario) {
        if (usuario != null) {
            lblUsuario.setText(usuario.getNombre());
            cardLayoutAuth.show(panelAuth, "LOGIN");
        } else {
            cardLayoutAuth.show(panelAuth, "LOGOUT");
        }
    }
}
