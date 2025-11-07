package Principal;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Es buena práctica iniciar Swing en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}