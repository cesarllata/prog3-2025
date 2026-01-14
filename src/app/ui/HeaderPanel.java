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

    public HeaderPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, 80));
        this.setBackground(new Color(0, 70, 140));

        // Botón Izquierdo
        btnNavegacion = new JButton("Salir");
        btnNavegacion.setPreferredSize(new Dimension(100, 40));
        btnNavegacion.addActionListener(e -> mainFrame.accionBotonNavegacion());
        this.add(btnNavegacion, BorderLayout.WEST);

        // Título Central
        lblEmpresa = new JLabel("DEUSTO BUS F.R.");
        lblEmpresa.setFont(new Font("Arial", Font.BOLD, 26));
        lblEmpresa.setForeground(Color.WHITE);
        lblEmpresa.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblEmpresa, BorderLayout.CENTER);

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
        panelAuth.add(userButton, "LOGIN");

        // Añadimos un margen derecho para que no pegue con el borde de la ventana
        JPanel wrapperRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        wrapperRight.setOpaque(false);
        // Texto previo a la cuenta atrás
        lblExpiraTexto = new JLabel("La sesión expira en:");
        lblExpiraTexto.setForeground(Color.WHITE);
        lblExpiraTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        lblExpiraTexto.setVisible(true);
        lblExpiraTexto.setPreferredSize(new Dimension(160, 40));
        wrapperRight.add(lblExpiraTexto);

        // Label de cuenta atrás
        lblCuentaAtras = new JLabel("01:00:00");
        lblCuentaAtras.setForeground(Color.WHITE);
        lblCuentaAtras.setFont(new Font("Arial", Font.BOLD, 14));
        // Mostrar por defecto para que sea visible en la pantalla de inicio
        lblCuentaAtras.setVisible(true);
        lblCuentaAtras.setPreferredSize(new Dimension(100, 40));
        wrapperRight.add(lblCuentaAtras);
    wrapperRight.add(panelAuth);
        
        this.add(wrapperRight, BorderLayout.EAST);
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
        if (lblCuentaAtras != null) lblCuentaAtras.setVisible(visible);
        if (lblExpiraTexto != null) lblExpiraTexto.setVisible(visible);
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
