package Principal;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.sql.*; // Importante para la BD
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class GestorRutas extends JPanel {
    private MainFrame mainFrame;
    private final Map<String, Point> coordenadas = new HashMap<>();
    
    private final Map<String, Color> coloresAsignados = new HashMap<>();
    private int indiceColorActual = 0; 
    
    private final Map<String, Ruta> rutasPorId = new LinkedHashMap<>();
    private final Map<String, Set<String>> rutasPorParada = new HashMap<>();

    private static final int ANCHO_BASE = 800;
    private static final int ALTO_BASE = 600;

    public GestorRutas() {
        this(null);
    }

    public GestorRutas(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        // --- COORDENADAS (Se mantienen igual) ---
        coordenadas.put("A Coruna", new Point(50, 80));
        coordenadas.put("Vigo", new Point(50, 140));
        coordenadas.put("Oporto", new Point(50, 220));
        coordenadas.put("Gijon", new Point(160, 70));
        coordenadas.put("Oviedo", new Point(160, 100));
        coordenadas.put("Leon", new Point(180, 160));
        coordenadas.put("Santander", new Point(280, 70));
        coordenadas.put("Bilbao", new Point(360, 80));
        coordenadas.put("San Sebastian", new Point(420, 75));
        coordenadas.put("Pamplona", new Point(440, 110));
        coordenadas.put("Vitoria", new Point(360, 120));
        coordenadas.put("Logrono", new Point(400, 150));
        coordenadas.put("Burgos", new Point(300, 170));
        coordenadas.put("Valladolid", new Point(260, 220));
        coordenadas.put("Madrid", new Point(340, 300)); 
        coordenadas.put("Zaragoza", new Point(480, 200));
        coordenadas.put("Barcelona", new Point(600, 160));
        coordenadas.put("Tarragona", new Point(580, 210));
        coordenadas.put("Castellon", new Point(560, 280));
        coordenadas.put("Valencia", new Point(540, 330));
        coordenadas.put("Alicante", new Point(550, 400));
        coordenadas.put("Murcia", new Point(520, 430));
        coordenadas.put("Cartagena", new Point(540, 460));

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- HEADER ---
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.LIGHT_GRAY));
        
        JLabel lblTitulo = new JLabel("Mapa de Rutas Activas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(40, 40, 40));
        header.add(lblTitulo);
        add(header, BorderLayout.NORTH);

        // --- MAPA ---
        MapaPanel mapaPanel = new MapaPanel();
        add(mapaPanel, BorderLayout.CENTER);

        // --- LEYENDA ---
        JPanel panelLeyenda = new JPanel();
        panelLeyenda.setLayout(new BoxLayout(panelLeyenda, BoxLayout.Y_AXIS));
        panelLeyenda.setBackground(new Color(250, 250, 250));
        panelLeyenda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelLeyenda.setPreferredSize(new Dimension(140, 0));
        
        JLabel titleLeyenda = new JLabel("LEYENDA");
        titleLeyenda.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLeyenda.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLeyenda.add(titleLeyenda);
        panelLeyenda.add(Box.createRigidArea(new Dimension(0, 15)));

        JScrollPane scrollLeyenda = new JScrollPane(panelLeyenda);
        scrollLeyenda.setBorder(null);
        add(scrollLeyenda, BorderLayout.EAST);

        // --- AQUÍ ESTÁ EL CAMBIO IMPORTANTE: CARGAR DESDE BD ---
        cargarDesdeBD(panelLeyenda);
    }

    // --- NUEVO MÉTODO PARA LEER DE SQLITE ---
    public void cargarDesdeBD(JPanel panelLeyenda) {
        rutasPorId.clear();
        rutasPorParada.clear();
        coloresAsignados.clear();
        indiceColorActual = 0;
        
        if(panelLeyenda != null) {
            while(panelLeyenda.getComponentCount() > 2) panelLeyenda.remove(2);
        }

        // Usamos una conexión temporal solo para leer las rutas y pintar el mapa
        GestorBD gestorBD = new GestorBD();
        Connection conn = gestorBD.getConnection();

        String sql = "SELECT * FROM ruta";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nombreCompleto = rs.getString("nombre"); // Ej: "A0: Bilbao - Santander"
                String desc = rs.getString("descripcion");
                String origen = rs.getString("origen");
                String destino = rs.getString("destino");
                int duracion = rs.getInt("duracion");
                double precio = rs.getDouble("precio");

                // Extraer el ID corto (A0, M1, etc.) del nombre completo
                String idRuta = "R"; // Valor por defecto
                if (nombreCompleto.contains(":")) {
                    idRuta = nombreCompleto.split(":")[0].trim();
                }

                // Recuperar o crear la Ruta lógica en memoria
                Ruta rutaObj = rutasPorId.computeIfAbsent(idRuta, k -> new Ruta(k, desc));

                // Crear el segmento
                Segmento s = new Segmento(origen, destino, duracion, precio);
                rutaObj.addSegmento(s);

                // Registrar paradas
                rutasPorParada.computeIfAbsent(origen, k -> new HashSet<>()).add(idRuta);
                rutasPorParada.computeIfAbsent(destino, k -> new HashSet<>()).add(idRuta);
            }

            // Una vez cargados los datos, actualizamos la leyenda
            for (Ruta r : rutasPorId.values()) {
                Color c = getColorPorRuta(r.getId());
                if(panelLeyenda != null) {
                    JLabel item = new JLabel("● Ruta " + r.getId());
                    item.setForeground(c);
                    item.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    item.setAlignmentX(Component.LEFT_ALIGNMENT);
                    item.setToolTipText(r.getNombre());
                    panelLeyenda.add(item);
                    panelLeyenda.add(Box.createRigidArea(new Dimension(0, 8)));
                }
            }

            if(panelLeyenda != null) {
                panelLeyenda.revalidate();
                panelLeyenda.repaint();
            }
            repaint(); // Repintar mapa

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error leyendo rutas de la BD: " + e.getMessage());
        } finally {
            // No cerramos el GestorBD aquí si lo usamos en otros sitios, 
            // pero para esta carga puntual está bien, o dejar que Main lo gestione.
            // gestorBD.cerrar(); 
        }
    }

    // --- CLASE DEL MAPA (Visual mejorado - IDÉNTICO AL ANTERIOR) ---
    private class MapaPanel extends JPanel {
        public MapaPanel() {
            setBackground(new Color(245, 247, 250));
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            double escalaX = (double) getWidth() / ANCHO_BASE;
            double escalaY = (double) getHeight() / ALTO_BASE;
            double escala = Math.min(escalaX, escalaY);
            
            double offsetX = (getWidth() - (ANCHO_BASE * escala)) / 2;
            double offsetY = (getHeight() - (ALTO_BASE * escala)) / 2;

            AffineTransform originalTransform = g2.getTransform();
            g2.translate(offsetX, offsetY);
            g2.scale(escala, escala);

            // 1. DIBUJAR CONEXIONES 
            g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            for (Ruta ruta : rutasPorId.values()) {
                g2.setColor(getColorPorRuta(ruta.getId()));
                for (Segmento seg : ruta.getSegmentos()) {
                    Point p1 = coordenadas.get(seg.getOrigen());
                    Point p2 = coordenadas.get(seg.getDestino());
                    if (p1 != null && p2 != null) {
                        g2.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
                    }
                }
            }

            // 2. DIBUJAR CIUDADES Y ETIQUETAS 
            for (Map.Entry<String, Point> entry : coordenadas.entrySet()) {
                String ciudad = entry.getKey();
                Point p = entry.getValue();
                boolean activa = rutasPorParada.containsKey(ciudad);

                if (activa) {
                    g2.setColor(new Color(50, 50, 50));
                    g2.fillOval(p.x - 6, p.y - 6, 12, 12);
                    g2.setColor(Color.WHITE);
                    g2.fillOval(p.x - 3, p.y - 3, 6, 6);

                    g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                    FontMetrics fm = g2.getFontMetrics();
                    int textW = fm.stringWidth(ciudad);
                    int textH = fm.getHeight();

                    int labelX = p.x + 12;
                    int labelY = p.y + 4;

                    if (ciudad.equals("Gijon") || ciudad.equals("Santander") || 
                        ciudad.equals("Bilbao") || ciudad.equals("San Sebastian") || 
                        ciudad.equals("Oviedo") || ciudad.equals("A Coruna")) {
                        
                        labelX = p.x - (textW / 2);
                        labelY = p.y - 10;          
                    }

                    g2.setColor(new Color(255, 255, 255, 200));
                    g2.fillRect(labelX - 2, labelY - textH + 2, textW + 4, textH);

                    g2.setColor(Color.DARK_GRAY);
                    g2.drawString(ciudad, labelX, labelY);
                }
            }
            g2.setTransform(originalTransform);
        }
    }

    // --- LÓGICA DE RUTAS BIDIRECCIONALES (IDÉNTICO AL ANTERIOR) ---
    public List<OpcionDestino> getDestinosDirectosDesde(String origen) {
        List<OpcionDestino> res = new ArrayList<>();
        Set<String> rutas = rutasPorParada.getOrDefault(origen, Collections.emptySet());

        for (String id : rutas) {
            Ruta r = rutasPorId.get(id);
            List<String> paradas = r.getParadasOrdenadas();
            int idx = paradas.indexOf(origen);
            if (idx == -1) continue;

            // 1. SENTIDO IDA
            for (int j = idx + 1; j < paradas.size(); j++) {
                int min = 0;
                double cost = 0.0;
                for (int k = idx; k < j; k++) {
                    Segmento seg = r.getSegmentos().get(k);
                    min += seg.getMinutos();
                    cost += seg.getCoste();
                }
                res.add(new OpcionDestino(r.getId(), r.getNombre(), paradas.get(j), min, cost));
            }

            // 2. SENTIDO VUELTA
            for (int j = idx - 1; j >= 0; j--) {
                int min = 0;
                double cost = 0.0;
                for (int k = idx; k > j; k--) {
                    Segmento seg = r.getSegmentos().get(k - 1);
                    min += seg.getMinutos();
                    cost += seg.getCoste();
                }
                res.add(new OpcionDestino(r.getId(), r.getNombre(), paradas.get(j), min, cost));
            }
        }
        
        res.sort(Comparator.comparingInt(OpcionDestino::getMinutos));
        return res;
    }

    public List<String> getTodasParadas() {
        List<String> list = new ArrayList<>(rutasPorParada.keySet());
        Collections.sort(list);
        return list;
    }

    // --- COLORES ---
    private final Color[] PALETA_COLORES = {
        Color.RED, new Color(0, 0, 200), new Color(0, 180, 0), new Color(255, 140, 0),
        new Color(148, 0, 211), new Color(0, 200, 200), Color.MAGENTA, new Color(139, 69, 19),
        new Color(255, 105, 180), new Color(128, 128, 0), new Color(0, 0, 0), new Color(75, 0, 130)
    };

    private Color getColorPorRuta(String idRuta) {
        if (coloresAsignados.containsKey(idRuta)) return coloresAsignados.get(idRuta);
        Color colorAsignado = PALETA_COLORES[indiceColorActual % PALETA_COLORES.length];
        coloresAsignados.put(idRuta, colorAsignado);
        indiceColorActual++;
        return colorAsignado;
    }

    // --- MODELOS ---
    public static class Ruta {
        private final String id;
        private final String nombre;
        private final List<Segmento> segmentos = new ArrayList<>();
        public Ruta(String id, String nombre) { this.id = id; this.nombre = nombre; }
        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public List<Segmento> getSegmentos() { return segmentos; }
        public void addSegmento(Segmento s) { segmentos.add(s); }
        public List<String> getParadasOrdenadas() {
            List<String> p = new ArrayList<>();
            if (segmentos.isEmpty()) return p;
            p.add(segmentos.get(0).getOrigen());
            for (Segmento s : segmentos) p.add(s.getDestino());
            return p;
        }
    }

    public static class Segmento {
        private final String origen, destino;
        private final int minutos;
        private final double coste;
        public Segmento(String o, String d, int m, double c) { origen=o; destino=d; minutos=m; coste=c; }
        public String getOrigen() { return origen; }
        public String getDestino() { return destino; }
        public int getMinutos() { return minutos; }
        public double getCoste() { return coste; }
    }

    public static class OpcionDestino {
        private final String idRuta, nombreRuta, destino;
        private final int minutos;
        private final double coste;
        public OpcionDestino(String idRuta, String nombreRuta, String destino, int m, double c) {
            this.idRuta=idRuta; this.nombreRuta=nombreRuta; this.destino=destino; this.minutos=m; this.coste=c;
        }
        public int getMinutos() { return minutos; }
        @Override public String toString() {
            return destino + "  (" + idRuta + ") — " + minutos + " min — " + String.format("%.2f", coste) + "€";
        }
    }
}