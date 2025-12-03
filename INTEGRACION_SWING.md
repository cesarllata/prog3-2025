# 🖥️ INTEGRACIÓN JDBC EN PANELES SWING

## Ejemplos de código para integrar JDBC en tus paneles

---

## 1. LOGINPANEL - Validar credenciales

```java
package Principal;

import BaseDatos.GestorDatos;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPanel() {
        // ... código de inicialización de componentes ...
        
        loginButton.addActionListener(e -> realizarLogin());
    }

    private void realizarLogin() {
        String email = emailField.getText().trim();
        String contrasena = new String(passwordField.getPassword());

        // Validar campos vacíos
        if (email.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor completa todos los campos", 
                "Campos requeridos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Autenticar usuario con JDBC
        Usuario usuario = GestorDatos.autenticar(email, contrasena);
        
        if (usuario != null) {
            JOptionPane.showMessageDialog(this, 
                "¡Bienvenido " + usuario.getNombre() + "!");
            // Aquí cambias al panel principal
            MenuPrincipalPanel panel = new MenuPrincipalPanel(usuario);
            // cambiar contenido del frame...
        } else {
            JOptionPane.showMessageDialog(this, 
                "Email o contraseña incorrectos", 
                "Error de autenticación", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}
```

---

## 2. REGISTROPANEL - Registrar nuevo usuario

```java
package Principal;

import BaseDatos.GestorDatos;
import javax.swing.*;

public class RegistroPanel extends JPanel {
    private JTextField nombreField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField telefonoField;
    private JButton registroButton;

    public RegistroPanel() {
        // ... código de inicialización ...
        
        registroButton.addActionListener(e -> realizarRegistro());
    }

    private void realizarRegistro() {
        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String telefono = telefonoField.getText().trim();

        // Validaciones
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor completa todos los campos", 
                "Campos requeridos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Las contraseñas no coinciden", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            confirmPasswordField.setText("");
            passwordField.setText("");
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, 
                "Email inválido", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Registrar usuario con JDBC
        if (GestorDatos.registrarUsuario(nombre, email, password, telefono)) {
            JOptionPane.showMessageDialog(this, 
                "¡Registro exitoso! Ahora puedes iniciar sesión", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            // Volver a LoginPanel...
        } else {
            JOptionPane.showMessageDialog(this, 
                "El email ya está registrado o hubo un error", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        nombreField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        telefonoField.setText("");
    }
}
```

---

## 3. NUEVARESERVAPANEL - Crear reserva

```java
package Principal;

import BaseDatos.GestorDatos;
import javax.swing.*;
import java.util.List;

public class NuevaReservaPanel extends JPanel {
    private JComboBox<String> origenCombo;
    private JComboBox<String> destinoCombo;
    private JComboBox<String> rutasCombo;
    private JComboBox<String> bonosCombo;
    private JLabel precioLabel;
    private JButton reservarButton;
    private Usuario usuarioActual;

    public NuevaReservaPanel(Usuario usuario) {
        this.usuarioActual = usuario;
        inicializarComponentes();
        cargarOpciones();
    }

    private void inicializarComponentes() {
        // ... código de Swing ...
        origenCombo.addActionListener(e -> actualizarRutas());
        destinoCombo.addActionListener(e -> actualizarRutas());
        rutasCombo.addActionListener(e -> actualizarPrecio());
        bonosCombo.addActionListener(e -> actualizarPrecio());
        reservarButton.addActionListener(e -> crearReserva());
    }

    private void cargarOpciones() {
        // Cargar orígenes
        origenCombo.removeAllItems();
        for (String origen : GestorDatos.obtenerOrigenes()) {
            origenCombo.addItem(origen);
        }

        // Cargar destinos
        destinoCombo.removeAllItems();
        for (String destino : GestorDatos.obtenerDestinos()) {
            destinoCombo.addItem(destino);
        }

        // Cargar bonos
        bonosCombo.removeAllItems();
        bonosCombo.addItem("Sin bono");
        for (String bono : GestorDatos.obtenerNombresBonos()) {
            bonosCombo.addItem(bono);
        }
    }

    private void actualizarRutas() {
        String origen = (String) origenCombo.getSelectedItem();
        String destino = (String) destinoCombo.getSelectedItem();

        if (origen == null || destino == null) return;

        rutasCombo.removeAllItems();
        List<Ruta> rutas = GestorDatos.buscarRutas(origen, destino);

        if (rutas.isEmpty()) {
            rutasCombo.addItem("No hay rutas disponibles");
        } else {
            for (Ruta ruta : rutas) {
                rutasCombo.addItem(ruta.getNombre() + " - $" + ruta.getPrecio());
            }
        }

        actualizarPrecio();
    }

    private void actualizarPrecio() {
        String rutas = (String) rutasCombo.getSelectedItem();
        String bonoSeleccionado = (String) bonosCombo.getSelectedItem();

        if (rutas == null || rutas.contains("No hay rutas")) {
            precioLabel.setText("Precio: $0.00");
            return;
        }

        // Obtener precio base (simplificado)
        double precioBase = Double.parseDouble(rutas.split("\\$")[1]);

        // Aplicar descuento de bono
        double precioFinal = precioBase;
        if (!bonoSeleccionado.equals("Sin bono")) {
            Bono bono = GestorDatos.obtenerBonos().stream()
                    .filter(b -> b.getNombre().equals(bonoSeleccionado))
                    .findFirst()
                    .orElse(null);
            
            if (bono != null) {
                precioFinal = GestorDatos.calcularPrecioConBono(precioBase, bono);
            }
        }

        precioLabel.setText(String.format("Precio: $%.2f", precioFinal));
    }

    private void crearReserva() {
        String origen = (String) origenCombo.getSelectedItem();
        String destino = (String) destinoCombo.getSelectedItem();
        String rutaSeleccionada = (String) rutasCombo.getSelectedItem();

        if (rutaSeleccionada == null || rutaSeleccionada.contains("No hay rutas")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor selecciona una ruta válida", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aquí iría la lógica de crear la reserva en BD
        // Por ahora solo mostramos confirmación
        JOptionPane.showMessageDialog(this, 
            "¡Reserva creada exitosamente!\n" +
            "Origen: " + origen + "\n" +
            "Destino: " + destino + "\n" +
            "Ruta: " + rutaSeleccionada, 
            "Confirmación", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
```

---

## 4. BONOSPANEL - Mostrar bonos vigentes

```java
package Principal;

import BaseDatos.GestorDatos;
import javax.swing.*;
import java.util.List;

public class BonosPanel extends JPanel {
    private JTable tablaBonos;
    private BonosTableModel tableModel;

    public BonosPanel() {
        inicializarComponentes();
        cargarBonos();
    }

    private void inicializarComponentes() {
        tableModel = new BonosTableModel();
        tablaBonos = new JTable(tableModel);
        
        // ... resto de inicialización de Swing ...
    }

    private void cargarBonos() {
        // Obtener bonos desde BD
        List<Bono> bonos = GestorDatos.obtenerBonos();

        // Limpiar tabla
        tableModel.setRowCount(0);

        // Agregar filas
        for (Bono bono : bonos) {
            Object[] fila = {
                bono.getId(),
                bono.getNombre(),
                bono.getDescripcion(),
                bono.getDescuento() + "%",
                bono.getVialesIncluidos(),
                bono.getFechaExpiracion()
            };
            tableModel.addRow(fila);
        }
    }

    // Botón para refrescar bonos
    public void refrescarBonos() {
        cargarBonos();
    }
}
```

---

## 5. MAINFRAME - Inicialización

```java
package Principal;

import BaseDatos.ConexionDB;
import BaseDatos.InicializadorBD;
import javax.swing.*;

public class MainFrame extends JFrame {
    
    public MainFrame() {
        setTitle("Sistema de Reservas de Rutas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Inicializar base de datos al arrancar
        if (!verificarBD()) {
            int resultado = JOptionPane.showConfirmDialog(this,
                "La base de datos no está disponible.\n" +
                "¿Deseas inicializarla ahora?",
                "Base de datos",
                JOptionPane.YES_NO_OPTION);

            if (resultado == JOptionPane.YES_OPTION) {
                if (InicializadorBD.inicializarBD()) {
                    JOptionPane.showMessageDialog(this,
                        "Base de datos inicializada correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error al inicializar la base de datos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Mostrar panel de login
        setContentPane(new LoginPanel());
    }

    private boolean verificarBD() {
        return GestorDatos.verificarConexion();
    }
}
```

---

## 6. PASAR USUARIO ENTRE PANELES

```java
package Principal;

import javax.swing.*;

public class MenuPrincipalPanel extends JPanel {
    private Usuario usuarioActual;

    public MenuPrincipalPanel(Usuario usuario) {
        this.usuarioActual = usuario;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Mostrar datos del usuario
        JLabel bienvenida = new JLabel("Bienvenido, " + usuarioActual.getNombre());
        
        // Botones para diferentes paneles
        JButton botonReservar = new JButton("Nueva Reserva");
        botonReservar.addActionListener(e -> {
            NuevaReservaPanel panel = new NuevaReservaPanel(usuarioActual);
            cambiarPanel(panel);
        });

        JButton botonBonos = new JButton("Ver Bonos");
        botonBonos.addActionListener(e -> {
            BonosPanel panel = new BonosPanel();
            cambiarPanel(panel);
        });

        // ... agregar componentes al panel ...
    }

    private void cambiarPanel(JPanel nuevoPanel) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.setContentPane(nuevoPanel);
            frame.revalidate();
            frame.repaint();
        }
    }
}
```

---

## ✅ CHECKLIST DE INTEGRACIÓN

- [ ] LoginPanel validando credenciales desde BD
- [ ] RegistroPanel creando usuarios en BD
- [ ] NuevaReservaPanel cargando rutas desde BD
- [ ] NuevaReservaPanel cargando bonos desde BD
- [ ] BonosPanel mostrando bonos vigentes
- [ ] Cálculo de precios con descuentos
- [ ] MainFrame verificando conexión a BD
- [ ] Paso de usuario entre paneles

---

**¡Listo para integrar!** 🚀
