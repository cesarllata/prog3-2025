package app;

import app.ui.MainFrame;
import javax.swing.*;

/**
 * Temporizador de sesión que corre en un thread y actualiza el label del header cada segundo.
 * Cuenta atrás desde 1 hora (3600 segundos). Cuando expira muestra un diálogo con dos opciones:
 *  - Seguir sesión (restablece la cuenta a 1 hora)
 *  - Cerrar sesión y salir (cierra sesión y finaliza la aplicación)
 */
public class SessionTimer {

    private final MainFrame mainFrame;
    private Thread worker;
    private volatile boolean running = false;
    private volatile long secondsLeft = 3600; // 1 hora

    /** Duración inicial en segundos */
    private final long initialSeconds;

    public SessionTimer(MainFrame mainFrame) {
        this(mainFrame, 3600);
    }

    /**
     * Constructor que permite especificar la duración inicial (útil para pruebas)
     * @param mainFrame ventana principal
     * @param initialSeconds duración inicial en segundos
     */
    public SessionTimer(MainFrame mainFrame, long initialSeconds) {
        this.mainFrame = mainFrame;
        this.initialSeconds = initialSeconds;
        this.secondsLeft = initialSeconds;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        if (secondsLeft <= 0) secondsLeft = initialSeconds;
        worker = new Thread(() -> runLoop(), "SessionTimer-Thread");
        worker.setDaemon(true);
        worker.start();
    }

    public synchronized void stopTimer() {
        running = false;
        if (worker != null) worker.interrupt();
    }

    public synchronized void reset() {
        secondsLeft = initialSeconds;
    }

    private void runLoop() {
        while (running) {
            try {
                long hours = secondsLeft / 3600;
                long mins = (secondsLeft % 3600) / 60;
                long secs = secondsLeft % 60;
                String text = String.format("%02d:%02d:%02d", hours, mins, secs);
                // Actualizar label en EDT
                SwingUtilities.invokeLater(() -> {
                    if (mainFrame != null) mainFrame.actualizarCuentaAtras(text);
                });

                if (secondsLeft <= 0) {
                    // Mostrar diálogo de expiración en EDT y esperar la respuesta
                    final int[] choice = new int[1];
                    try {
                        SwingUtilities.invokeAndWait(() -> {
                            String[] options = {"Seguir sesión", "Cerrar sesión y salir"};
                            choice[0] = JOptionPane.showOptionDialog(mainFrame,
                                    "Tiempo de sesión expirado.", "Sesión expirada",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                    null, options, options[0]);
                        });
                    } catch (Exception e) {
                        // Si algo falla al mostrar el diálogo, salimos
                        running = false;
                        break;
                    }

                    if (choice[0] == 0) {
                        // Seguir sesión: reiniciar contador
                        secondsLeft = initialSeconds;
                        continue;
                    } else {
                        // Cerrar sesión y salir
                        SwingUtilities.invokeLater(() -> {
                            if (mainFrame != null) mainFrame.cerrarSesion();
                        });
                        // Pequeña espera para que la UI procese el logout antes de salir
                        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                        System.exit(0);
                        running = false;
                        break;
                    }
                }

                Thread.sleep(1000);
                secondsLeft--;
            } catch (InterruptedException e) {
                // Interrupción: salir si no estamos corriendo
                if (!running) break;
            } catch (Throwable t) {
                t.printStackTrace();
                break;
            }
        }
    }
}
