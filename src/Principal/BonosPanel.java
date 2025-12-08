package Principal;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.LIGHT_GRAY));
        JLabel lblTitulo = new JLabel("Bonos y Abonos Disponibles");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.add(lblTitulo);
        add(header, BorderLayout.NORTH);

        List<Bono> listaBonos = cargarBonosDesdeBD();

        bonosModel = new BonosTableModel(listaBonos);
        tablaBonos = new JTable(bonosModel);
        tablaBonos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaBonos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaBonos.setRowHeight(28);
        tablaBonos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // --- IMPLEMENTACIÓN: BLOQUEAR MOVIMIENTO DE COLUMNAS ---
        tablaBonos.getTableHeader().setReorderingAllowed(false);

        tablaBonos.setDefaultRenderer(Object.class, new BonoRowRenderer());
        tablaBonos.setDefaultRenderer(Integer.class, new BonoRowRenderer());
        tablaBonos.setDefaultRenderer(Double.class, new BonoRowRenderer());
        tablaBonos.setDefaultRenderer(java.util.Date.class, new BonoRowRenderer());

        add(new JScrollPane(tablaBonos), BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.Y_AXIS));
        
        descripcionModel = new DefaultTableModel(new String[]{"ID", "Descripción"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDescripcion = new JTable(descripcionModel);
        tablaDescripcion.setRowHeight(90);
        tablaDescripcion.getColumnModel().getColumn(0).setMaxWidth(60);
        tablaDescripcion.setDefaultRenderer(Object.class, new DescripcionRowRenderer());
        
        scrollDescripcion = new JScrollPane(tablaDescripcion);
        scrollDescripcion.setPreferredSize(new Dimension(0, 150));
        panelSur.add(scrollDescripcion);

        btnComprar = new JButton("COMPRAR SELECCIONADO");
        btnComprar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnComprar.setBackground(new Color(0, 70, 140));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.addActionListener(e -> comprarBonoSeleccionado());
        
        JPanel pBtn = new JPanel(); 
        pBtn.add(btnComprar);
        panelSur.add(pBtn);
        
        add(panelSur, BorderLayout.SOUTH);

        tablaBonos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablaBonos.getSelectedRow();
                if (row != -1) {
                    Bono b = bonosModel.getBonoAt(tablaBonos.convertRowIndexToModel(row));
                    String texto = "<html><b>" + b.getNombre() + ":</b><br>" + 
                                   b.getDescripcion() + "<br>" +
                                   "<i>Validez tras validación: " + b.getDuracionDias() + " días.</i></html>";
                    
                    descripcionModel.setRowCount(0);
                    descripcionModel.addRow(new Object[]{b.getId(), texto});
                }
            }
        });
    }

    private List<Bono> cargarBonosDesdeBD() {
        List<Bono> lista = new ArrayList<>();
        GestorBD gestor = new GestorBD();
        Connection conn = gestor.getConnection();
        SimpleDateFormat sdfBD = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "SELECT * FROM bono";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String desc = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                int viajes = rs.getInt("viajes_incluidos");
                String fechaStr = rs.getString("fecha_expiracion");
                int duracion = rs.getInt("duracion_dias");
                Date fecha;
                try { fecha = sdfBD.parse(fechaStr); } catch (Exception e) { fecha = new Date(); }
                lista.add(new Bono(id, nombre, desc, precio, viajes, fecha, duracion));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    private void comprarBonoSeleccionado() {
        int row = tablaBonos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un bono.");
            return;
        }

        Bono b = bonosModel.getBonoAt(tablaBonos.convertRowIndexToModel(row));

        // --- IMPLEMENTACIÓN: BLOQUEO DE ABONO VERANO ---
        if (b.getNombre().toUpperCase().contains("VERANO")) {
            JOptionPane.showMessageDialog(this, 
                "⛔ El " + b.getNombre() + " no está disponible actualmente.\n" +
                "Este abono está fuera de temporada (Solo disponible Jun-Sept).", 
                "Producto No Disponible", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = mainFrame.getUsuarioActual();
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Debes iniciar sesión para comprar.");
            mainFrame.mostrarPantallaLoginRegistro();
            return;
        }

        int opt = JOptionPane.showConfirmDialog(this, 
            "Comprar " + b.getNombre() + "?\n" +
            "Precio: " + b.getPrecio() + "€\n" +
            "Tu saldo: " + String.format("%.2f €", u.getSaldo()),
            "Confirmar Compra", JOptionPane.YES_NO_OPTION);
            
        if(opt == JOptionPane.YES_OPTION) {
            UsuarioDAO dao = new UsuarioDAO();
            boolean exito = dao.comprarBono(u, b);
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Compra realizada! Nuevo saldo: " + String.format("%.2f €", u.getSaldo()));
                mainFrame.actualizarSaldoHeader();
            } else {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente. Recarga tu monedero.");
            }
        }
    }

    // (Renderers igual que antes...)
    private class BonoRowRenderer extends DefaultTableCellRenderer {
        private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            if(v instanceof Date) v = sdf.format((Date)v);
            if(v instanceof Double) v = String.format("%.2f €", (Double)v);
            Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
            setHorizontalAlignment(CENTER);
            if(!s) {
                Bono b = bonosModel.getBonoAt(t.convertRowIndexToModel(r));
                comp.setBackground(b.getNombre().contains("Verano") ? new Color(255,200,200) : new Color(200,255,200));
                comp.setForeground(Color.BLACK);
            }
            return comp;
        }
    }
    private class DescripcionRowRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
            if (c == 0) { setHorizontalAlignment(CENTER); setVerticalAlignment(CENTER); }
            else { setHorizontalAlignment(LEFT); setVerticalAlignment(TOP); }
            Object id = t.getModel().getValueAt(r, 0);
            if(id != null && id.toString().equals("5")) {
                comp.setBackground(Color.YELLOW);
                comp.setForeground(Color.BLACK);
            } else {
                comp.setBackground(Color.WHITE);
                comp.setForeground(Color.BLACK);
            }
            return comp;
        }
    }
}