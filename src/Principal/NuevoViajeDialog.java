package Principal;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Diálogo para crear/seleccionar un nuevo viaje.
 */
public class NuevoViajeDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> cbOrigen;
    private JComboBox<String> cbDestino;
    private JTextField txtDeparture;
    private JTextField txtReturn;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public NuevoViajeDialog(Frame parent) {
        super(parent, "Reserva nuevo viaje", true);
        initComponents();
        setSize(500, 300);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Origen:"), c);
        c.gridx = 1; c.gridy = 0;
        cbOrigen = new JComboBox<>(new String[]{"Madrid", "Barcelona", "Sevilla", "Valencia"});
        cbOrigen.setEditable(true);
        panel.add(cbOrigen, c);

        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Destino:"), c);
        c.gridx = 1; c.gridy = 1;
        cbDestino = new JComboBox<>(new String[]{"Barcelona", "Madrid", "Granada", "Zaragoza"});
        cbDestino.setEditable(true);
        panel.add(cbDestino, c);

        c.gridx = 0; c.gridy = 2;
        panel.add(new JLabel("Fecha de ida:"), c);
        c.gridx = 1; c.gridy = 2;
        JPanel pFechaIda = new JPanel(new BorderLayout(4,4));
        txtDeparture = new JTextField();
        txtDeparture.setEditable(false);
        JButton btnPickIda = new JButton("Seleccionar...");
        pFechaIda.add(txtDeparture, BorderLayout.CENTER);
        pFechaIda.add(btnPickIda, BorderLayout.EAST);
        panel.add(pFechaIda, c);

        c.gridx = 0; c.gridy = 3;
        panel.add(new JLabel("Fecha de vuelta:"), c);
        c.gridx = 1; c.gridy = 3;
        JPanel pFechaVuelta = new JPanel(new BorderLayout(4,4));
        txtReturn = new JTextField();
        txtReturn.setEditable(false);
        JButton btnPickVuelta = new JButton("Seleccionar...");
        pFechaVuelta.add(txtReturn, BorderLayout.CENTER);
        pFechaVuelta.add(btnPickVuelta, BorderLayout.EAST);
        panel.add(pFechaVuelta, c);

        add(panel, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("Aceptar");
        JButton btnCancel = new JButton("Cancelar");
        botones.add(btnCancel);
        botones.add(btnOk);
        add(botones, BorderLayout.SOUTH);

        btnPickIda.addActionListener(e -> {
            CalendarDialog cal = new CalendarDialog((Frame) getParent());
            LocalDate d = cal.selectDate();
            if (d != null) {
                departureDate = d;
                txtDeparture.setText(fmt.format(d));
            }
        });

        btnPickVuelta.addActionListener(e -> {
            CalendarDialog cal = new CalendarDialog((Frame) getParent());
            LocalDate d = cal.selectDate();
            if (d != null) {
                returnDate = d;
                txtReturn.setText(fmt.format(d));
            }
        });

        btnOk.addActionListener(e -> doOk());
        btnCancel.addActionListener(e -> dispose());
    }

    private void doOk() {
        String origen = cbOrigen.getEditor().getItem().toString().trim();
        String destino = cbDestino.getEditor().getItem().toString().trim();
        if (origen.isEmpty() || destino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete origen y destino.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (departureDate == null) {
            JOptionPane.showMessageDialog(this, "Seleccione la fecha de ida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (returnDate != null && returnDate.isBefore(departureDate)) {
            JOptionPane.showMessageDialog(this, "La fecha de vuelta no puede ser anterior a la de ida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Por ahora solo mostramos un resumen
        String mensaje = String.format("Reserva:\nOrigen: %s\nDestino: %s\nIda: %s\nVuelta: %s",
                origen,
                destino,
                departureDate != null ? fmt.format(departureDate) : "-",
                returnDate != null ? fmt.format(returnDate) : "-");

        JOptionPane.showMessageDialog(this, mensaje, "Resumen reserva", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
