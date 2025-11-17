package Principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private HeaderPanel headerPanel;
    private Usuario usuarioActual;
    private String panelActual;

    public MainFrame() {
        setTitle("Gestión de Estación de Autobuses");
        
        // --- CAMBIO IMPORTANTE: Cierre directo al pulsar la X ---
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        

        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // Configuración de atajos (Ctrl + W)
        configurarAtajoGlobal();

        // Header
        headerPanel = new HeaderPanel(this);
        this.add(headerPanel, BorderLayout.NORTH);

        // Contenedor central
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // Paneles
        MenuPrincipalPanel menuPanel = new MenuPrincipalPanel(this);
        LoginPanel loginPanel = new LoginPanel(this);
        BonosPanel bonosPanel = new BonosPanel(this);
        NuevaReservaPanel reservaPanel = new NuevaReservaPanel(this);
        RegistroPanel registroPanel = new RegistroPanel(this);
        GestorRutas gestorRutas = new GestorRutas(this);

        panelContenedor.add(menuPanel, "MENU");
        panelContenedor.add(loginPanel, "LOGIN");
        panelContenedor.add(registroPanel, "REGISTRO");
        panelContenedor.add(gestorRutas, "RUTAS");
        panelContenedor.add(bonosPanel, "BONOS");
        panelContenedor.add(reservaPanel, "RESERVA");

        this.add(panelContenedor, BorderLayout.CENTER);

        this.usuarioActual = null;
        mostrarPanel("MENU");
    }

    private void configurarAtajoGlobal() {
        JPanel contentPane = (JPanel) this.getContentPane();
        InputMap im = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = contentPane.getActionMap();
        
        // Ctrl + W (o Cmd + W) para volver al menú
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
    }

    public void loginExitoso(Usuario usuario) {
        this.usuarioActual = usuario;
        headerPanel.actualizarVistaLogin(usuario);
        mostrarPanel("MENU");
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
        headerPanel.actualizarVistaLogin(null);
        JOptionPane.showMessageDialog(this, "Sesión cerrada correctamente.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        mostrarPanel("MENU");
    }

    // Este método se sigue usando para los botones internos de "Salir", 
    // pero ya NO salta al pulsar la X de la ventana.
    public void solicitarSalir() {
        if (!"MENU".equals(panelActual)) {
            String[] opciones = {"Volver al Menú", "Salir de la Aplicación", "Cancelar"};
            int opcion = JOptionPane.showOptionDialog(this, 
                "¿Qué deseas hacer?", "Navegación", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, 
                null, opciones, opciones[0]);
            if (opcion == 0) mostrarPanel("MENU");
            else if (opcion == 1) System.exit(0);
            return;
        }

        if (usuarioActual == null) {
            int opcion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres salir?", "Salir", JOptionPane.YES_NO_OPTION);
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
        String[] opciones = {"Iniciar Sesión", "Registrarse", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(this, 
            "¿Qué deseas hacer?", 
            "Acceso a la cuenta", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, opciones, opciones[0]);
        
        switch (eleccion) {
            case 0 -> mostrarPanel("LOGIN");
            case 1 -> mostrarPanel("REGISTRO");
            default -> {}
        }
    }
}