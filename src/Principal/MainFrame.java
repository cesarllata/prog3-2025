package Principal;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private HeaderPanel headerPanel;
    private Usuario usuarioActual;

    public MainFrame() {
        setTitle("Gestión de Estación de Autobuses");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

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
        NuevaReservaPanel reservaPanel = new NuevaReservaPanel();
        RegistroPanel registroPanel = new RegistroPanel(this);

        JPanel rutasPanel = new JPanel();
        rutasPanel.add(new JLabel("Aquí irá la gestión de RUTAS"));

        // Añadir paneles al contenedor
        panelContenedor.add(menuPanel, "MENU");
        panelContenedor.add(loginPanel, "LOGIN");
        panelContenedor.add(registroPanel, "REGISTRO");
        panelContenedor.add(rutasPanel, "RUTAS");
        panelContenedor.add(bonosPanel, "BONOS");
        panelContenedor.add(reservaPanel, "RESERVA");

        this.add(panelContenedor, BorderLayout.CENTER);

        this.usuarioActual = null;
        mostrarPanel("MENU");
    }

    public void mostrarPanel(String nombrePanel) {
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
        mostrarPanel("MENU");
    }

    public void solicitarSalir() {
        if (usuarioActual == null) {
            int opcion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres salir?", "Salir", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) System.exit(0);
        } else {
            String[] opciones = {"Cerrar Sesión", "Salir de la Aplicación", "Cancelar"};
            int opcion = JOptionPane.showOptionDialog(this, "¿Qué deseas hacer?", "Salir", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[2]);
            switch (opcion) {
                case 0 -> cerrarSesion();
                case 1 -> System.exit(0);
                default -> {}
            }
        }
    }

    public void mostrarPantallaLoginRegistro() {
        String[] opciones = {"Iniciar Sesión", "Registrarse", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Qué deseas hacer?", "Acceso a la cuenta", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        switch (eleccion) {
            case 0 -> mostrarPanel("LOGIN");
            case 1 -> mostrarPanel("REGISTRO");
            default -> {}
        }
    }
}
