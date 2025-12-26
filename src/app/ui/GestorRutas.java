package app.ui;

import app.database.GestorBD;
import app.models.Ruta;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.sql.*;
import java.util.*;
import java.util.List;

public class GestorRutas extends JPanel {

    private MainFrame mainFrame;

    private final Map<String, Point> coordenadas = new HashMap<>();
    private final Map<String, Color> coloresAsignados = new LinkedHashMap<>();
    private final Map<String, RutaInterna> rutasPorId = new LinkedHashMap<>();
    private final Map<String, Set<String>> rutasPorParada = new HashMap<>();

    private static final int ANCHO_BASE = 800;
    private static final int ALTO_BASE = 600;

    // 🎨 PALETA BASE (colores bien separados perceptivamente)
    private static final Color[] PALETA_BASE = {
            new Color(52, 152, 219),   // Azul
            new Color(231, 76, 60),    // Rojo
            new Color(46, 204, 113),   // Verde
            new Color(155, 89, 182),   // Morado
            new Color(241, 196, 15),   // Amarillo
            new Color(230, 126, 34),   // Naranja
            new Color(26, 188, 156),   // Turquesa
            new Color(149, 165, 166),  // Gris elegante
            new Color(192, 57, 43),    // Granate
            new Color(41, 128, 185)    // Azul oscuro
    };

    public GestorRutas() {
        this(null);
    }

    public GestorRutas(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        configurarCoordenadas();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // HEADER
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel lblTitulo = new JLabel("Mapa de Rutas Activas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(lblTitulo);
        add(header, BorderLayout.NORTH);

        add(new MapaPanel(), BorderLayout.CENTER);

        // LEYENDA
        JPanel panelLeyenda = new JPanel();
        panelLeyenda.setLayout(new BoxLayout(panelLeyenda, BoxLayout.Y_AXIS));
        panelLeyenda.setBackground(new Color(250, 250, 250));
        panelLeyenda.setPreferredSize(new Dimension(150, 0));

        JScrollPane scrollLeyenda = new JScrollPane(panelLeyenda);
        scrollLeyenda.setBorder(null);
        add(scrollLeyenda, BorderLayout.EAST);

        cargarDesdeBD(panelLeyenda);
    }

    private void configurarCoordenadas() {
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
    }

    public List<Ruta> getTodasLasRutas() {
        List<Ruta> lista = new ArrayList<>();
        for (RutaInterna ri : rutasPorId.values()) {
            for (Segmento s : ri.getSegmentos()) {
                int horas = Math.max(1, s.getMinutos() / 60);
                lista.add(new Ruta(s.getOrigen(), s.getDestino(), s.getCoste(), horas));
                lista.add(new Ruta(s.getDestino(), s.getOrigen(), s.getCoste(), horas));
            }
        }
        return lista;
    }

    public void cargarDesdeBD(JPanel panelLeyenda) {

        rutasPorId.clear();
        rutasPorParada.clear();
        coloresAsignados.clear();

        if (panelLeyenda != null) {
            panelLeyenda.removeAll();
            JLabel title = new JLabel("LEYENDA");
            title.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panelLeyenda.add(title);
            panelLeyenda.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        GestorBD gestorBD = new GestorBD();

        try (Connection conn = gestorBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ruta")) {

            while (rs.next()) {

                String nombreCompleto = rs.getString("nombre");
                String idRuta = nombreCompleto.contains(":")
                        ? nombreCompleto.split(":")[0].trim()
                        : "R";

                RutaInterna ruta = rutasPorId.computeIfAbsent(idRuta, k -> {
                    try {
                        return new RutaInterna(k, rs.getString("descripcion"));
                    } catch (SQLException e) {
                        return new RutaInterna(k, "");
                    }
                });

                ruta.addSegmento(new Segmento(
                        rs.getString("origen"),
                        rs.getString("destino"),
                        rs.getInt("duracion"),
                        rs.getDouble("precio")
                ));

                rutasPorParada.computeIfAbsent(rs.getString("origen"), k -> new HashSet<>()).add(idRuta);
                rutasPorParada.computeIfAbsent(rs.getString("destino"), k -> new HashSet<>()).add(idRuta);
            }

            if (panelLeyenda != null) {
                for (RutaInterna r : rutasPorId.values()) {
                    JLabel item = new JLabel("● Ruta " + r.getId());
                    item.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
                    item.setForeground(getColorPorRuta(r.getId()));
                    panelLeyenda.add(item);
                    panelLeyenda.add(Box.createRigidArea(new Dimension(0, 8)));
                }
                panelLeyenda.revalidate();
                panelLeyenda.repaint();
            }

            repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class MapaPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double escala = Math.min(
                    (double) getWidth() / ANCHO_BASE,
                    (double) getHeight() / ALTO_BASE
            );

            g2.translate(
                    (getWidth() - ANCHO_BASE * escala) / 2,
                    (getHeight() - ALTO_BASE * escala) / 2
            );
            g2.scale(escala, escala);

            g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            for (RutaInterna ruta : rutasPorId.values()) {
                g2.setColor(getColorPorRuta(ruta.getId()));
                for (Segmento seg : ruta.getSegmentos()) {
                    Point p1 = coordenadas.get(seg.getOrigen());
                    Point p2 = coordenadas.get(seg.getDestino());
                    if (p1 != null && p2 != null) {
                        g2.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
                    }
                }
            }

            for (Map.Entry<String, Point> entry : coordenadas.entrySet()) {
                if (rutasPorParada.containsKey(entry.getKey())) {
                    Point p = entry.getValue();
                    g2.setColor(new Color(44, 62, 80));
                    g2.fillOval(p.x - 5, p.y - 5, 10, 10);
                    g2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
                    g2.drawString(entry.getKey(), p.x + 12, p.y + 4);
                }
            }
        }
    }

    // 🎯 ASIGNACIÓN DE COLOR ESTÉTICA Y SIN REPETICIONES PERCEPTIVAS
    private Color getColorPorRuta(String idRuta) {

        if ("M1".equalsIgnoreCase(idRuta)) {
            return Color.BLACK;
        }

        return coloresAsignados.computeIfAbsent(idRuta, k -> {

            int index = coloresAsignados.size();
            Color base = PALETA_BASE[index % PALETA_BASE.length];
            int ciclo = index / PALETA_BASE.length;

            float[] hsb = Color.RGBtoHSB(
                    base.getRed(),
                    base.getGreen(),
                    base.getBlue(),
                    null
            );

            float saturation = Math.max(0.6f, hsb[1] - ciclo * 0.08f);
            float brightness = Math.max(0.65f, hsb[2] - ciclo * 0.06f);

            return Color.getHSBColor(hsb[0], saturation, brightness);
        });
    }

    public List<String> getTodasParadas() {
        List<String> list = new ArrayList<>(rutasPorParada.keySet());
        Collections.sort(list);
        return list;
    }

    // ===================== MODELOS INTERNOS =====================

    public static class RutaInterna {
        private final String id, nombre;
        private final List<Segmento> segmentos = new ArrayList<>();

        public RutaInterna(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public String getId() {
            return id;
        }

        public List<Segmento> getSegmentos() {
            return segmentos;
        }

        public void addSegmento(Segmento s) {
            segmentos.add(s);
        }
    }

    public static class Segmento {
        private final String origen, destino;
        private final int minutos;
        private final double coste;

        public Segmento(String o, String d, int m, double c) {
            origen = o;
            destino = d;
            minutos = m;
            coste = c;
        }

        public String getOrigen() {
            return origen;
        }

        public String getDestino() {
            return destino;
        }

        public int getMinutos() {
            return minutos;
        }

        public double getCoste() {
            return coste;
        }
    }
}
