# 🗄️ CONFIGURACIÓN DE MYSQL

## Paso 1: Descargar MySQL Server

### Windows:
1. Ve a: https://dev.mysql.com/downloads/mysql/
2. Descarga MySQL Server (versión 8.0 o superior)
3. Ejecuta el instalador `.msi`
4. Acepta los términos y sigue las instrucciones

### macOS:
```bash
brew install mysql
```

### Linux (Ubuntu/Debian):
```bash
sudo apt-get install mysql-server
```

## Paso 2: Iniciar MySQL Server

### Windows:
```powershell
# Si lo instalaste como servicio, debe iniciar automáticamente
# Si no, abre MySQL Command Line Client o:
net start MySQL80  # (el número puede variar según versión)
```

### macOS:
```bash
brew services start mysql
```

### Linux:
```bash
sudo systemctl start mysql
```

## Paso 3: Conectarse a MySQL

### Desde terminal (Windows PowerShell):
```powershell
mysql -u root -p
# Te pedirá contraseña (si la configuraste en la instalación)
```

### Si no tienes contraseña:
```powershell
mysql -u root
```

## Paso 4: Crear la base de datos

Una vez conectado en MySQL, ejecuta:

```sql
-- Crear base de datos
CREATE DATABASE prog3_2025;

-- Usar la base de datos
USE prog3_2025;

-- Crear tabla de usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    telefono VARCHAR(15),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de rutas
CREATE TABLE rutas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    origen VARCHAR(100) NOT NULL,
    destino VARCHAR(100) NOT NULL,
    duracion INT,
    precio DECIMAL(10, 2) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de bonos
CREATE TABLE bonos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    descuento DECIMAL(5, 2) NOT NULL,
    viajes_incluidos INT DEFAULT 0,
    fecha_expiracion DATE NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de reservas
CREATE TABLE reservas (
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

-- Crear índices
CREATE INDEX idx_usuario_email ON usuarios(email);
CREATE INDEX idx_reserva_usuario ON reservas(usuario_id);
CREATE INDEX idx_reserva_ruta ON reservas(ruta_id);
CREATE INDEX idx_rutas_origen_destino ON rutas(origen, destino);
CREATE INDEX idx_bonos_expiracion ON bonos(fecha_expiracion);

-- Verificar que se crearon las tablas
SHOW TABLES;
```

## Paso 5: Insertar datos de ejemplo (opcional)

```sql
INSERT INTO usuarios (nombre, email, contrasena, telefono) VALUES
('Juan Pérez', 'juan@example.com', 'pass123', '123456789'),
('María García', 'maria@example.com', 'pass456', '987654321'),
('Carlos López', 'carlos@example.com', 'pass789', '555666777');

INSERT INTO rutas (nombre, descripcion, origen, destino, duracion, precio) VALUES
('Bilbao - Madrid', 'Ruta directa a la capital', 'Bilbao', 'Madrid', 480, 45.50),
('Bilbao - Barcelona', 'Viaje a la ciudad condal', 'Bilbao', 'Barcelona', 600, 55.00),
('Bilbao - Vitoria', 'Viaje corto por Álava', 'Bilbao', 'Vitoria', 120, 20.00);

INSERT INTO bonos (nombre, descripcion, descuento, viajes_incluidos, fecha_expiracion) VALUES
('Bono 10 viajes', 'Pack de 10 viajes con descuento', 15.00, 10, '2025-12-31'),
('Verano 2025', 'Promoción de verano', 20.00, 5, '2025-09-30'),
('Fidelización', 'Para clientes frecuentes', 30.00, 20, '2026-06-30');
```

## Paso 6: Verificar datos

```sql
-- Ver usuarios
SELECT * FROM usuarios;

-- Ver rutas
SELECT * FROM rutas;

-- Ver bonos
SELECT * FROM bonos;

-- Salir
EXIT;
```

## 🔧 Configuración en Java

Actualiza los valores en `ConexionDB.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/prog3_2025?useSSL=false&serverTimezone=UTC";
private static final String USUARIO = "root";
private static final String CONTRASENA = "";  // O tu contraseña si la configuraste
private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
```

### Explicación de parámetros:
- `useSSL=false`: No usar SSL (para desarrollo local)
- `serverTimezone=UTC`: Especificar zona horaria

## 🐛 Resolver problemas comunes

### "Access denied for user 'root'@'localhost'"
```sql
-- Cambiar contraseña de root (si lo olvidaste)
ALTER USER 'root'@'localhost' IDENTIFIED BY 'nueva_contraseña';
FLUSH PRIVILEGES;
```

### "No route to host" o "Cannot connect"
- Verifica que MySQL Server está corriendo
- Comando: `mysql --version` (para verificar que está instalado)

### "Unknown database 'prog3_2025'"
- Asegúrate de haber ejecutado: `CREATE DATABASE prog3_2025;`

## ✅ Checklist final

- [ ] MySQL Server instalado
- [ ] MySQL Server en ejecución
- [ ] Base de datos `prog3_2025` creada
- [ ] Tablas creadas
- [ ] Datos de ejemplo insertados (opcional)
- [ ] Driver JDBC descargado e instalado en el proyecto
- [ ] Credenciales en `ConexionDB.java` configuradas

---

**Una vez completado esto, ejecuta `PruebaJDBC` para verificar que todo funciona correctamente.**
