package Principal;

import javax.swing.*;
import java.awt.*;

public class RegistroPanel extends JPanel {

    private MainFrame mainFrame;

    public RegistroPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridLayout(4, 2, 10, 10));

        JTextField txtNombre = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtPass = new JPasswordField();

        add(new JLabel("Nombre:"));
        add(txtNombre);
        add(new JLabel("Email:"));
        add(txtEmail);
        add(new JLabel("Contraseña:"));
        add(txtPass);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> {

            // Aquí podrías guardar en BBDD o archivo
            Usuario nuevo = new Usuario(txtNombre.getText());

            JOptionPane.showMessageDialog(this,"Usuario registrado con éxito");
            
            // Iniciar sesión automáticamente tras registro
            mainFrame.loginExitoso(nuevo);
        });

        add(btnRegistrar);
    }
}
