package Principal;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalPanel extends JPanel {

    private MainFrame mainFrame;

    public MenuPrincipalPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridLayout(3, 1, 15, 15)); 
        setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150)); 
        setBackground(new Color(240, 248, 255));

        // Carga de imágenes
        ImageIcon imgReserva = new ImageIcon("");
        ImageIcon imgRutas = new ImageIcon("");
        ImageIcon imgBonos = new ImageIcon("");

        // Botones con imagen de fondo
        JButton btnReserva = crearBotonConFondo("RESERVA UN VIAJE", imgReserva);
        JButton btnRutas   = crearBotonConFondo("RUTAS", imgRutas);
        JButton btnBonos   = crearBotonConFondo("BONOS", imgBonos);

        // Acciones
        btnReserva.addActionListener(e -> mainFrame.mostrarPanel("RESERVA"));
        btnRutas.addActionListener(e -> mainFrame.mostrarPanel("RUTAS"));
        btnBonos.addActionListener(e -> mainFrame.mostrarPanel("BONOS"));

        add(btnReserva);
        add(btnRutas);
        add(btnBonos);
    }

    private JButton crearBotonConFondo(String texto, ImageIcon imgIcon) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(imgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
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
