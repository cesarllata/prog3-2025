package Principal;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

/**
 * Diálogo que muestra una lista de "Bonos ALSA" en un JTable con varios renderizados.
 */
public class BonosAlsaDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel model;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BonosAlsaDialog(Frame parent) {
        super(parent, "Bonos ALSA", true);
        initComponents();
        setSize(800, 420);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        String[] columns = new String[]{"ID", "Bono", "Descripción", "Precio (€)", "Vigencia", "Activo", "Preview"};
        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo permitir edición de columna 'Activo' como ejemplo
                return column == 5;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class;
                    case 3: return Double.class;
                    case 4: return LocalDate.class;
                    case 5: return Boolean.class;
                    case 6: return Icon.class;
                    default: return String.class;
                }
            }
        };

        table = new JTable(model);
        table.setRowHeight(60);
        table.setFillsViewportHeight(true);

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 90, 158));
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));

        // Striping rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 252));
                return c;
            }
        });

        // Precio renderer (formateo y color según valor)
        table.getColumnModel().getColumn(3).setCellRenderer(new PriceRenderer());

        // Fecha renderer
        table.getColumnModel().getColumn(4).setCellRenderer(new DateRenderer());

        // Boolean renderer (activo) con color/ícono
        table.getColumnModel().getColumn(5).setCellRenderer(new ActiveRenderer());

        // Descripción renderer (wrap)
        table.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer());

        // Preview renderer (colored icon)
        table.getColumnModel().getColumn(6).setCellRenderer(new IconPreviewRenderer());

        JScrollPane scroll = new JScrollPane(table);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnComprar = new JButton("Comprar");
        JButton btnAgregar = new JButton("Agregar Bono");
        JButton btnCerrar = new JButton("Cerrar");
        botones.add(btnAgregar);
        botones.add(btnComprar);
        botones.add(btnCerrar);

        btnCerrar.addActionListener(e -> dispose());
        btnAgregar.addActionListener(e -> addSampleBono());
        btnComprar.addActionListener(e -> comprarSeleccion());

        add(scroll, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        // Rellenar con varios bonos de ejemplo
        for (int i = 0; i < 6; i++) addSampleBono();
    }

    private void comprarSeleccion() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un bono para comprar.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String nombre = String.valueOf(model.getValueAt(row, 1));
        Double precio = (Double) model.getValueAt(row, 3);
        JOptionPane.showMessageDialog(this, "Has comprado: " + nombre + " por €" + String.format("%.2f", precio));
    }

    private void addSampleBono() {
        // Crear datos de ejemplo variados
        int id = model.getRowCount() + 1;
        String nombre = switch (id % 5) {
            case 0 -> "Bono Estudiantil";
            case 1 -> "Bono Fin de Semana";
            case 2 -> "Bono Ida/Vuelta";
            case 3 -> "Bono Mensual";
            default -> "Bono Descuento";
        };
        String desc = "Valido para rutas seleccionadas. Ofrece descuento progresivo según número de viajes. Incluye condiciones.";
        double precio = 5 + (id * 2.5);
        LocalDate vig = LocalDate.now().plusDays(15 + id * 10);
        boolean activo = id % 3 != 0;
        Icon icon = new ColorIcon(new Color(50 + (id * 20) % 200, 120 + (id * 30) % 100, 150));

        Vector<Object> row = new Vector<>();
        row.add(id);
        row.add(nombre);
        row.add(desc);
        row.add(precio);
        row.add(vig);
        row.add(activo);
        row.add(icon);
        model.addRow(row);
    }

    // ---- Renderers ----

    private class PriceRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Double d) {
                setText(String.format("€ %.2f", d));
                if (d > 20) setForeground(new Color(180, 30, 30)); else setForeground(new Color(20, 90, 20));
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }

    private class DateRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof LocalDate d) {
                value = fmt.format(d);
            }
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }

    private class ActiveRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            boolean active = Boolean.TRUE.equals(value);
            lbl.setText(active ? "Activo" : "No activo");
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setForeground(active ? new Color(0, 120, 0) : Color.GRAY);
            return lbl;
        }
    }

    private class TextAreaRenderer extends JTextArea implements TableCellRenderer {
        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setFont(table.getFont().deriveFont(12f));
            setBackground(isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? Color.WHITE : new Color(245,248,252)));
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setSize(table.getColumnModel().getColumn(column).getWidth(), Short.MAX_VALUE);
            int prefH = getPreferredSize().height;
            if (table.getRowHeight(row) != prefH) table.setRowHeight(row, prefH);
            return this;
        }
    }

    private class IconPreviewRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setText("");
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            if (value instanceof Icon ic) lbl.setIcon(ic); else lbl.setIcon(null);
            return lbl;
        }
    }

    private static class ColorIcon implements Icon {
        private final Color color;
        private final int w = 48, h = 48;
        public ColorIcon(Color color) { this.color = color; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.fillRoundRect(x+4, y+4, w-8, h-8, 8, 8);
            g2.setColor(color.darker());
            g2.drawRoundRect(x+4, y+4, w-8, h-8, 8, 8);
            g2.dispose();
        }
        @Override public int getIconWidth() { return w; }
        @Override public int getIconHeight() { return h; }
    }
}
