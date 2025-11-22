package Principal;

import javax.swing.*;
import java.awt.*;
import Principal.*; // mantiene referencia a MainFrame si está en el mismo paquete
import Principal.*; // no-op pero deja claro el paquete

public class HeaderPanel extends JPanel {

    private MainFrame mainFrame;
    private JButton btnSalir;
    private JLabel lblEmpresa;

    // autenticación
    private JPanel panelAuth;
    private CardLayout cardLayoutAuth;
    private JButton btnLogin;
    private UserButton userButton;
    private UserSidePanel userSidePanel;

    public HeaderPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, 80));
        this.setBackground(new Color(0, 70, 140));

        btnSalir = new JButton("Salir");
        btnSalir.setPreferredSize(new Dimension(100, 40));
        btnSalir.addActionListener(e -> { if (mainFrame != null) mainFrame.solicitarSalir(); });
        this.add(btnSalir, BorderLayout.WEST);

        lblEmpresa = new JLabel("DEUSTO BUS F.R.");
        lblEmpresa.setFont(new Font("Arial", Font.BOLD, 26));
        lblEmpresa.setForeground(Color.WHITE);
        lblEmpresa.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblEmpresa, BorderLayout.CENTER);

        cardLayoutAuth = new CardLayout();
        panelAuth = new JPanel(cardLayoutAuth);
        panelAuth.setOpaque(false);
        panelAuth.setPreferredSize(new Dimension(160, 60));

        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(100, 40));
        btnLogin.addActionListener(e -> { if (mainFrame != null) mainFrame.mostrarPantallaLoginRegistro(); });
        panelAuth.add(btnLogin, "LOGOUT");

        userButton = new UserButton();
        userButton.setBackground(btnLogin.getBackground());
        userButton.addActionListener(ae -> toggleUserSidePanel());
        panelAuth.add(userButton, "LOGIN");

        this.add(panelAuth, BorderLayout.EAST);

        // inicializar estado (si usas SessionManager, suscríbete allí; aquí se mantiene el método público actualizarVistaLogin)
        actualizarVistaLogin(null);
    }

    private void toggleUserSidePanel() {
        if (mainFrame == null) {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof MainFrame) mainFrame = (MainFrame) w;
        }
        if (userSidePanel != null && userSidePanel.isVisible()) {
            userSidePanel.dispose();
            userSidePanel = null;
            return;
        }
        Window owner = mainFrame != null ? mainFrame : SwingUtilities.getWindowAncestor(this);
        if (owner == null) return;
        userSidePanel = new UserSidePanel(owner, 280, mainFrame);
        userSidePanel.setVisible(true);
    }

    public void actualizarVistaLogin(Usuario usuario) {
        if (usuario != null) {
            userButton.setUsername(usuario.getNombre());
            cardLayoutAuth.show(panelAuth, "LOGIN");
        } else {
            cardLayoutAuth.show(panelAuth, "LOGOUT");
            if (userSidePanel != null) { userSidePanel.dispose(); userSidePanel = null; }
        }
    }
}
