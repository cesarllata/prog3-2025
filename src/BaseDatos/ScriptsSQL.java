package BaseDatos;

/**
 * Clase de utilidad para ejecutar scripts SQL
 * Útil para crear o inicializar la base de datos
 */
public class ScriptsSQL {
    
    /**
     * Script para crear las tablas necesarias en la base de datos
     */
    public static final String CREAR_TABLAS = """
        CREATE TABLE IF NOT EXISTS usuarios (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            email VARCHAR(100) UNIQUE NOT NULL,
            contrasena VARCHAR(255) NOT NULL,
            telefono VARCHAR(15),
            fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
        
        CREATE TABLE IF NOT EXISTS rutas (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            descripcion TEXT,
            origen VARCHAR(100) NOT NULL,
            destino VARCHAR(100) NOT NULL,
            duracion INT,
            precio DECIMAL(10, 2) NOT NULL,
            fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
        
        CREATE TABLE IF NOT EXISTS bonos (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            descripcion TEXT,
            descuento DECIMAL(5, 2) NOT NULL,
            viajes_incluidos INT DEFAULT 0,
            fecha_expiracion DATE NOT NULL,
            fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
        
        CREATE TABLE IF NOT EXISTS reservas (
            id INT AUTO_INCREMENT PRIMARY KEY,
            usuario_id INT NOT NULL,
            ruta_id INT NOT NULL,
            bono_id INT,
            fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            estado VARCHAR(50) DEFAULT 'pendiente',
            precio_final DECIMAL(10, 2),
            FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
            FOREIGN KEY (ruta_id) REFERENCES rutas(id) ON DELETE CASCADE,
            FOREIGN KEY (bono_id) REFERENCES bonos(id) ON DELETE SET NULL
        );
        """;
    
    /**
     * Script para crear índices que mejoren el rendimiento
     */
    public static final String CREAR_INDICES = """
        CREATE INDEX idx_usuario_email ON usuarios(email);
        CREATE INDEX idx_reserva_usuario ON reservas(usuario_id);
        CREATE INDEX idx_reserva_ruta ON reservas(ruta_id);
        CREATE INDEX idx_rutas_origen_destino ON rutas(origen, destino);
        CREATE INDEX idx_bonos_expiracion ON bonos(fecha_expiracion);
        """;
    
    /**
     * Script para insertar datos de ejemplo
     */
    public static final String DATOS_EJEMPLO = """
        INSERT INTO usuarios (nombre, email, contrasena, telefono) VALUES
        ('Juan Pérez', 'juan@example.com', 'pass123', '123456789'),
        ('María García', 'maria@example.com', 'pass456', '987654321');
        
        INSERT INTO rutas (nombre, descripcion, origen, destino, duracion, precio) VALUES
        ('Bilbao - Madrid', 'Ruta directa a la capital', 'Bilbao', 'Madrid', 480, 45.50),
        ('Bilbao - Barcelona', 'Viaje a la ciudad condal', 'Bilbao', 'Barcelona', 600, 55.00);
        
        INSERT INTO bonos (nombre, descripcion, descuento, viajes_incluidos, fecha_expiracion) VALUES
        ('Bono 10 viajes', 'Pack de 10 viajes con descuento', 15.00, 10, '2025-12-31'),
        ('Verano 2025', 'Promoción de verano', 20.00, 5, '2025-09-30');
        """;
}
