package app.ui.components;

import app.models.Ruta;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class ViajeCompuestoPanel extends JPanel {

    public ViajeCompuestoPanel(List<Ruta> escalas) {
        // Usamos BorderLayout con separación horizontal para que los elementos no se peguen
        setLayout(new BorderLayout(20, 10));
        
        // Diseño visual: Fondo blanco, bordes redondeados y margen interno
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // --- CÁLCULOS ---
        double totalPrecio = escalas.stream().mapToDouble(Ruta::getPrecio).sum();
        int totalTiempo = escalas.stream().mapToInt(Ruta::getDuracionHoras).sum();
        
        // --- PARTE IZQUIERDA/CENTRO: RUTA ---
        StringBuilder rutaTexto = new StringBuilder("<html><body style='width: 100%;'>");
        rutaTexto.append("<font color='#2C3E50' size='5'><b>");
        for(int i=0; i < escalas.size(); i++) {
            rutaTexto.append(escalas.get(i).getOrigen().toUpperCase());
            if (i == escalas.size() - 1) {
                rutaTexto.append(" ➜ ").append(escalas.get(i).getDestino().toUpperCase());
            } else {
                rutaTexto.append(" ➜ ");
            }
        }
        rutaTexto.append("</b></font><br>");
        rutaTexto.append("<font color='#7F8C8D'>Itinerario compuesto por ").append(escalas.size()).append(" tramos</font>");
        rutaTexto.append("</body></html>");

        JLabel lblRuta = new JLabel(rutaTexto.toString());
        add(lblRuta, BorderLayout.CENTER);

        // --- PARTE DERECHA: INFO Y ACCIÓN ---
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;

        // Precio
        JLabel lblPrecio = new JLabel(String.format("%.2f €", totalPrecio));
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblPrecio.setForeground(new Color(39, 174, 96));
        panelDerecho.add(lblPrecio, gbc);

        // Tiempo
        gbc.gridy = 1;
        JLabel lblTiempo = new JLabel("⏱ Duración: " + totalTiempo + "h");
        lblTiempo.setForeground(Color.GRAY);
        panelDerecho.add(lblTiempo, gbc);

        // Botón
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        JButton btnComprar = new JButton("COMPRAR PACK");
        btnComprar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnComprar.setBackground(new Color(41, 128, 185));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelDerecho.add(btnComprar, gbc);

        add(panelDerecho, BorderLayout.EAST);

        // --- RESPONSIVIDAD ---
        // Permitimos que el panel crezca horizontalmente sin límite (Integer.MAX_VALUE)
        // Pero limitamos su altura máxima para que no se deforme en vertical
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
    }
}