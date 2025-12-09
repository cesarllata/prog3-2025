package app.ui;

import app.dao.UsuarioDAO;
import app.models.Usuario;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class NuevaReservaPanel extends JPanel {
    private MainFrame mainFrame;
    private final GestorRutas gestor; 
    
    private final JComboBox<String> cbOrigen;
    private final JComboBox<String> cbDestino;
    private final DefaultComboBoxModel<String> modeloDestino;

    private JPanel panelTicket;
    private JLabel lblTicketRuta;
    private JLabel lblTicketDuracion;
    private JLabel lblTicketPrecio;
    private JLabel lblTicketOrigenDestino;
    
    private double precioActual = 0.0;

    public NuevaReservaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        gestor = new GestorRutas(); 
        gestor.cargarDesdeBD(null);  

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

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

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font comboFont = new Font("Segoe UI", Font.PLAIN, 16);

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

        crearPanelTicket();

        JButton btnReservar = new JButton("CONFIRMAR Y PAGAR");
        btnReservar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnReservar.setBackground(new Color(0, 100, 200));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFocusPainted(false);
        btnReservar.setBorderPainted(false);
        btnReservar.setOpaque(true);
        btnReservar.setPreferredSize(new Dimension(250, 50));
        btnReservar.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        cargarOrigenesValidos(); 

        cbOrigen.addActionListener(e -> actualizarDestinos());
        cbDestino.addActionListener(e -> actualizarVistaTicket());
        btnReservar.addActionListener(e -> confirmarYGuardar());

        if (cbOrigen.getItemCount() > 0) cbOrigen.setSelectedIndex(0);
        actualizarDestinos();
    }

    private void cargarOrigenesValidos() {
        List<String> todas = gestor.getTodasParadas();
        for (String parada : todas) {
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

        Usuario usuario = mainFrame.getUsuarioActual();
        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Debes iniciar sesión para realizar una reserva.");
            mainFrame.mostrarPantallaLoginRegistro();
            return;
        }
        
        int respuesta = JOptionPane.showConfirmDialog(
            this, 
            "Estás a punto de comprar el siguiente billete:\n\n" +
            "Viaje: " + lblTicketOrigenDestino.getText() + "\n" +
            "Precio: " + lblTicketPrecio.getText() + "\n\n" +
            "Tu saldo: " + String.format("%.2f €", usuario.getSaldo()) + "\n" +
            "¿Deseas confirmar el pago?",
            "Confirmar Compra",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            UsuarioDAO dao = new UsuarioDAO();
                String precioStr = lblTicketPrecio.getText().replace("€", "").replace("💶", "").replace(",", ".").trim();
            double precio = Double.parseDouble(precioStr);

            boolean exito = dao.guardarReserva(usuario, lblTicketOrigenDestino.getText(), precio);

            if (exito) {
                JOptionPane.showMessageDialog(this, "✅ ¡Reserva realizada con éxito!\nBuen viaje.\nNuevo saldo: " + String.format("%.2f €", usuario.getSaldo()), "Confirmado", JOptionPane.INFORMATION_MESSAGE);
                // REFRESCAR HEADER
                mainFrame.actualizarSaldoHeader();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Saldo insuficiente para realizar esta operación.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
