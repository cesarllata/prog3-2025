package app.ui;

import app.models.Usuario;
import app.ui.components.UserButton;
import app.ui.components.UserSidePanel;
import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private MainFrame mainFrame;
    private JButton btnNavegacion;
    private JLabel lblEmpresa;

    private JPanel panelAuth; // ESTE SERÁ NUESTRA ANCLA
    private CardLayout cardLayoutAuth;
    private JButton btnLogin;
    private UserButton userButton;
    private UserSidePanel userSidePanel;
    private JLabel lblCuentaAtras;
    private JLabel lblExpiraTexto;
    private JPanel sessionPanel;

    public HeaderPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, 80));
        this.setBackground(new Color(0, 70, 140));

        // Botón Izquierdo
        btnNavegacion = new JButton("Salir");
        btnNavegacion.setPreferredSize(new Dimension(100, 40));
    btnNavegacion.addActionListener(e -> mainFrame.accionBotonNavegacion());

        // Título Central
        lblEmpresa = new JLabel("DEUSTO BUS F.R.");
        lblEmpresa.setFont(new Font("Arial", Font.BOLD, 26));
        lblEmpresa.setForeground(Color.WHITE);
    lblEmpresa.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel Derecho (Auth)
        cardLayoutAuth = new CardLayout();
        panelAuth = new JPanel(cardLayoutAuth);
        panelAuth.setOpaque(false);
        
        // ANCHO FIJO: Esto definirá el ancho del menú desplegable también
        panelAuth.setPreferredSize(new Dimension(220, 60)); 

        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(100, 40));
        btnLogin.addActionListener(e -> { 
            if (mainFrame != null) mainFrame.mostrarPanel("LOGIN"); 
        });
        panelAuth.add(btnLogin, "LOGOUT");

    userButton = new UserButton();
    userButton.setBackground(btnLogin.getBackground());
    userButton.addActionListener(ae -> toggleUserSidePanel());

    // Construir el panel que se mostrará en el estado LOGIN: cuenta + userButton
    sessionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 10));
    sessionPanel.setOpaque(false);

    lblExpiraTexto = new JLabel("La sesión expira en:");
    lblExpiraTexto.setForeground(Color.WHITE);
    lblExpiraTexto.setFont(new Font("Arial", Font.PLAIN, 13));
    lblExpiraTexto.setVisible(true);
    sessionPanel.add(lblExpiraTexto);

    lblCuentaAtras = new JLabel("01:00:00");
    lblCuentaAtras.setForeground(Color.WHITE);
    lblCuentaAtras.setFont(new Font("Arial", Font.BOLD, 13));
    lblCuentaAtras.setVisible(true);
    sessionPanel.add(lblCuentaAtras);

    JPanel loginCard = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 10));
    loginCard.setOpaque(false);
    loginCard.add(sessionPanel);
    loginCard.add(userButton);
    panelAuth.add(loginCard, "LOGIN");

            // Construir la fila superior: izquierdo (botón), centro (título), derecho (auth)
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);

            // Ajustar botón izquierdo dentro de un wrapper para controlar márgenes
            JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 18));
            leftWrapper.setOpaque(false);
            leftWrapper.add(btnNavegacion);
            topPanel.add(leftWrapper, BorderLayout.WEST);

            // Título central (lo dejamos centrado en su propio wrapper)
            JPanel centerWrapper = new JPanel(new BorderLayout());
            centerWrapper.setOpaque(false);
            centerWrapper.add(lblEmpresa, BorderLayout.CENTER);
            topPanel.add(centerWrapper, BorderLayout.CENTER);

            // Panel derecho (auth)
            JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            rightWrapper.setOpaque(false);
            rightWrapper.add(panelAuth);
            topPanel.add(rightWrapper, BorderLayout.EAST);

            this.add(topPanel, BorderLayout.NORTH);

        actualizarVistaLogin(null);
    }

    // Método público para que MainFrame actualice el texto del contador
    public void actualizarCuentaAtras(String texto) {
        if (lblCuentaAtras != null) {
            lblCuentaAtras.setText(texto);
        }
    }

    // Mostrar u ocultar el contador
    public void mostrarCuentaAtras(boolean visible) {
        if (sessionPanel != null) sessionPanel.setVisible(visible);
    }

    public void configurarBotonNavegacion(boolean esMenuPrincipal) {
        if (esMenuPrincipal) {
            btnNavegacion.setText("Salir");
        } else {
            btnNavegacion.setText("← Atrás");
        }
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
        
        // --- CAMBIO IMPORTANTE ---
        // Pasamos 'panelAuth' como el componente ANCLA.
        // El menú tendrá el mismo ancho y posición X que este panel.
        userSidePanel = new UserSidePanel(owner, mainFrame, panelAuth);
        userSidePanel.setVisible(true);
    }

    public void actualizarVistaLogin(Usuario usuario) {
        if (usuario != null) {
            userButton.setUsuario(usuario);
            cardLayoutAuth.show(panelAuth, "LOGIN");
        } else {
            cardLayoutAuth.show(panelAuth, "LOGOUT");
            if (userSidePanel != null) { userSidePanel.dispose(); userSidePanel = null; }
        }
    }
}
