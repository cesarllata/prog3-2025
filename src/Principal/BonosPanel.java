package Principal;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BonosPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable tablaBonos;
    private BonosTableModel bonosModel;
    private JButton btnComprar;

    private JTable tablaDescripcion;
    private DefaultTableModel descripcionModel;
    private JScrollPane scrollDescripcion;

    public BonosPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // --- Header ---
        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setLayout(new GridBagLayout());
        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.LIGHT_GRAY));
        
        JLabel lblTitulo = new JLabel("Bonos y Abonos Disponibles");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(40, 40, 40));
        
        header.add(lblTitulo);
        add(header, BorderLayout.NORTH);

        // --- Datos ---
        List<Bono> listaBonos = new ArrayList<>();
        listaBonos.add(new Bono(1, "Bono Mensual", "30 días", 45.00, "Viajes ilimitados", "hasta 31/12/2025"));
        listaBonos.add(new Bono(2, "Bono 10 Viajes", "10 viajes", 12.50, "Multipersonal", "hasta 31/12/2025"));
        listaBonos.add(new Bono(3, "Abono Joven", "Mensual (<26 años)", 25.00, "Tarifa reducida", "hasta 31/12/2025"));
        listaBonos.add(new Bono(4, "Bono Turista", "3 días", 18.00, "72 horas ilimitadas", "hasta 31/12/2025"));
        listaBonos.add(new Bono(5, "Abono Verano", "Junio-Agosto", 60.00, "Especial temporada", "hasta 31/08/2025"));

        // --- Modelo Principal ---
        bonosModel = new BonosTableModel(listaBonos);
        tablaBonos = new JTable(bonosModel);

        tablaBonos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaBonos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaBonos.setRowHeight(28);
        tablaBonos.setAutoCreateRowSorter(true);
        tablaBonos.getTableHeader().setReorderingAllowed(false);
        tablaBonos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        tablaBonos.setDefaultRenderer(Object.class, new BonoRowRenderer());
        tablaBonos.setDefaultRenderer(Integer.class, new BonoRowRenderer());
        tablaBonos.setDefaultRenderer(Double.class, new BonoRowRenderer());

        JScrollPane scrollPane = new JScrollPane(tablaBonos);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // --- Panel Inferior ---
        JPanel panelSur = new JPanel();
        panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.Y_AXIS));
        panelSur.setBackground(new Color(245, 247, 250));

        // Tabla Descripción
        String[] columnasDesc = {"ID", "Descripción Detallada"};
        
        descripcionModel = new DefaultTableModel(columnasDesc, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaDescripcion = new JTable(descripcionModel);
        tablaDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaDescripcion.setRowHeight(90); // Altura para texto largo
        tablaDescripcion.getTableHeader().setReorderingAllowed(false);
        tablaDescripcion.getColumnModel().getColumn(0).setMaxWidth(60);
        tablaDescripcion.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaDescripcion.setDefaultRenderer(Object.class, new DescripcionRowRenderer());

        scrollDescripcion = new JScrollPane(tablaDescripcion);
        scrollDescripcion.setPreferredSize(new Dimension(0, 250)); // Grande, como pediste
        scrollDescripcion.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        panelSur.add(scrollDescripcion);

        // Botón comprar
        btnComprar = new JButton("COMPRAR SELECCIONADO");
        btnComprar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnComprar.setBackground(new Color(0, 70, 140));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnComprar.addActionListener(e -> comprarBonoSeleccionado());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(245, 247, 250));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelBotones.add(btnComprar);

        panelSur.add(panelBotones);
        add(panelSur, BorderLayout.SOUTH);

        // --- LISTENER CON TEXTOS LARGOS RESTAURADOS ---
        tablaBonos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaVista = tablaBonos.getSelectedRow();

                if (filaVista != -1) {
                    int filaModelo = tablaBonos.convertRowIndexToModel(filaVista);
                    Bono b = bonosModel.getBonoAt(filaModelo);

                    // Usamos HTML para permitir saltos de línea automáticos y formateo
                    String textoExtra = switch (b.getId()) {
                        case 1 -> "<html><b>BONO MENSUAL ESTÁNDAR:</b><br>" +
                                  "La opción perfecta para el viajero frecuente. Disfruta de <b>viajes ilimitados</b> " +
                                  "durante 30 días naturales consecutivos desde la primera validación. " +
                                  "Olvídate de recargar y viaja sin preocupaciones.</html>";
                                  
                        case 2 -> "<html><b>BONO 10 VIAJES (MULTIPERSONAL):</b><br>" +
                                  "Ideal si no viajas todos los días. Este título carga 10 viajes sin caducidad cercana. " +
                                  "<b>¡Es compartible!</b> Puedes validarlo varias veces consecutivas para viajar con amigos o familiares. " +
                                  "Permite transbordos gratuitos durante 60 minutos.</html>";
                                  
                        case 3 -> "<html><b>ABONO JOVEN (<26 AÑOS):</b><br>" +
                                  "Tarifa plana súper reducida exclusiva para menores de 26 años. " +
                                  "Acceso ilimitado a todas las zonas tarifarias. " +
                                  "<i>Requisito indispensable:</i> Presentar DNI o tarjeta de residencia al validar.</html>";
                                  
                        case 4 -> "<html><b>BONO TURISTA (72 HORAS):</b><br>" +
                                  "Diseñado para visitantes. Ofrece movilidad total e ilimitada durante 3 días consecutivos (72h). " +
                                  "Incluye suplemento especial para trayectos al Aeropuerto y descuentos en museos concertados. " +
                                  "Se activa automáticamente con el primer uso.</html>";
                                  
                        case 5 -> "<html><b>ABONO TEMPORADA VERANO:</b><br>" +
                                  "Pase especial válido únicamente durante los meses de junio, julio y agosto. " +
                                  "Permite acceso ilimitado a líneas de costa y zonas de ocio veraniego. " +
                                  "<br><b>ESTADO: NO DISPONIBLE ACTUALMENTE (FUERA DE TEMPORADA).</b></html>";
                                  
                        default -> "Sin información detallada disponible para este bono.";
                    };

                    descripcionModel.setRowCount(0);
                    descripcionModel.addRow(new Object[]{b.getId(), textoExtra});
                }
            }
        });
    }

    private void comprarBonoSeleccionado() {
        int filaSeleccionadaEnVista = tablaBonos.getSelectedRow();
        if (filaSeleccionadaEnVista == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un bono...", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int filaSeleccionadaEnModelo = tablaBonos.convertRowIndexToModel(filaSeleccionadaEnVista);
            Bono bonoAComprar = bonosModel.getBonoAt(filaSeleccionadaEnModelo);

            if (bonoAComprar.getId() == 5) {
                JOptionPane.showMessageDialog(this, "El 'Abono Verano' no está disponible para la compra.", "Bono no disponible", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombreBono = bonoAComprar.getNombre();
            Double precio = bonoAComprar.getPrecio();

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas comprar el bono:\n\n" +
                            "Bono: " + nombreBono + "\n" +
                            "Precio: " + String.format("%.2f", precio) + "€\n",
                    "Confirmar Compra",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "¡Bono '" + nombreBono + "' comprado!", "Compra Realizada", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class BonoRowRenderer extends DefaultTableCellRenderer {
        private final Color COLOR_VERDE_SUAVE = new Color(200, 255, 200);
        private final Color COLOR_ROJO_SUAVE = new Color(255, 200, 200);

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);

            if (!isSelected) {
                c.setBackground(Color.WHITE);
            } else {
                int modelRow = table.convertRowIndexToModel(row);
                Bono bono = bonosModel.getBonoAt(modelRow);
                if (bono.getId() == 5) {
                    c.setBackground(COLOR_ROJO_SUAVE);
                } else {
                    c.setBackground(COLOR_VERDE_SUAVE);
                }
                c.setForeground(Color.BLACK);
            }
            return c;
        }
    }

    private class DescripcionRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (column == 0) {
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
                setVerticalAlignment(SwingConstants.TOP); 
            }

            // Recuperar el ID de la primera columna
            Object idValue = table.getModel().getValueAt(row, 0);
            
            // Lógica: Si es el bono 5 (Verano), pintar AMARILLO
            if (idValue != null && idValue.toString().equals("5")) {
                c.setBackground(Color.YELLOW);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }
}