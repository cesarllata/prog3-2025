package Principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;

public class HeaderPanel extends JPanel {

    private MainFrame mainFrame;
    private JButton btnSalir;
    private JLabel lblEmpresa;

    // Componentes de autenticación
    private JPanel panelAuth;
    private CardLayout cardLayoutAuth;
    private JButton btnLogin;
    // reemplazamos el antiguo btnUsuario por un panel clicable más compacto
    private JPanel userPanelBtn;
    private JLabel lblUserName;
    private ProfileAvatar avatar;
    private JDialog usuarioDialog;   // panel lateral ocupado de arriba a abajo

    public HeaderPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, 80));
        this.setBackground(new Color(0, 70, 140));

        // --- Botón Salir ---
        btnSalir = new JButton("Salir");
        btnSalir.setPreferredSize(new Dimension(100, 40));
        btnSalir.addActionListener(e -> {
            if (mainFrame != null) mainFrame.solicitarSalir();
        });
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
        panelAuth.setPreferredSize(new Dimension(160, 60));

        // Botón Login (cuando no hay sesión)
        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(100, 40));
        btnLogin.addActionListener(e -> {
            if (mainFrame != null) mainFrame.mostrarPantallaLoginRegistro();
        });
        panelAuth.add(btnLogin, "LOGOUT");

        // Panel usuario (cuando hay sesión) — más estrecho y centrado con avatar circular
        userPanelBtn = createUserPanelButton();
        panelAuth.add(userPanelBtn, "LOGIN");

        this.add(panelAuth, BorderLayout.EAST);

        // Inicializar estado
        actualizarVistaLogin(null);
    }

    private JPanel createUserPanelButton() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        p.setOpaque(true);

        // usar el mismo color de fondo que btnLogin (si existe), fallback a gris claro
        Color normalBg = btnLogin != null ? btnLogin.getBackground() : new Color(230, 230, 230);
        p.setBackground(normalBg);

        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        p.setPreferredSize(new Dimension(140, 44));
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // avatar placeholder (círculo)
        avatar = new ProfileAvatar(36);
        p.add(avatar);

        // nombre centrado verticalmente
        lblUserName = new JLabel("Usuario");
        lblUserName.setFont(new Font("Arial", Font.BOLD, 14));
        lblUserName.setForeground(Color.DARK_GRAY);
        p.add(lblUserName);

        // comportamiento: click abre/cierra el panel lateral
        Color hoverBg = normalBg.brighter();
        MouseAdapter clickAndHover = new MouseAdapter() {
            Color normal = normalBg;

            @Override
            public void mouseClicked(MouseEvent e) {
                toggleUsuarioPanel();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                p.setBackground(hoverBg);
                p.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                p.setBackground(normal);
                p.repaint();
            }
        };
        p.addMouseListener(clickAndHover);
        // forward mouse events to children so clicking them triggers the same
        for (Component c : p.getComponents()) {
            c.addMouseListener(clickAndHover);
        }

        return p;
    }

    /**
     * Alterna la visibilidad del panel lateral del usuario.
     * Ahora ocupa toda la altura de la ventana (top -> bottom) y no sobresale por derecha ni por abajo.
     */
    private void toggleUsuarioPanel() {
        if (mainFrame == null) {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof MainFrame) mainFrame = (MainFrame) w;
        }

        if (usuarioDialog != null && usuarioDialog.isVisible()) {
            usuarioDialog.dispose();
            usuarioDialog = null;
            return;
        }

        Window owner = mainFrame != null ? mainFrame : SwingUtilities.getWindowAncestor(this);
        if (owner == null) return;

        usuarioDialog = new JDialog(owner);
        usuarioDialog.setUndecorated(true);
        usuarioDialog.setModal(false);
        usuarioDialog.setFocusableWindowState(true);
        usuarioDialog.setAlwaysOnTop(true);

        int preferredWidth = 280; // ancho preferido del panel lateral

        try {
            // Coordenadas en pantalla del owner (ventana principal)
            Point ownerScreen = owner.getLocationOnScreen();
            int ownerTop = ownerScreen.y;
            int ownerLeft = ownerScreen.x;
            int ownerRight = ownerLeft + owner.getWidth();
            int ownerBottom = ownerTop + owner.getHeight();

            // Width: no mayor al ancho de la ventana
            int width = Math.min(preferredWidth, owner.getWidth());
            // x: pegar al borde derecho de la ventana, sin sobresalir por la izquierda
            int x = ownerRight - width;
            if (x < ownerLeft) x = ownerLeft;

            // y: top de la ventana principal (ocupa desde la parte superior hasta la inferior)
            int y = ownerTop;
            int height = ownerBottom - ownerTop;
            if (height < 0) height = 0;

            usuarioDialog.setBounds(x, y, width, height);
        } catch (IllegalComponentStateException ex) {
            // Fallback si no está mostrado: posicionar respecto al owner bounds
            int width = Math.min(preferredWidth, owner.getWidth());
            int x = owner.getX() + owner.getWidth() - width;
            if (x < owner.getX()) x = owner.getX();
            int y = owner.getY();
            int height = owner.getHeight();
            usuarioDialog.setBounds(x, y, width, height);
        }

        // construir contenido del panel lateral
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(245, 245, 245));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Cuenta");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 12)));

        JButton miPerfil = new JButton("Perfil");
        miPerfil.setAlignmentX(Component.RIGHT_ALIGNMENT);
        miPerfil.addActionListener(e -> {
            if (mainFrame != null) {
                mainFrame.mostrarPanel("PERFIL");
            }
            usuarioDialog.dispose();
        });
        content.add(miPerfil);
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        JButton miConfig = new JButton("Configuración");
        miConfig.setAlignmentX(Component.RIGHT_ALIGNMENT);
        miConfig.addActionListener(e -> {
            if (mainFrame != null) mainFrame.mostrarPanel("CONFIG");
            usuarioDialog.dispose();
        });
        content.add(miConfig);
        content.add(Box.createVerticalGlue());

        JSeparator sep = new JSeparator();
        sep.setAlignmentX(Component.RIGHT_ALIGNMENT);
        content.add(sep);
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        JButton miCerrar = new JButton("Cerrar sesión");
        miCerrar.setAlignmentX(Component.RIGHT_ALIGNMENT);
        miCerrar.addActionListener(e -> {
            if (mainFrame != null) {
                try {
                    Method m = mainFrame.getClass().getMethod("cerrarSesion");
                    m.invoke(mainFrame);
                } catch (Exception ignored) {
                }
                try {
                    mainFrame.mostrarPantallaLoginRegistro();
                } catch (Exception ignored) {
                }
            }
            usuarioDialog.dispose();
        });
        content.add(miCerrar);

        usuarioDialog.setContentPane(content);

        usuarioDialog.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                usuarioDialog.dispose();
            }
        });

        usuarioDialog.setVisible(true);
    }

    /**
     * Actualiza el panel de autenticación según el usuario actual.
     * Ajusta nombre y avatar.
     */
    public void actualizarVistaLogin(Usuario usuario) {
        if (usuario != null) {
            String nombre = usuario.getNombre() != null ? usuario.getNombre() : "Usuario";
            // actualizar etiqueta e iniciales del avatar
            lblUserName.setText(nombre);
            avatar.setInitials(extractInitials(nombre));
            cardLayoutAuth.show(panelAuth, "LOGIN");
        } else {
            cardLayoutAuth.show(panelAuth, "LOGOUT");
            if (usuarioDialog != null) {
                usuarioDialog.dispose();
                usuarioDialog = null;
            }
        }
    }

    private String extractInitials(String nombre) {
        if (nombre == null || nombre.isBlank()) return "";
        String[] parts = nombre.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }
        return ("" + parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
    }

    /**
     * Pequeño componente circular para avatar/placeholder.
     */
    private static class ProfileAvatar extends JComponent {
        private final int size;
        private String initials = "";

        ProfileAvatar(int size) {
            this.size = size;
            setPreferredSize(new Dimension(size, size));
            setMinimumSize(new Dimension(size, size));
            setOpaque(false);
        }

        void setInitials(String initials) {
            this.initials = initials != null ? initials : "";
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // fondo circular
                g2.setColor(new Color(100, 150, 240));
                g2.fillOval(0, 0, size - 1, size - 1);
                // borde
                g2.setColor(new Color(70, 110, 210));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(0, 0, size - 1, size - 1);
                // iniciales
                if (!initials.isEmpty()) {
                    g2.setColor(Color.WHITE);
                    Font font = g2.getFont().deriveFont(Font.BOLD, Math.max(10, size / 2f));
                    g2.setFont(font);
                    FontMetrics fm = g2.getFontMetrics();
                    int tx = (size - fm.stringWidth(initials)) / 2;
                    int ty = (size + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(initials, tx, ty);
                }
            } finally {
                g2.dispose();
            }
        }
    }
}
