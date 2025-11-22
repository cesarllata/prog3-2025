package Principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;

/**
 * Panel lateral reutilizable que ocupa la altura completa de la ventana principal.
 * Recibe MainFrame para poder navegar / cerrar sesión.
 */
public class UserSidePanel extends JDialog {

    public UserSidePanel(Window owner, int preferredWidth, MainFrame mainFrame) {
        super(owner);
        setUndecorated(true);
        setModal(false);
        setFocusableWindowState(true);
        setAlwaysOnTop(true);
        buildContent(mainFrame);
        positionFullHeight(owner, preferredWidth);
        addWindowFocusListener(new WindowAdapter() {
            @Override public void windowLostFocus(WindowEvent e) { dispose(); }
        });
    }

    private void buildContent(MainFrame mainFrame) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(245, 245, 245));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Cuenta");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 12)));

        JButton perfil = new JButton("Perfil");
        perfil.setAlignmentX(Component.RIGHT_ALIGNMENT);
        perfil.addActionListener(e -> {
            if (mainFrame != null) mainFrame.mostrarPanel("PERFIL");
            dispose();
        });
        content.add(perfil);
        content.add(Box.createRigidArea(new Dimension(0,8)));

        JButton config = new JButton("Configuración");
        config.setAlignmentX(Component.RIGHT_ALIGNMENT);
        config.addActionListener(e -> {
            if (mainFrame != null) mainFrame.mostrarPanel("CONFIG");
            dispose();
        });
        content.add(config);
        content.add(Box.createVerticalGlue());

        content.add(new JSeparator());
        content.add(Box.createRigidArea(new Dimension(0,8)));

        JButton cerrar = new JButton("Cerrar sesión");
        cerrar.setAlignmentX(Component.RIGHT_ALIGNMENT);
        cerrar.addActionListener(e -> {
            if (mainFrame != null) {
                try {
                    Method m = mainFrame.getClass().getMethod("cerrarSesion");
                    m.invoke(mainFrame);
                } catch (Exception ignored) {}
                try { mainFrame.mostrarPantallaLoginRegistro(); } catch (Exception ignored) {}
            }
            dispose();
        });
        content.add(cerrar);

        setContentPane(content);
    }

    private void positionFullHeight(Window owner, int preferredWidth) {
        try {
            Point s = owner.getLocationOnScreen();
            int ownerTop = s.y;
            int ownerLeft = s.x;
            int ownerRight = ownerLeft + owner.getWidth();
            int ownerBottom = ownerTop + owner.getHeight();

            int width = Math.min(preferredWidth, owner.getWidth());
            int x = ownerRight - width;
            if (x < ownerLeft) x = ownerLeft;
            int y = ownerTop;
            int height = ownerBottom - ownerTop;
            setBounds(x, y, width, Math.max(0, height));
        } catch (IllegalComponentStateException ex) {
            Rectangle b = owner.getBounds();
            int width = Math.min(preferredWidth, b.width);
            int x = b.x + b.width - width;
            setBounds(x, b.y, width, b.height);
        }
    }
}