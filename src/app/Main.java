package app;

import app.database.GestorBD;
import app.ui.MainFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 1. Instanciar gestor
        GestorBD gestor = new GestorBD();
        
        // 2. Inicializar (Crear tablas) SOLO UNA VEZ
        gestor.inicializar();
        
        // IMPORTANTE: Cerramos la conexión de inicialización para liberar el archivo
        // Las siguientes conexiones se abrirán bajo demanda en UsuarioDAO
        gestor.cerrar();

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
