package Principal;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class NuevaReservaPanel extends JPanel {

    private final GestorRutas gestor;
    private final JComboBox<String> cbOrigen;
    private final JComboBox<String> cbDestino;
    private final DefaultComboBoxModel<String> modeloDestino;
    private final JLabel lblInfo;

    public NuevaReservaPanel() {
        gestor = new GestorRutas();
        gestor.cargarDesdeFichero("rutas.txt");

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
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
        add(new JLabel("Origen:", SwingConstants.RIGHT), gbc);

        gbc.gridx = 1;
        add(cbOrigen, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Destino:"), gbc);

        gbc.gridx = 1;
        add(cbDestino, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(lblInfo, gbc);

        gbc.gridy = 3;
        add(btnReservar, gbc);

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
