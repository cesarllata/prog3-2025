package app.ui;

import app.models.Usuario;
import app.ui.components.UserButton;
import app.ui.components.UserSidePanel;
import javax.swing.*;
import java.awt.*;

/* Uso de IAG para acelerar tareas de desarrollo */
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
    private JPanel bottomPanel;

    public HeaderPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    this.setLayout(new BorderLayout());
    // Altura del header: ajustada para eliminar franja vacía bajo la línea separadora
    this.setPreferredSize(new Dimension(0, 80));
        this.setBackground(new Color(0, 70, 140));

        // Estética: tamaños uniformes y padding
        Dimension botonSize = new Dimension(120, 40);
        Dimension sideWrapperSize = new Dimension(220, 60);

        // Botón Izquierdo
        btnNavegacion = new JButton("Salir");
        btnNavegacion.setPreferredSize(botonSize);
        btnNavegacion.setFocusPainted(false);
        btnNavegacion.setBackground(new Color(255,255,255));
        btnNavegacion.setForeground(new Color(0,70,140));
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
    panelAuth.setPreferredSize(sideWrapperSize);

        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(botonSize);
        btnLogin.setFocusPainted(false);
        btnLogin.setBackground(new Color(255,255,255));
        btnLogin.setForeground(new Color(0,70,140));
        btnLogin.addActionListener(e -> { 
            if (mainFrame != null) mainFrame.mostrarPanel("LOGIN"); 
        });
        panelAuth.add(btnLogin, "LOGOUT");

    userButton = new UserButton();
    userButton.setPreferredSize(botonSize);
    userButton.setBackground(btnLogin.getBackground());
    userButton.setForeground(btnLogin.getForeground());
    userButton.addActionListener(ae -> toggleUserSidePanel());

    // Construir el panel que se mostrará en el estado LOGIN: sólo el userButton
    JPanel loginCard = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 10));
    loginCard.setOpaque(false);
    loginCard.add(userButton);
    panelAuth.add(loginCard, "LOGIN");

            // Construir la fila superior: izquierdo (botón), centro (título), derecho (auth)
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);

            // Ajustar botón izquierdo dentro de un wrapper para controlar márgenes
            JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 18));
            leftWrapper.setOpaque(false);
            leftWrapper.setPreferredSize(sideWrapperSize);
            leftWrapper.add(btnNavegacion);
            topPanel.add(leftWrapper, BorderLayout.WEST);

            // Columna central: título encima y contador debajo (alineados)
            JPanel centerColumn = new JPanel();
            centerColumn.setOpaque(false);
            centerColumn.setLayout(new BoxLayout(centerColumn, BoxLayout.Y_AXIS));
            // Alinear componentes al centro dentro de la columna
            lblEmpresa.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerColumn.add(lblEmpresa);
            // Añadimos un pequeño espacio entre título y contador
            centerColumn.add(Box.createVerticalStrut(6));
            // bottomPanel se añadirá más abajo (ya existe) — lo moveremos al centroColumn
            topPanel.add(centerColumn, BorderLayout.CENTER);

            // Panel derecho (auth)
            JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            rightWrapper.setOpaque(false);
            rightWrapper.setPreferredSize(sideWrapperSize);
            rightWrapper.add(panelAuth);
            topPanel.add(rightWrapper, BorderLayout.EAST);

            // Añadir borde inferior para separar visualmente el header
            topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200,200,200)));
            // Añadir topPanel en CENTER para que ocupe todo el area del header
            this.add(topPanel, BorderLayout.CENTER);

        // Fila inferior centrada: texto + contador (oculto por defecto)
    bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        bottomPanel.setOpaque(false);

    lblExpiraTexto = new JLabel("La sesión expira en:");
    lblExpiraTexto.setForeground(Color.WHITE);
    lblExpiraTexto.setFont(new Font("Arial", Font.PLAIN, 13));
        bottomPanel.add(lblExpiraTexto);

    lblCuentaAtras = new JLabel("01:00:00");
    lblCuentaAtras.setForeground(Color.WHITE);
    lblCuentaAtras.setFont(new Font("Arial", Font.BOLD, 13));
        bottomPanel.add(lblCuentaAtras);

        bottomPanel.setVisible(true); // mostrar desde el inicio
        // Añadir el bottomPanel directamente a la columna central creada arriba.
        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerColumn.add(bottomPanel);

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
        if (bottomPanel != null) bottomPanel.setVisible(visible);
        // Forzar re-layout y repaint para asegurar que se muestra correctamente
        this.revalidate();
        this.repaint();
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
