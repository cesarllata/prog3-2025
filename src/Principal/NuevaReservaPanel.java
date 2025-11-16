package Principal;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class NuevaReservaPanel extends JPanel {
    private MainFrame mainFrame;
    private final GestorRutas gestor;
    private final JComboBox<String> cbOrigen;
    private final JComboBox<String> cbDestino;
    private final DefaultComboBoxModel<String> modeloDestino;
    private final JLabel lblInfo;

    // Añadido: constructor sin args para evitar "constructor undefined"
    public NuevaReservaPanel() {
        this(null);
    }

    public NuevaReservaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        gestor = new GestorRutas();
        gestor.cargarDesdeFichero("rutas.txt");

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);

        // ---------- COMPONENTES ----------
        cbOrigen = new JComboBox<>();
        modeloDestino = new DefaultComboBoxModel<>();
        cbDestino = new JComboBox<>(modeloDestino);

        lblInfo = new JLabel("Selecciona origen y destino", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnReservar = new JButton("Reservar");
        btnReservar.setFont(new Font("Arial", Font.BOLD, 14));

        // ---------- POBLAR ORIGEN ----------
        for (String parada : gestor.getTodasParadas())
            cbOrigen.addItem(parada);

        cbOrigen.addActionListener(e -> actualizarDestinos());
        cbDestino.addActionListener(e -> mostrarDetalles());
        btnReservar.addActionListener(e -> guardarReserva());

        // ---------- LAYOUT ----------
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Origen:", SwingConstants.RIGHT), gbc);

        gbc.gridx = 1;
        centerPanel.add(cbOrigen, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Destino:"), gbc);

        gbc.gridx = 1;
        centerPanel.add(cbDestino, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        centerPanel.add(lblInfo, gbc);

        gbc.gridy = 3;
        centerPanel.add(btnReservar, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // ---- Reemplazado: barra superior con botón atrás visible y funcional ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JButton backBtn = new JButton("← Volver");
        backBtn.setToolTipText("Volver al menú principal");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 19));
        backBtn.setFocusPainted(false);

        // Mostrar como botón (fondo y borde)
        backBtn.setOpaque(true);
        backBtn.setContentAreaFilled(true);
        backBtn.setBackground(new Color(230, 230, 230));
        backBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        backBtn.addActionListener(e -> {
            // intentar usar el mainFrame pasado en el constructor
            if (this.mainFrame != null) {
                this.mainFrame.mostrarPanel("MENU");
                return;
            }
            // si mainFrame es null, buscar la ventana contenedora y castear a MainFrame
            java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (w instanceof MainFrame) {
                ((MainFrame) w).mostrarPanel("MENU");
            }
        });

        topBar.add(backBtn, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);
        // -------------------------------------------------

        // Primer despliegue
        if (cbOrigen.getItemCount() > 0)
            cbOrigen.setSelectedIndex(0);

        actualizarDestinos();
    }

    // ==============================
    // Cargar destinos solo de la misma ruta
    // ==============================
    private void actualizarDestinos() {
        modeloDestino.removeAllElements();
        String origen = (String) cbOrigen.getSelectedItem();
        if (origen == null) return;

        var opciones = gestor.getDestinosDirectosDesde(origen);

        if (opciones.isEmpty()) {
            modeloDestino.addElement("-- No hay destinos desde aquí --");
            lblInfo.setText("No hay destinos desde " + origen);
            return;
        }

        for (var op : opciones)
            modeloDestino.addElement(op.toString());

        cbDestino.setSelectedIndex(0);
        mostrarDetalles();
    }

    private void mostrarDetalles() {
        String txt = (String) cbDestino.getSelectedItem();
        if (txt != null && !txt.startsWith("--"))
            lblInfo.setText(txt);
    }

    // ==============================
    // Guardar la reserva en "reservas.txt"
    // ==============================
    private void guardarReserva() {
        String origen = (String) cbOrigen.getSelectedItem();
        String txtDestino = (String) cbDestino.getSelectedItem();

        if (txtDestino == null || txtDestino.startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Selecciona un destino válido.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("reservas.txt", true)))) {
            pw.println(origen + " -> " + txtDestino);
            JOptionPane.showMessageDialog(this,
                    "Reserva realizada:\n\n" + origen + " → " + txtDestino,
                    "Reserva confirmada", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar la reserva",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
