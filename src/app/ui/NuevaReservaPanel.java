package app.ui;

import app.dao.UsuarioDAO;
import app.models.Usuario;
import app.models.Ruta;
import app.logic.BuscadorRecursivo;
import app.logic.GestorRutas; 
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class NuevaReservaPanel extends JPanel {
    private MainFrame mainFrame;
    private final GestorRutas gestor; 
    
    private final JComboBox<String> cbOrigen;
    private final JComboBox<String> cbDestino;
    private JTextField txtMaxPrecio;
    private JTextField txtMaxTiempo;
    private JPanel panelResultadosRecursivos;
    private JScrollPane scrollResultados;

    public NuevaReservaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.gestor = new GestorRutas(); 
        gestor.cargarDesdeBD(null);  

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // --- HEADER ---
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        JLabel title = new JLabel("Buscador Inteligente de Viajes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // --- CUERPO PRINCIPAL ---
        JPanel bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setOpaque(false);
        GridBagConstraints bodyGbc = new GridBagConstraints();
        bodyGbc.fill = GridBagConstraints.BOTH;
        bodyGbc.weighty = 1.0;

        // --- PANEL IZQUIERDO: FILTROS ---
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; 
        gbc.gridx = 0;

        leftPanel.add(new JLabel("Origen:"), gbc);
        cbOrigen = new JComboBox<>();
        leftPanel.add(cbOrigen, gbc);

        leftPanel.add(new JLabel("Destino:"), gbc);
        cbDestino = new JComboBox<>();
        leftPanel.add(cbDestino, gbc);

        leftPanel.add(new JSeparator(), gbc);
        
        // Etiqueta informativa
        JLabel infoFiltros = new JLabel("<html><small>Deja vacíos para no filtrar</small></html>");
        infoFiltros.setForeground(Color.GRAY);
        leftPanel.add(infoFiltros, gbc);

        leftPanel.add(new JLabel("Presupuesto Máximo (€):"), gbc);
        txtMaxPrecio = new JTextField(""); // Ahora vacío por defecto
        leftPanel.add(txtMaxPrecio, gbc);

        leftPanel.add(new JLabel("Tiempo Máximo (h):"), gbc);
        txtMaxTiempo = new JTextField(""); // Ahora vacío por defecto
        leftPanel.add(txtMaxTiempo, gbc);

        JButton btnBuscar = new JButton("BUSCAR RUTAS");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(0, 100, 200));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        leftPanel.add(btnBuscar, gbc);

        // --- PANEL CENTRAL: RESULTADOS ---
        JPanel wrapperResultados = new JPanel(new BorderLayout());
        wrapperResultados.setBackground(Color.WHITE);

        panelResultadosRecursivos = new JPanel();
        panelResultadosRecursivos.setLayout(new BoxLayout(panelResultadosRecursivos, BoxLayout.Y_AXIS));
        panelResultadosRecursivos.setBackground(Color.WHITE);
        panelResultadosRecursivos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        wrapperResultados.add(panelResultadosRecursivos, BorderLayout.NORTH);

        scrollResultados = new JScrollPane(wrapperResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Caminos Disponibles"));
        scrollResultados.getVerticalScrollBar().setUnitIncrement(16);

        bodyGbc.gridx = 0;
        bodyGbc.weightx = 0.3; 
        bodyPanel.add(leftPanel, bodyGbc);

        bodyGbc.gridx = 1;
        bodyGbc.weightx = 0.7; 
        bodyPanel.add(scrollResultados, bodyGbc);

        add(bodyPanel, BorderLayout.CENTER);

        cargarOrigenesYDestinos();
        btnBuscar.addActionListener(e -> ejecutarBusquedaRecursiva());
    }

    private void cargarOrigenesYDestinos() {
        List<String> paradas = gestor.getTodasParadas();
        for (String p : paradas) {
            cbOrigen.addItem(p);
            cbDestino.addItem(p);
        }
    }

    private void ejecutarBusquedaRecursiva() {
        String origen = (String) cbOrigen.getSelectedItem();
        String destino = (String) cbDestino.getSelectedItem();
        
        if (origen != null && origen.equals(destino)) {
            JOptionPane.showMessageDialog(this, "El origen y el destino no pueden ser el mismo.");
            return;
        }

        try {
            // --- LÓGICA DE FILTROS OPCIONALES ---
            String strPrecio = txtMaxPrecio.getText().trim();
            String strTiempo = txtMaxTiempo.getText().trim();

            // Si está vacío, ponemos un valor astronómico para que no filtre nada
            double pMax = strPrecio.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(strPrecio);
            int tMax = strTiempo.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(strTiempo);

            BuscadorRecursivo buscador = new BuscadorRecursivo(gestor.getTodasLasRutas());
            List<List<Ruta>> caminos = buscador.buscarCaminosFiltrados(origen, destino, pMax, tMax);

            actualizarVistaResultados(caminos);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce solo números en los filtros de precio y tiempo.");
        }
    }

    private void actualizarVistaResultados(List<List<Ruta>> caminos) {
        panelResultadosRecursivos.removeAll();
        if (caminos.isEmpty()) {
            panelResultadosRecursivos.add(new JLabel("No se encontraron rutas con los criterios seleccionados."));
        } else {
            for (List<Ruta> camino : caminos) {
                panelResultadosRecursivos.add(crearTarjetaCamino(camino));
                panelResultadosRecursivos.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        panelResultadosRecursivos.revalidate();
        panelResultadosRecursivos.repaint();
    }

    private JPanel crearTarjetaCamino(List<Ruta> camino) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        double precioTotal = camino.stream().mapToDouble(Ruta::getPrecio).sum();
        int tiempoTotal = camino.stream().mapToInt(Ruta::getDuracionHoras).sum();

        GridBagConstraints gbc = new GridBagConstraints();
        
        // HTML con ancho controlado para forzar saltos de línea si es necesario
        StringBuilder sb = new StringBuilder("<html><div style='width: 250px;'>");
        sb.append("<font color='#00468C' size='5'><b>");
        for (int i = 0; i < camino.size(); i++) {
            sb.append(camino.get(i).getOrigen().toUpperCase());
            sb.append(" ➜ ");
            if (i == camino.size() - 1) sb.append(camino.get(i).getDestino().toUpperCase());
        }
        sb.append("</b></font><br><br>");
        sb.append("<font color='#666666' size='4'>Escalas: ").append(camino.size() - 1).append("</font></div></html>");
        
        JLabel lblRuta = new JLabel(sb.toString());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        card.add(lblRuta, gbc);

        JPanel panelAccion = new JPanel(new GridLayout(3, 1, 2, 2));
        panelAccion.setOpaque(false);

        JLabel lblPrice = new JLabel(String.format("%.2f €", precioTotal), JLabel.RIGHT);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblPrice.setForeground(new Color(0, 120, 60));

        JLabel lblTime = new JLabel("⏱ " + tiempoTotal + "h total", JLabel.RIGHT);
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton btnComprar = new JButton("RESERVAR");
        btnComprar.setBackground(new Color(0, 100, 200));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.addActionListener(e -> procesarCompraMultiple(camino, precioTotal));

        panelAccion.add(lblPrice);
        panelAccion.add(lblTime);
        panelAccion.add(btnComprar);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 20, 0, 0);
        card.add(panelAccion, gbc);

        return card;
    }

    private void procesarCompraMultiple(List<Ruta> camino, double precioTotal) {
        Usuario u = mainFrame.getUsuarioActual();
        if (u == null) {
            mainFrame.mostrarPanel("LOGIN"); 
            return;
        }

        if (u.getSaldo() < precioTotal) {
            JOptionPane.showMessageDialog(this, "Tu saldo es insuficiente para realizar esta compra.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Confirmar la reserva de este viaje compuesto por " + precioTotal + "€?", 
            "Confirmar Compra", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            UsuarioDAO dao = new UsuarioDAO();
            for (Ruta r : camino) {
                dao.guardarReserva(u, r.getOrigen() + " - " + r.getDestino(), r.getPrecio());
            }
            mainFrame.actualizarSaldoHeader();
            JOptionPane.showMessageDialog(this, "¡Reserva realizada correctamente!");
        }
    }
}