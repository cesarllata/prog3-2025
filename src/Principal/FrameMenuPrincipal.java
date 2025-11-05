package Principal;

import java.awt.*;
import javax.swing.*;
import LogIn.LoginDialog;
import LogIn.RegisterDialog;


public class FrameMenuPrincipal extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isLoggedIn = false;
    private String currentUser = "";

    private JButton botonLogIn;
    private JButton botonSalir;
    private JLabel labelBienvenida;
    private JTextField txtUsuario;
    private JPanel panelDerecha;
    private JPanel panelCentral;
    private JPanel panelSeleccion;

    public FrameMenuPrincipal() {
        // === Configuración básica del JFrame ===
        setTitle("Estación de Buses - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Barra superior ===
        JPanel barraAlta = new JPanel(new BorderLayout());
        barraAlta.setBackground(new Color(0, 90, 158));

        // Título central
        JLabel titulo = new JLabel("ALSA Estación de Buses", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        barraAlta.add(titulo, BorderLayout.CENTER);

        // Panel izquierdo con botón Salir
        JPanel panelIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzquierda.setBackground(new Color(0, 90, 158));
        botonSalir = new JButton("Salir");
        botonSalir.setFocusPainted(false);
        botonSalir.setBackground(Color.WHITE);
        panelIzquierda.add(botonSalir);
        barraAlta.add(panelIzquierda, BorderLayout.WEST);

        // Panel derecho con botón LogIn/Usuario
        panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDerecha.setBackground(new Color(0, 90, 158));
        
        // Crear el botón de login
        botonLogIn = new JButton("LogIn");
        botonLogIn.setFocusPainted(false);
        botonLogIn.setBackground(Color.WHITE);
        
        // Crear el campo de texto para el usuario (inicialmente invisible)
        txtUsuario = new JTextField(15);
        txtUsuario.setForeground(Color.BLACK);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setEditable(false);
        txtUsuario.setHorizontalAlignment(JTextField.CENTER);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        txtUsuario.setVisible(false);
        
        panelDerecha.add(botonLogIn);
        panelDerecha.add(txtUsuario);
        barraAlta.add(panelDerecha, BorderLayout.EAST);

        add(barraAlta, BorderLayout.NORTH);

        // === Panel central ===
        panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.WHITE);

        // Texto principal
        labelBienvenida = new JLabel("¿Qué desea hacer?", SwingConstants.CENTER);
        labelBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 28));
        labelBienvenida.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        labelBienvenida.setForeground(new Color(0, 70, 120));
        panelCentral.add(labelBienvenida, BorderLayout.NORTH);

        // === Panel de opciones ===
        panelSeleccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 40));
        panelSeleccion.setBackground(Color.WHITE);

        JButton botonNuevoViaje = crearBotonOpcion("RESERVA NUEVO VIAJE", "resources/img/viaje.png");
        JButton botonDestinos = crearBotonOpcion("DESTINOS Y RUTAS", "resources/img/destinos.png");
        JButton botonBonos = crearBotonOpcion("BONOS ALSA", "resources/img/bonos.png");

        panelSeleccion.add(botonNuevoViaje);
        panelSeleccion.add(botonDestinos);
        panelSeleccion.add(botonBonos);

        panelCentral.add(panelSeleccion, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // === Acciones de botones ===
        botonSalir.addActionListener(e -> {
            if (!isLoggedIn) {
                // Si no está logueado, simplemente sale de la aplicación
                int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea salir de la aplicación?",
                    "Salir",
                    JOptionPane.YES_NO_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            } else {
                // Si está logueado, muestra opciones adicionales
                String[] opciones = {"Salir de la aplicación", "Cerrar sesión", "Cancelar"};
                int seleccion = JOptionPane.showOptionDialog(
                    this,
                    "¿Qué acción desea realizar?",
                    "Opciones de salida",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[2]
                );

                switch (seleccion) {
                    case 0: // Salir de la aplicación
                        System.exit(0);
                        break;
                    case 1: // Cerrar sesión
                        isLoggedIn = false;
                        currentUser = "";
                        txtUsuario.setVisible(false);
                        botonLogIn.setVisible(true);
                        JOptionPane.showMessageDialog(this, 
                            "Sesión cerrada correctamente", 
                            "Cierre de sesión", 
                            JOptionPane.INFORMATION_MESSAGE);
                        break;
                }
            }
        });

        botonLogIn.addActionListener(e -> {
            if (!isLoggedIn) {
                String[] opcionesLogin = {"Log", "Register", "Cancelar"};
                int sel = JOptionPane.showOptionDialog(
                    this,
                    "Seleccione una opción:",
                    "Iniciar sesión / Registrarse",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesLogin,
                    opcionesLogin[0]
                );

                if (sel == 0) { // Log
                    LoginDialog loginDialog = new LoginDialog(this);
                    loginDialog.setVisible(true);
                    if (loginDialog.isLoginExitoso()) {
                        isLoggedIn = true;
                        currentUser = loginDialog.getUsuario();
                        botonLogIn.setVisible(false);
                        txtUsuario.setText(currentUser);
                        txtUsuario.setVisible(true);
                        JOptionPane.showMessageDialog(this,
                            "Has iniciado sesión correctamente",
                            "Inicio de sesión",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (sel == 1) { // Register
                    RegisterDialog registerDialog = new RegisterDialog(this);
                    registerDialog.setVisible(true);
                    if (registerDialog.isRegistered()) {
                        isLoggedIn = true;
                        currentUser = registerDialog.getRegisteredUser();
                        botonLogIn.setVisible(false);
                        txtUsuario.setText(currentUser);
                        txtUsuario.setVisible(true);
                        JOptionPane.showMessageDialog(this,
                            "Registro completado y sesión iniciada",
                            "Registro",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                // else cancelar o cerrar -> no hacer nada
            } else {
                JOptionPane.showMessageDialog(this,
                    "Ya has iniciado sesión",
                    "Inicio de sesión",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        botonDestinos.addActionListener(e -> {
            if (!isLoggedIn) {
                LoginDialog loginDialog = new LoginDialog(this);
                loginDialog.setVisible(true);
                if (loginDialog.isLoginExitoso()) {
                    isLoggedIn = true;
                    JOptionPane.showMessageDialog(this, "Abrir módulo: Destinos y Rutas");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Abrir módulo: Destinos y Rutas");
            }
        });
        botonNuevoViaje.addActionListener(e -> {
            if (!isLoggedIn) {
                LoginDialog loginDialog = new LoginDialog(this);
                loginDialog.setVisible(true);
                if (loginDialog.isLoginExitoso()) {
                    isLoggedIn = true;
                    // Abrir diálogo de nuevo viaje
                    NuevoViajeDialog nv = new NuevoViajeDialog(this);
                    nv.setVisible(true);
                }
            } else {
                NuevoViajeDialog nv = new NuevoViajeDialog(this);
                nv.setVisible(true);
            }
        });
        botonBonos.addActionListener(e -> {
            if (!isLoggedIn) {
                LoginDialog loginDialog = new LoginDialog(this);
                loginDialog.setVisible(true);
                if (loginDialog.isLoginExitoso()) {
                    isLoggedIn = true;
                    BonosAlsaDialog bonos = new BonosAlsaDialog(this);
                    bonos.setVisible(true);
                }
            } else {
                BonosAlsaDialog bonos = new BonosAlsaDialog(this);
                bonos.setVisible(true);
            }
        });

        setVisible(true);
    }

    // === Método auxiliar para crear botones de opciones ===
    private JButton crearBotonOpcion(String texto, String iconoRuta) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(250, 180));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setBackground(new Color(0, 120, 200));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Si existe un icono, cargarlo
        try {
            ImageIcon icono = new ImageIcon(iconoRuta);
            Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(img));	
            boton.setHorizontalTextPosition(SwingConstants.CENTER);
            boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        } catch (Exception ex) {
            // si no hay imagen, no hace nada
        }

        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrameMenuPrincipal::new);
    }
}
