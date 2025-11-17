package Principal;
//uso de IAG para mejorar la estetica
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List; // Importante

public class NuevaReservaPanel extends JPanel {
    private MainFrame mainFrame;
    private final GestorRutas gestor; 
    
    // Componentes de selección
    private final JComboBox<String> cbOrigen;
    private final JComboBox<String> cbDestino;
    private final DefaultComboBoxModel<String> modeloDestino;

    // Componentes del "Ticket Visual"
    private JPanel panelTicket;
    private JLabel lblTicketRuta;
    private JLabel lblTicketDuracion;
    private JLabel lblTicketPrecio;
    private JLabel lblTicketOrigenDestino;
    
    // Datos actuales
    private double precioActual = 0.0;

    // CONSTRUCTOR QUE ACEPTA MAINFRAME (Esto soluciona tu error)
    public NuevaReservaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        // Instanciamos el GestorRutas. 
        // Al usar el constructor vacío, no dibuja interfaz gráfica aquí, solo carga datos.
        gestor = new GestorRutas(); 
        // Aseguramos carga de datos por si acaso el constructor vacío no lo hizo
        gestor.cargarDesdeFichero("rutas.txt", null); 

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // --- 1. Header ---
        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setLayout(new GridBagLayout());
        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.LIGHT_GRAY));
        
        JLabel title = new JLabel("Nueva Reserva de Viaje");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 40));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // --- 2. Panel Central (Formulario) ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fuentes
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font comboFont = new Font("Segoe UI", Font.PLAIN, 16);

        // -- Selectores --
        JLabel lblOrigen = new JLabel("Origen:");
        lblOrigen.setFont(labelFont);
        
        cbOrigen = new JComboBox<>();
        cbOrigen.setFont(comboFont);
        cbOrigen.setBackground(Color.WHITE);
        cbOrigen.setPreferredSize(new Dimension(300, 40));

        JLabel lblDestino = new JLabel("Destino:");
        lblDestino.setFont(labelFont);
        
        modeloDestino = new DefaultComboBoxModel<>();
        cbDestino = new JComboBox<>(modeloDestino);
        cbDestino.setFont(comboFont);
        cbDestino.setBackground(Color.WHITE);
        cbDestino.setPreferredSize(new Dimension(300, 40));

        // -- Panel Ticket --
        crearPanelTicket();

        // -- Botón Confirmar --
        JButton btnReservar = new JButton("CONFIRMAR Y PAGAR");
        btnReservar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnReservar.setBackground(new Color(0, 100, 200));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFocusPainted(false);
        btnReservar.setBorderPainted(false);
        btnReservar.setOpaque(true);
        btnReservar.setPreferredSize(new Dimension(250, 50));
        btnReservar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(lblOrigen, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(cbOrigen, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(lblDestino, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(cbDestino, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(30, 20, 30, 20);
        centerPanel.add(panelTicket, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(btnReservar, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // --- Lógica de Carga ---
        cargarOrigenesValidos();

        // Listeners
        cbOrigen.addActionListener(e -> actualizarDestinos());
        cbDestino.addActionListener(e -> actualizarVistaTicket());
        btnReservar.addActionListener(e -> confirmarYGuardar());

        // Inicialización
        if (cbOrigen.getItemCount() > 0) cbOrigen.setSelectedIndex(0);
        actualizarDestinos();
    }

    private void cargarOrigenesValidos() {
        List<String> todas = gestor.getTodasParadas();
        for (String parada : todas) {
            // Solo añadimos al combo si hay rutas saliendo de aquí
            if (!gestor.getDestinosDirectosDesde(parada).isEmpty()) {
                cbOrigen.addItem(parada);
            }
        }
    }

    private void crearPanelTicket() {
        panelTicket = new JPanel(new BorderLayout());
        panelTicket.setBackground(Color.WHITE);
        panelTicket.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        panelTicket.setPreferredSize(new Dimension(400, 140));
        
        // Sombra simulada (borde inferior grueso)
        panelTicket.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(220, 220, 220)),
            panelTicket.getBorder()
        ));

        JPanel topTicket = new JPanel(new BorderLayout());
        topTicket.setOpaque(false);
        JLabel lblBrand = new JLabel("BILLETE SENCILLO");
        lblBrand.setFont(new Font("Arial", Font.BOLD, 12));
        lblBrand.setForeground(Color.GRAY);
        topTicket.add(lblBrand, BorderLayout.WEST);
        
        lblTicketRuta = new JLabel("RUTA: --");
        lblTicketRuta.setFont(new Font("Monospaced", Font.BOLD, 12));
        lblTicketRuta.setForeground(Color.GRAY);
        topTicket.add(lblTicketRuta, BorderLayout.EAST);

        lblTicketOrigenDestino = new JLabel("Seleccione Ruta", SwingConstants.CENTER);
        lblTicketOrigenDestino.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTicketOrigenDestino.setForeground(new Color(0, 70, 140));

        JPanel bottomTicket = new JPanel(new GridLayout(1, 2));
        bottomTicket.setOpaque(false);
        
        lblTicketDuracion = new JLabel("⏱ -- min");
        lblTicketDuracion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTicketDuracion.setForeground(Color.DARK_GRAY);
        
        lblTicketPrecio = new JLabel("💶 -- €", SwingConstants.RIGHT);
        lblTicketPrecio.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTicketPrecio.setForeground(new Color(0, 100, 60));

        bottomTicket.add(lblTicketDuracion);
        bottomTicket.add(lblTicketPrecio);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.LIGHT_GRAY);

        panelTicket.add(topTicket, BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.add(lblTicketOrigenDestino, BorderLayout.CENTER);
        centro.add(separator, BorderLayout.SOUTH);
        panelTicket.add(centro, BorderLayout.CENTER);
        panelTicket.add(bottomTicket, BorderLayout.SOUTH);
        
        panelTicket.setVisible(false);
    }

    private void actualizarDestinos() {
        modeloDestino.removeAllElements();
        String origen = (String) cbOrigen.getSelectedItem();
        if (origen == null) return;

        var opciones = gestor.getDestinosDirectosDesde(origen);

        if (opciones.isEmpty()) {
            modeloDestino.addElement("-- No hay rutas --");
            panelTicket.setVisible(false);
            return;
        }

        for (var op : opciones) {
            modeloDestino.addElement(op.toString()); 
        }

        if (cbDestino.getItemCount() > 0) cbDestino.setSelectedIndex(0);
        actualizarVistaTicket();
    }

    private void actualizarVistaTicket() {
        String selected = (String) cbDestino.getSelectedItem();
        
        if (selected == null || selected.startsWith("--")) {
            panelTicket.setVisible(false);
            return;
        }

        try {
            String origen = (String) cbOrigen.getSelectedItem();
            // Parseamos el toString de OpcionDestino: "Destino (ID) — min — precio"
            String[] partes = selected.split("—"); 

            String destinoLimpio = partes[0].split("\\(")[0].trim();
            String idRuta = partes[0].substring(partes[0].indexOf("(")+1, partes[0].indexOf(")"));
            String tiempo = partes[1].trim();
            String precioStr = partes[2].trim();
            
            lblTicketRuta.setText("ID: " + idRuta);
            lblTicketOrigenDestino.setText(origen + " ➜ " + destinoLimpio);
            lblTicketDuracion.setText("⏱ " + tiempo);
            lblTicketPrecio.setText("💶 " + precioStr);
            
            this.precioActual = Double.parseDouble(precioStr.replace("€", "").replace(",", "."));

            panelTicket.setVisible(true);
            panelTicket.revalidate();
            panelTicket.repaint();
            
        } catch (Exception e) {
            panelTicket.setVisible(false);
        }
    }

    private void confirmarYGuardar() {
        if (!panelTicket.isVisible()) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un trayecto válido.", "Información incompleta", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int respuesta = JOptionPane.showConfirmDialog(
            this, 
            "Estás a punto de comprar el siguiente billete:\n\n" +
            "Viaje: " + lblTicketOrigenDestino.getText() + "\n" +
            "Precio: " + lblTicketPrecio.getText() + "\n\n" +
            "¿Deseas confirmar el pago?",
            "Confirmar Compra",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("reservas.txt", true)))) {
                pw.println(lblTicketOrigenDestino.getText() + " | " + lblTicketPrecio.getText());
                JOptionPane.showMessageDialog(this, "✅ ¡Reserva realizada con éxito!\nBuen viaje.", "Confirmado", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar la reserva en el sistema.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}