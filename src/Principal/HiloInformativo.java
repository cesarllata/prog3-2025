package Principal;

import javax.swing.*;
import java.awt.*;

/**
 * Componente que muestra un texto informativo desplazándose de derecha a izquierda en bucle.
 */
public class HiloInformativo extends JComponent {
    private final String texto;
    private final Font font;
    private final Color color;
    private final Timer timer;
    private final int step;       // píxeles por tick (positivo)
    private final int gap;        // separación entre copias del texto
    private int x;                // posición X actual del inicio del texto
    private int textWidth;

    public HiloInformativo(String texto, Font font, Color color, int delayMs, int step) {
        this.texto = texto;
        this.font = font;
        this.color = color;
        this.step = Math.abs(step);
        this.gap = 50;
        setOpaque(true);
        setBackground(new Color(240, 248, 255));
        textWidth = 0;
        x = 0;

        timer = new Timer(delayMs, e -> {
            // avanzar hacia la izquierda
            x -= HiloInformativo.this.step;
            // cuando el texto ha salido completamente por la izquierda, reiniciar fuera por la derecha
            if (x + textWidth < 0) {
                x = getWidth() + gap;
            }
            repaint();
        });

        // iniciar/parar el timer según visibilidad y recalcular medidas al mostrarse
        addHierarchyListener(e -> {
            if (isShowing()) {
                computeTextWidth();
                if (!timer.isRunning()) timer.start();
            } else {
                timer.stop();
            }
        });

        // recalcular si cambia el tamaño
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                computeTextWidth();
            }
        });
    }

    private void computeTextWidth() {
        FontMetrics fm = getFontMetrics(font);
        textWidth = fm.stringWidth(texto);
        // iniciar fuera por la derecha para que entre de derecha a izquierda
        x = getWidth() > 0 ? getWidth() + gap : gap;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // fondo
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // texto
        g.setFont(font);
        g.setColor(color);
        if (textWidth == 0) computeTextWidth();
        FontMetrics fm = g.getFontMetrics();
        int y = (getHeight() + fm.getAscent()) / 2 - 2;
        g.drawString(texto, x, y);

        // dibujar copia para evitar huecos grandes en pantallas anchas
        int segundaX = x - (textWidth + gap);
        if (segundaX + textWidth > 0) {
            g.drawString(texto, segundaX, y);
        }
    }
}