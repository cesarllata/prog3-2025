package app.ui.components;

import app.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/* Uso de IAG para acelerar tareas de desarrollo */
public class UserSidePanel extends JDialog {

    private final Color COLOR_HOVER = new Color(230, 240, 255);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    private final Color COLOR_ROJO = new Color(200, 50, 50);

    public UserSidePanel(Window owner, MainFrame mainFrame, Component anchor) {
        super(owner);
        setUndecorated(true);
        setModal(false); 
        setFocusableWindowState(true);
        
        buildContent(mainFrame);
        
        // Alineamos el panel justo debajo del botón "anchor" (el botón de usuario)
        alinearConAncla(owner, anchor);
        
        // Si pinchas fuera, se cierra el menú
        addWindowFocusListener(new WindowAdapter() {
            @Override public void windowLostFocus(WindowEvent e) { 
                // Evitamos que se cierre si el foco va al JOptionPane de confirmación
                // (Esto a veces es tricky en Swing, pero con JDialog modal interno suele ir bien)
            }
        });
        
        // Listener global de ratón para cerrar si clicamos fuera (más robusto que focus)
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent && event.getID() == MouseEvent.MOUSE_PRESSED) {
                MouseEvent me = (MouseEvent) event;
                if (isVisible() && !getBounds().contains(me.getLocationOnScreen())) {
                    // Verificamos que no sea un componente interno del dialogo (como el JOptionPane)
                    Window w = SwingUtilities.windowForComponent((Component)me.getSource());
                    if (w != this) {
                        dispose();
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }

    private void buildContent(MainFrame mainFrame) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        // --- CABECERA ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        header.setBackground(Color.WHITE);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JLabel title = new JLabel("MI CUENTA");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(0, 70, 140));
        header.add(title);
        content.add(header);
        
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230,230,230));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        content.add(sep);

        // --- BOTONES ---
        
        JButton btnPerfil = crearBotonMenu("👤  Perfil");
        btnPerfil.addActionListener(e -> {
            if (mainFrame != null) mainFrame.mostrarPerfilConPestana(0);
            dispose();
        });

        JButton btnHistorial = crearBotonMenu("📜  Historial");
        btnHistorial.addActionListener(e -> {
            if (mainFrame != null) mainFrame.mostrarPerfilConPestana(1);
            dispose();
        });

        JButton btnLogout = crearBotonMenu("🚪  Cerrar Sesión");
        btnLogout.setForeground(COLOR_ROJO);
        
        // --- AQUÍ ESTÁ LA LÓGICA QUE PEDISTE ---
        btnLogout.addActionListener(e -> {
            // Usamos 'this' como padre para que el aviso salga encima del menú
            int opcion = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que quieres cerrar la sesión?", 
                "Cerrar Sesión", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                if (mainFrame != null) {
                    mainFrame.cerrarSesion();
                }
                dispose(); // Cerramos el menú lateral
            }
            // Si elige NO, no hacemos nada y el menú sigue abierto
        });

        content.add(btnPerfil);
        content.add(btnHistorial);
        
        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(230,230,230));
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        content.add(sep2);
        
        content.add(btnLogout);
        content.add(Box.createVerticalGlue());

        setContentPane(content);
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 5));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_HOVER); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(Color.WHITE); }
        });
        return btn;
    }

    private void alinearConAncla(Window owner, Component anchor) {
        try {
            Point pAnchor = anchor.getLocationOnScreen();
            
            int width = anchor.getWidth(); // Mismo ancho que el botón Login
            int x = pAnchor.x;
            int y = pAnchor.y + anchor.getHeight() + 2; 
            
            // Calculamos altura disponible hasta el fondo de la ventana
            Point pOwner = owner.getLocationOnScreen();
            int ownerBottom = pOwner.y + owner.getHeight();
            int marginBottom = 20; 
            
            int height = ownerBottom - y - marginBottom;
            
            // Altura mínima y máxima estética
            if (height < 150) height = 150;
            if (height > 220) height = 220; // Limitamos alto para que quede compacto

            setBounds(x, y, width, height);
            
        } catch (IllegalComponentStateException ex) {
            setSize(200, 300);
            setLocationRelativeTo(owner);
        }
    }
}
