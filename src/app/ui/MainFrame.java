package app.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private HeaderPanel headerPanel;
    private app.models.Usuario usuarioActual;
    private String panelActual;
    
    // Necesitamos referencia a este panel para cambiar pestañas
    private PerfilUsuarioPanel perfilPanel;

    public MainFrame() {
        setTitle("Gestión de Estación de Autobuses");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        configurarAtajoGlobal();

        headerPanel = new HeaderPanel(this);
        this.add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        MenuPrincipalPanel menuPanel = new MenuPrincipalPanel(this);
        LoginPanel loginPanel = new LoginPanel(this);
        BonosPanel bonosPanel = new BonosPanel(this);
        NuevaReservaPanel reservaPanel = new NuevaReservaPanel(this);
        RegistroPanel registroPanel = new RegistroPanel(this);
        GestorRutas gestorRutas = new GestorRutas(this);
        
        // Inicializamos el panel de perfil como atributo
        perfilPanel = new PerfilUsuarioPanel(this);

        panelContenedor.add(menuPanel, "MENU");
        panelContenedor.add(loginPanel, "LOGIN");
        panelContenedor.add(registroPanel, "REGISTRO");
        panelContenedor.add(gestorRutas, "RUTAS");
        panelContenedor.add(bonosPanel, "BONOS");
        panelContenedor.add(reservaPanel, "RESERVA");
        panelContenedor.add(perfilPanel, "PERFIL");

        this.add(panelContenedor, BorderLayout.CENTER);

        this.usuarioActual = null;
        mostrarPanel("MENU");
    }

    // MÉTODO NUEVO: Muestra el perfil y selecciona la pestaña correcta
    public void mostrarPerfilConPestana(int indiceTab) {
        mostrarPanel("PERFIL");
        if (perfilPanel != null) {
            perfilPanel.seleccionarPestana(indiceTab);
        }
    }

    private void configurarAtajoGlobal() {
        JPanel contentPane = (JPanel) this.getContentPane();
        InputMap im = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = contentPane.getActionMap();
        KeyStroke closeKey = KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        im.put(closeKey, "volverAlMenu");
        am.put("volverAlMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panelActual != null && !panelActual.equals("MENU")) {
                    mostrarPanel("MENU");
                }
            }
        });
    }

    public void mostrarPanel(String nombrePanel) {
        this.panelActual = nombrePanel;
        cardLayout.show(panelContenedor, nombrePanel);
        boolean esMenu = "MENU".equals(nombrePanel);
        headerPanel.configurarBotonNavegacion(esMenu);
    }

    public void accionBotonNavegacion() {
        if ("MENU".equals(panelActual)) {
            solicitarSalirApp();
        } else {
            mostrarPanel("MENU");
        }
    }

    public app.models.Usuario getUsuarioActual() { return usuarioActual; }

    public void loginExitoso(app.models.Usuario usuario) {
        this.usuarioActual = usuario;
        headerPanel.actualizarVistaLogin(usuario);
        mostrarPanel("MENU");
    }

    public void actualizarSaldoHeader() {
        if (usuarioActual != null && headerPanel != null) {
            headerPanel.actualizarVistaLogin(usuarioActual);
        }
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
        headerPanel.actualizarVistaLogin(null);
        JOptionPane.showMessageDialog(this, "Sesión cerrada.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        mostrarPanel("MENU");
    }

    public void solicitarSalirApp() {
        if (usuarioActual == null) {
            int opcion = JOptionPane.showConfirmDialog(this, "¿Salir de la aplicación?", "Salir", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) System.exit(0);
        } else {
            String[] opciones = {"Cerrar Sesión", "Salir de la Aplicación", "Cancelar"};
            int opcion = JOptionPane.showOptionDialog(this, 
                "Usuario: " + usuarioActual.getNombre(), "Salir", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, 
                null, opciones, opciones[2]);
            if (opcion == 0) cerrarSesion();
            else if (opcion == 1) System.exit(0);
        }
    }

    public void mostrarPantallaLoginRegistro() {
        mostrarPanel("LOGIN");
    }
    
    public void solicitarSalir() {
        accionBotonNavegacion();
    }
}
