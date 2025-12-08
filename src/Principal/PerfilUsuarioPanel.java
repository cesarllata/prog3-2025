package Principal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class PerfilUsuarioPanel extends JPanel {
    private MainFrame mainFrame;

    // Campos Datos
    private JTextField txtNombre, txtEmail, txtTelefono, txtTarjeta;
    private JLabel lblSaldoGrande;

    // Pestañas
    private JTabbedPane tabbedPane; // AHORA ES VARIABLE DE CLASE

    // Modelos Tablas Historial
    private DefaultTableModel modeloBonos;
    private DefaultTableModel modeloReservas;

    public PerfilUsuarioPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        JLabel lblTitulo = new JLabel("Área Personal");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.add(lblTitulo);
        add(header, BorderLayout.NORTH);

        // --- SISTEMA DE PESTAÑAS ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Pestaña 0: Mis Datos
        tabbedPane.addTab("Perfil y Saldo", crearPanelDatos());

        // Pestaña 1: Historial
        tabbedPane.addTab("Historial de Compras", crearPanelHistorial());

        add(tabbedPane, BorderLayout.CENTER);

        // Listener para recargar datos al mostrar
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarDatosPerfil();
                cargarDatosHistorial();
            }
        });
    }

    // MÉTODO NUEVO PARA CAMBIAR DE PESTAÑA DESDE EL MENÚ
    public void seleccionarPestana(int indice) {
        if (indice >= 0 && indice < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(indice);
        }
    }

    private JPanel crearPanelDatos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = createStyledField();
        txtEmail = createStyledField();
        txtTelefono = createStyledField();
        txtTarjeta = createStyledField();

        addLabelAndField(panel, gbc, 0, "Nombre Completo:", txtNombre);
        addLabelAndField(panel, gbc, 1, "Correo Electrónico:", txtEmail);
        addLabelAndField(panel, gbc, 2, "Teléfono:", txtTelefono);
        addLabelAndField(panel, gbc, 3, "Nº Tarjeta:", txtTarjeta);

        JButton btnGuardar = new JButton("GUARDAR CAMBIOS");
        btnGuardar.setBackground(new Color(0, 70, 140));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarCambios());
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnGuardar, gbc);

        JSeparator sep = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 20, 30, 20);
        panel.add(sep, gbc);

        JLabel lblSaldoTitulo = new JLabel("Saldo Disponible:");
        lblSaldoTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        lblSaldoGrande = new JLabel("0.00 €");
        lblSaldoGrande.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblSaldoGrande.setForeground(new Color(0, 100, 60));

        JPanel panelBotonesSaldo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonesSaldo.setOpaque(false);

        JButton btnIngresar = new JButton("AÑADIR SALDO (+)");
        btnIngresar.setBackground(new Color(40, 167, 69));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.addActionListener(e -> gestionarSaldo(true));

        JButton btnRetirar = new JButton("RETIRAR FONDOS (-)");
        btnRetirar.setBackground(new Color(220, 53, 69));
        btnRetirar.setForeground(Color.WHITE);
        btnRetirar.addActionListener(e -> gestionarSaldo(false));

        panelBotonesSaldo.add(btnIngresar);
        panelBotonesSaldo.add(btnRetirar);

        gbc.gridwidth = 1; gbc.gridy = 6; gbc.insets = new Insets(5, 20, 5, 20);
        gbc.gridx = 0; panel.add(lblSaldoTitulo, gbc);
        gbc.gridx = 1; panel.add(lblSaldoGrande, gbc);
        gbc.gridx = 1; gbc.gridy = 7; panel.add(panelBotonesSaldo, gbc);

        return panel;
    }

    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tabla Bonos
        modeloBonos = new DefaultTableModel(new String[]{"Bono", "Viajes Restantes", "Fecha Compra", "Caducidad"}, 0);
        JTable tablaBonos = new JTable(modeloBonos);
        tablaBonos.setEnabled(false);
        tablaBonos.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollBonos = new JScrollPane(tablaBonos);
        scrollBonos.setBorder(BorderFactory.createTitledBorder("Mis Bonos Activos e Históricos"));

        // Tabla Reservas
        modeloReservas = new DefaultTableModel(new String[]{"Ruta / Trayecto", "Precio Pagado", "Fecha Reserva"}, 0);
        JTable tablaReservas = new JTable(modeloReservas);
        tablaReservas.setEnabled(false);
        tablaReservas.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollReservas = new JScrollPane(tablaReservas);
        scrollReservas.setBorder(BorderFactory.createTitledBorder("Historial de Billetes Sencillos"));

        panel.add(scrollBonos);
        panel.add(scrollReservas);
        return panel;
    }

    private void addLabelAndField(JPanel p, GridBagConstraints gbc, int y, String label, JTextField field) {
        gbc.gridy = y; gbc.gridx = 0; gbc.weightx = 0;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(l, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        p.add(field, gbc);
    }

    private JTextField createStyledField() {
        JTextField f = new JTextField(20);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(200, 35));
        return f;
    }

    private void cargarDatosPerfil() {
        Usuario u = mainFrame.getUsuarioActual();
        if (u != null) {
            txtNombre.setText(u.getNombre());
            txtEmail.setText(u.getEmail());
            txtTelefono.setText(u.getTelefono());
            txtTarjeta.setText(u.getNumTarjeta());
            lblSaldoGrande.setText(String.format("%.2f €", u.getSaldo()));
        }
    }

    private void cargarDatosHistorial() {
        Usuario u = mainFrame.getUsuarioActual();
        if (u == null) return;

        UsuarioDAO dao = new UsuarioDAO();
        modeloBonos.setRowCount(0);
        List<String[]> bonos = dao.obtenerHistorialBonos(u.getId());
        for (String[] fila : bonos) modeloBonos.addRow(fila);

        modeloReservas.setRowCount(0);
        List<String[]> reservas = dao.obtenerHistorialReservas(u.getId());
        for (String[] fila : reservas) modeloReservas.addRow(fila);
    }

    private void guardarCambios() {
        Usuario u = mainFrame.getUsuarioActual();
        if (u == null) return;
        u.setNombre(txtNombre.getText());
        u.setEmail(txtEmail.getText());
        u.setTelefono(txtTelefono.getText());
        u.setNumTarjeta(txtTarjeta.getText());

        UsuarioDAO dao = new UsuarioDAO();
        if (dao.actualizarDatosUsuario(u)) {
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
            mainFrame.actualizarSaldoHeader();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gestionarSaldo(boolean ingresar) {
        Usuario u = mainFrame.getUsuarioActual();
        if (u == null) return;
        String accion = ingresar ? "ingresar" : "retirar";
        String input = JOptionPane.showInputDialog(this, "Cantidad a " + accion + " (€):");
        
        if (input != null && !input.isEmpty()) {
            try {
                double cantidad = Double.parseDouble(input.replace(",", "."));
                if (cantidad <= 0) { JOptionPane.showMessageDialog(this, "Debe ser positiva."); return; }
                if (!ingresar && cantidad > u.getSaldo()) { JOptionPane.showMessageDialog(this, "Saldo insuficiente."); return; }

                double nuevoSaldo = ingresar ? (u.getSaldo() + cantidad) : (u.getSaldo() - cantidad);
                UsuarioDAO dao = new UsuarioDAO();
                if (dao.actualizarSaldo(u.getId(), nuevoSaldo)) {
                    u.setSaldo(nuevoSaldo);
                    lblSaldoGrande.setText(String.format("%.2f €", u.getSaldo()));
                    mainFrame.actualizarSaldoHeader();
                    JOptionPane.showMessageDialog(this, "Operación realizada.");
                }
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Número inválido."); }
        }
    }
}