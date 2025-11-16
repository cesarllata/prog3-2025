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
	
	        // Asegurar layout adecuado para colocar la barra superior
	        setLayout(new BorderLayout());
	        setBackground(Color.WHITE);
	
	        // ---- Añadido / reemplazado: barra superior con botón atrás visible y funcional ----
	        JPanel topBar = new JPanel(new BorderLayout());
	        topBar.setOpaque(false);
	
	        JButton backBtn = createBackButton();
	        topBar.add(backBtn, BorderLayout.WEST);
	
	        // Añadimos la barra en NORTH (si ya había algo en NORTH, se reemplaza)
	        add(topBar, BorderLayout.NORTH);
	        // ----------------------------------------------------------------------------------
	
	        // --- 1. Título ---
	        JLabel lblTitulo = new JLabel("Bonos y Abonos Disponibles");
	        lblTitulo.setForeground(Color.BLUE);
	        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
	        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
	        add(lblTitulo, BorderLayout.NORTH);
	
	        // --- 2. Crear los datos ---
	        List<Bono> listaBonos = new ArrayList<>();
	        listaBonos.add(new Bono(1, "Bono Mensual", "30 días", 45.00, "Viajes ilimitados durante 30 días.","hasta 31/12/2025"));
	        listaBonos.add(new Bono(2, "Bono 10 Viajes", "10 viajes", 12.50, "Permite 10 viajes. No caduca.","hasta 31/12/2025"));
	        listaBonos.add(new Bono(3, "Abono Joven", "Mensual (<26 años)", 25.00, "Tarifa plana mensual para jóvenes.","hasta 31/12/2025"));
	        listaBonos.add(new Bono(4, "Bono Turista", "3 días", 18.00, "Viajes ilimitados por 72 horas.","hasta 31/12/2025"));
	        listaBonos.add(new Bono(5, "Abono Verano", "Junio-Agosto", 60.00, "Bono especial de verano. No disponible.","hasta 31/08/2025"));
	
	        // --- 3. Modelo principal ---
	        bonosModel = new BonosTableModel(listaBonos);
	        tablaBonos = new JTable(bonosModel);
	
	        tablaBonos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        tablaBonos.setFont(new Font("Arial", Font.PLAIN, 14));
	        tablaBonos.setRowHeight(25);
	        tablaBonos.setAutoCreateRowSorter(true);
	
	        tablaBonos.setDefaultRenderer(Object.class, new BonoRowRenderer());
	        tablaBonos.setDefaultRenderer(String.class, new BonoRowRenderer());
	        tablaBonos.setDefaultRenderer(Integer.class, new BonoRowRenderer());
	        tablaBonos.setDefaultRenderer(Double.class, new BonoRowRenderer());
	
	        JScrollPane scrollPane = new JScrollPane(tablaBonos);
	        add(scrollPane, BorderLayout.CENTER);
	
	        // --- Panel inferior ---
	        JPanel panelSur = new JPanel();
	        panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.Y_AXIS));
	
	        // --- Tabla de descripción ---
	        String[] columnasDesc = {"ID del Bono", "Descripción"};
	        descripcionModel = new DefaultTableModel(columnasDesc, 0);
	        tablaDescripcion = new JTable(descripcionModel);
	        tablaDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
	        tablaDescripcion.setRowHeight(25);
	
	        DefaultTableCellRenderer centerDesc = new DefaultTableCellRenderer();
	        centerDesc.setHorizontalAlignment(SwingConstants.CENTER);
	        tablaDescripcion.getColumnModel().getColumn(0).setCellRenderer(centerDesc);
	        tablaDescripcion.getColumnModel().getColumn(1).setCellRenderer(centerDesc);
	
	        scrollDescripcion = new JScrollPane(tablaDescripcion);
	        scrollDescripcion.setPreferredSize(new Dimension(0, 150));
	        panelSur.add(scrollDescripcion);
	
	        // --- Botón comprar ---
	        btnComprar = new JButton("Comprar Bono Seleccionado");
	        btnComprar.setFont(new Font("Arial", Font.BOLD, 16));
	        btnComprar.addActionListener(e -> comprarBonoSeleccionado());
	
	        JPanel panelBotones = new JPanel();
	        panelBotones.add(btnComprar);
	
	        panelSur.add(panelBotones);
	        add(panelSur, BorderLayout.SOUTH);
	
	        // --- Mostrar descripción por bono seleccionado ---
	        tablaBonos.getSelectionModel().addListSelectionListener(e -> {
	            if (!e.getValueIsAdjusting()) {
	                int filaVista = tablaBonos.getSelectedRow();
	
	                if (filaVista != -1) {
	                    int filaModelo = tablaBonos.convertRowIndexToModel(filaVista);
	                    Bono b = bonosModel.getBonoAt(filaModelo);
	
	                    String textoExtra = switch (b.getId()) {
	                        case 1 -> "Ideal para viajeros diarios.";
	                        case 2 -> "Perfecto si viajas poco y quieres ahorrar.";
	                        case 3 -> "Descuento especial para jóvenes.";
	                        case 4 -> "Ideal si vienes de turismo.";
	                        case 5 -> "No disponible actualmente.";
	                        default -> "Sin información extra.";
	                    };
	
	                    descripcionModel.setRowCount(0);
	                    descripcionModel.addRow(new Object[]{b.getId(), textoExtra});
	                }
	            }
	        });
	    }
	
	    // Helper reutilizable para estilo y comportamiento del botón
	    private JButton createBackButton() {
	        JButton backBtn = new JButton("← Volver");
	        backBtn.setToolTipText("Volver al menú principal");
	        backBtn.setFont(new Font("Arial", Font.PLAIN, 14));
	        backBtn.setFocusPainted(false);
	
	        // Mostrar claramente como botón
	        backBtn.setOpaque(true);
	        backBtn.setContentAreaFilled(true);
	        backBtn.setBackground(new Color(230, 230, 230));
	        backBtn.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createLineBorder(Color.GRAY),
	                BorderFactory.createEmptyBorder(6, 10, 6, 10)
	        ));
	
	        backBtn.addActionListener(e -> {
	            if (this.mainFrame != null) {
	                this.mainFrame.mostrarPanel("MENU");
	                return;
	            }
	            Window w = SwingUtilities.getWindowAncestor(this);
	            if (w instanceof MainFrame) {
	                ((MainFrame) w).mostrarPanel("MainFrame");
	            }
	        });
	        return backBtn;
	    }
	
	    private void comprarBonoSeleccionado() {
	        int filaSeleccionadaEnVista = tablaBonos.getSelectedRow();
	
	        if (filaSeleccionadaEnVista == -1) {
	            JOptionPane.showMessageDialog(this, "Por favor, selecciona un bono...", "Error", JOptionPane.ERROR_MESSAGE);
	        } else {
	            int filaSeleccionadaEnModelo = tablaBonos.convertRowIndexToModel(filaSeleccionadaEnVista);
	            Bono bonoAComprar = bonosModel.getBonoAt(filaSeleccionadaEnModelo);
	
	            if (bonoAComprar.getNombre().contains("Verano")) {
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
	                return c;
	            }
	
	            int modelRow = table.convertRowIndexToModel(row);
	            Bono bono = bonosModel.getBonoAt(modelRow);
	
	            if (bono.getId() == 5) {
	                c.setBackground(COLOR_ROJO_SUAVE);
	            } else {
	                c.setBackground(COLOR_VERDE_SUAVE);
	            }
	
	            return c;
	        }
	    }
	}
