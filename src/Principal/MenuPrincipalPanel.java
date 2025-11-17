package Principal;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalPanel extends JPanel {

    private MainFrame mainFrame;

    public MenuPrincipalPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(50, 150, 20, 150));
        setBackground(new Color(240, 248, 255));

        // Panel con los 3 botones (mantiene el GridLayout original)
        JPanel botonesPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        botonesPanel.setOpaque(false);

        // Carga de imágenes (lo he puesto por si ponemos imagenes en los botones en un futuro pero ahora mismo queda mejor en blanco)
        ImageIcon imgReserva = new ImageIcon("");
        ImageIcon imgRutas   = new ImageIcon("");
        ImageIcon imgBonos   = new ImageIcon("");

        // Botones con imagen de fondo
        JButton btnReserva = crearBotonConFondo("RESERVA UN VIAJE", imgReserva);
        JButton btnRutas   = crearBotonConFondo("RUTAS", imgRutas);
        JButton btnBonos   = crearBotonConFondo("BONOS", imgBonos);

        // Acciones
        btnReserva.addActionListener(e -> mainFrame.mostrarPanel("RESERVA"));
        btnRutas.addActionListener(e -> mainFrame.mostrarPanel("RUTAS"));
        btnBonos.addActionListener(e -> mainFrame.mostrarPanel("BONOS"));

        botonesPanel.add(btnReserva);
        botonesPanel.add(btnRutas);
        botonesPanel.add(btnBonos);

        add(botonesPanel, BorderLayout.CENTER);

        // Mensajes del hilo (rotan en bucle)
        String[] mensajes = new String[] {
            "OFERTA: 2x1 en billetes a Bilbao este fin de semana",
            "NUEVO: Rutas panorámicas a San Sebastián desde 9.99€",
            "BONOS: Compra 5 viajes y consigue 1 gratis",
            "DESTINO DESTACADO: Santander - precios especiales"
        };
        String textoConcatenado = String.join("   •   ", mensajes);

        // Hilo informativo debajo de los botones
        HiloInformativo hilo = new HiloInformativo(textoConcatenado, new Font("Arial", Font.BOLD, 16), Color.DARK_GRAY, 18, 2);
        hilo.setPreferredSize(new Dimension(0, 36));
        hilo.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        add(hilo, BorderLayout.SOUTH);
    }

    private JButton crearBotonConFondo(String texto, ImageIcon imgIcon) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                if (imgIcon != null && imgIcon.getImage() != null) {
                    g.drawImage(imgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
                super.paintComponent(g);
            }
        };

        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.CENTER);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setForeground(Color.BLACK);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        return btn;
    }
}
