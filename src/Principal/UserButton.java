package Principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Componente clicable compacto: avatar circular + nombre.
 * Exponer setUsername(...) y addActionListener(...).
 */
public class UserButton extends JPanel {
    private final ProfileAvatar avatar;
    private final JLabel lblName;
    private final List<ActionListener> listeners = new CopyOnWriteArrayList<>();

    public UserButton() {
        super(new FlowLayout(FlowLayout.CENTER, 8, 8));
        setOpaque(true);
        setPreferredSize(new Dimension(140, 44));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        avatar = new ProfileAvatar(36);
        add(avatar);

        lblName = new JLabel("Usuario");
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblName.setForeground(Color.DARK_GRAY);
        add(lblName);

        // Hover visual
        MouseAdapter hover = new MouseAdapter() {
            private final Color normal = getBackground();
            private final Color hoverCol = normal.brighter();
            @Override public void mouseEntered(MouseEvent e) { setBackground(hoverCol); repaint(); }
            @Override public void mouseExited(MouseEvent e) { setBackground(normal); repaint(); }
            @Override public void mouseClicked(MouseEvent e) { fireActionEvent(); }
        };
        addMouseListener(hover);
        for (Component c : getComponents()) c.addMouseListener(hover);
    }

    public void setUsername(String name) {
        lblName.setText(name != null ? name : "Usuario");
        avatar.setInitials(extractInitials(name));
    }

    public void addActionListener(ActionListener l) {
        if (l != null) listeners.add(l);
    }

    private void fireActionEvent() {
        ActionEvent ev = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "click");
        for (ActionListener l : listeners) l.actionPerformed(ev);
    }

    private String extractInitials(String nombre) {
        if (nombre == null || nombre.isBlank()) return "";
        String[] parts = nombre.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        return ("" + parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
    }

    // Avatar interno
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
                g2.setColor(new Color(100, 150, 240));
                g2.fillOval(0, 0, size - 1, size - 1);
                g2.setColor(new Color(70, 110, 210));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(0, 0, size - 1, size - 1);
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