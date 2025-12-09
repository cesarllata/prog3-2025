package app.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;

public class GestorBD {

    private static final String PROPERTIES_FILE = "resources/config.properties";
    private Properties properties;
    private Connection connection;

    public GestorBD() {
        properties = new Properties();
        try {
            // 1. Cargar configuración
            File f = new File(PROPERTIES_FILE);
            if (f.exists()) {
                properties.load(new FileInputStream(f));
            } else {
                // Configuración de respaldo por si falla el archivo
                properties.setProperty("db.dir", "db");
                properties.setProperty("db.url", "jdbc:sqlite:${db.dir}/estacion.db");
                properties.setProperty("db.driver", "org.sqlite.JDBC");
            }
            
            String dbDir = properties.getProperty("db.dir");
            String dbUrl = properties.getProperty("db.url").replace("${db.dir}", dbDir);
            String driverClass = properties.getProperty("db.driver");

            // 2. Cargar Driver
            Class.forName(driverClass);

            // 3. SOLUCIÓN AL ERROR DE CARPETA
            // Creamos la carpeta directamente sin buscar padres complejos
            File carpetaDb = new File(dbDir);
            if (!carpetaDb.exists()) {
                carpetaDb.mkdirs();
            }

            // 4. Conectar
            connection = DriverManager.getConnection(dbUrl);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error conectando: " + e.getMessage());
        }
    }

    // Método llamado desde Main.java
    public void inicializar() {
        try {
            // 1. Asegurar que las tablas existen (SIN BORRARLAS)
            crearTablas();

            // 2. Cargar datos del CSV SOLO si la base de datos parece vacía
            // (Miramos si hay bonos creados. Si hay 0, asumimos que es nueva y cargamos CSV)
            if (estaVaciaLaBD()) {
                System.out.println("📂 Base de datos vacía detectada. Cargando datos desde CSV...");
                cargarDatosCSV();
            } else {
                System.out.println("💾 Base de datos con datos previos. Omitiendo carga de CSV para no duplicar/borrar.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() { return connection; }

    public void cerrar() {
        try { if (connection != null && !connection.isClosed()) connection.close(); } 
        catch (SQLException e) { e.printStackTrace(); }
    }

    // --- MÉTODOS PRIVADOS ---

    private boolean estaVaciaLaBD() {
        try {
            Statement stmt = connection.createStatement();
            // Comprobamos si la tabla 'bono' tiene registros
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM bono");
            if (rs.next()) {
                int cantidad = rs.getInt(1);
                return cantidad == 0; // Si es 0, está vacía. Si es > 0, ya tiene datos.
            }
        } catch (SQLException e) {
            // Si da error (ej: la tabla no existía aún), asumimos que está vacía
            return true;
        }
        return true;
    }

    private void crearTablas() throws SQLException {
        if (connection == null) return;
        Statement stmt = connection.createStatement();

        // Usamos IF NOT EXISTS para respetar datos antiguos
        
        // 1. USUARIO 
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "nombre TEXT NOT NULL, " +
                            "email TEXT UNIQUE, " +
                            "contrasena TEXT, " +
                            "telefono TEXT, " +
                            "num_tarjeta TEXT, " + 
                            "saldo REAL DEFAULT 0.0)";
        stmt.executeUpdate(sqlUsuario);

        // 2. RUTA
        String sqlRuta = "CREATE TABLE IF NOT EXISTS ruta (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "nombre TEXT, " +
                         "descripcion TEXT, " +
                         "origen TEXT, " +
                         "destino TEXT, " +
                         "duracion INTEGER, " +
                         "precio REAL)";
        stmt.executeUpdate(sqlRuta);

        // 3. BONO
        String sqlBono = "CREATE TABLE IF NOT EXISTS bono (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "nombre TEXT, " +
                         "descripcion TEXT, " +
                         "precio REAL, " +
                         "viajes_incluidos INTEGER, " +
                         "fecha_expiracion TEXT, " +
                         "duracion_dias INTEGER)";
        stmt.executeUpdate(sqlBono);

        // 4. USUARIO_BONO
        String sqlUserBono = "CREATE TABLE IF NOT EXISTS usuario_bono (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             "usuario_id INTEGER, " +
                             "bono_id INTEGER, " +
                             "nombre_bono TEXT, " +
                             "viajes_restantes INTEGER, " +
                             "fecha_compra TEXT, " +
                             "fecha_caducidad TEXT, " +
                             "FOREIGN KEY(usuario_id) REFERENCES usuario(id), " +
                             "FOREIGN KEY(bono_id) REFERENCES bono(id))";
        stmt.executeUpdate(sqlUserBono);

        // 5. RESERVA
        String sqlReserva = "CREATE TABLE IF NOT EXISTS reserva (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "usuario_id INTEGER, " +
                            "descripcion_ruta TEXT, " +
                            "precio_pagado REAL, " +
                            "fecha_reserva TEXT, " +
                            "FOREIGN KEY(usuario_id) REFERENCES usuario(id))";
        stmt.executeUpdate(sqlReserva);

        stmt.close();
    }

    private void cargarDatosCSV() {
        String csvUsuarios = properties.getProperty("db.csv.usuarios");
        String csvRutas = properties.getProperty("db.csv.rutas");
        String csvBonos = properties.getProperty("db.csv.bonos");

        if (csvUsuarios != null) cargarUsuarios(csvUsuarios);
        if (csvRutas != null)    cargarRutas(csvRutas);
        if (csvBonos != null)    cargarBonos(csvBonos);
    }
    
    private void cargarRutas(String rutaArchivo) {
        if (connection == null) return;
        String sql = "INSERT INTO ruta (nombre, descripcion, origen, destino, duracion, precio) VALUES (?, ?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";");
                if (d.length >= 6) {
                    pstmt.setString(1, d[0]);
                    pstmt.setString(2, d[1]);
                    pstmt.setString(3, d[2]);
                    pstmt.setString(4, d[3]);
                    pstmt.setInt(5, Integer.parseInt(d[4]));
                    pstmt.setDouble(6, Double.parseDouble(d[5]));
                    pstmt.executeUpdate();
                }
            }
            System.out.println("📥 Rutas importadas desde CSV.");
        } catch (Exception e) { System.err.println("⚠️ Error cargando rutas CSV: " + e.getMessage()); }
    }

    private void cargarUsuarios(String rutaArchivo) {
        if (connection == null) return;
        String sql = "INSERT INTO usuario (nombre, email, contrasena, telefono) VALUES (?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";");
                if (d.length >= 4) {
                    pstmt.setString(1, d[0]);
                    pstmt.setString(2, d[1]);
                    pstmt.setString(3, d[2]);
                    pstmt.setString(4, d[3]);
                    pstmt.executeUpdate();
                }
            }
            System.out.println("📥 Usuarios importados desde CSV.");
        } catch (Exception e) { System.err.println("⚠️ Error cargando usuarios CSV: " + e.getMessage()); }
    }

    private void cargarBonos(String rutaArchivo) {
        if (connection == null) return;
        String sql = "INSERT INTO bono (nombre, descripcion, precio, viajes_incluidos, fecha_expiracion, duracion_dias) VALUES (?, ?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";");
                if (d.length >= 6) {
                    pstmt.setString(1, d[0]);
                    pstmt.setString(2, d[1]);
                    pstmt.setDouble(3, Double.parseDouble(d[2]));
                    pstmt.setInt(4, Integer.parseInt(d[3]));
                    pstmt.setString(5, d[4]);
                    pstmt.setInt(6, Integer.parseInt(d[5]));
                    pstmt.executeUpdate();
                }
            }
            System.out.println("📥 Bonos importados desde CSV.");
        } catch (Exception e) { System.err.println("⚠️ Error cargando bonos CSV: " + e.getMessage()); }
    }
}
