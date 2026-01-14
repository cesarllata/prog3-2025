package app.ui;

import javax.swing.*;

/**
 * Temporizador de sesión que corre en un thread y actualiza el label del header cada segundo.
 * Cuenta atrás desde una duración configurable. Cuando expira muestra un diálogo con dos opciones:
 *  - Seguir sesión (restablece la cuenta a la duración inicial)
 *  - Cerrar sesión y salir (cierra sesión y finaliza la aplicación)
 */
/* Uso de IAG para acelerar tareas de desarrollo */
public class SessionTimer {

    private final MainFrame mainFrame;
    private Thread worker;
    private volatile boolean running = false;
    private volatile long secondsLeft; // en segundos

    /** Duración inicial en segundos */
    private final long initialSeconds;

    public SessionTimer(MainFrame mainFrame) {
        this(mainFrame, 3600);
    }

    public SessionTimer(MainFrame mainFrame, long initialSeconds) {
        this.mainFrame = mainFrame;
        this.initialSeconds = initialSeconds;
        this.secondsLeft = initialSeconds;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        if (secondsLeft <= 0) secondsLeft = initialSeconds;
        worker = new Thread(this::runLoop, "SessionTimer-Thread");
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
                // DEBUG: imprimir actualización en consola para verificar que el hilo corre
                System.out.println("DEBUG: timer update: " + text);
                // Actualizar label en EDT
                SwingUtilities.invokeLater(() -> {
                    if (mainFrame != null) mainFrame.actualizarCuentaAtras(text);
                });

                if (secondsLeft <= 0) {
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
                        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                        System.exit(0);
                        running = false;
                        break;
                    }
                }

                Thread.sleep(1000);
                secondsLeft--;
            } catch (InterruptedException e) {
                if (!running) break;
            } catch (Throwable t) {
                t.printStackTrace();
                break;
            }
        }
    }
}
