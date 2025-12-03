package BaseDatos;

import java.sql.*;

/**
 * Clase para manejar la conexión a la base de datos
 * Implementa el patrón Singleton para garantizar una única conexión
 */
public class ConexionDB {
    private static ConexionDB instancia;
    private Connection conexion;
    private static final String URL = "jdbc:mysql://localhost:3306/prog3_2025";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Constructor privado para implementar Singleton
     */
    private ConexionDB() {
    }

    /**
     * Obtiene la instancia única de ConexionDB
     *
     * @return Instancia de ConexionDB
     */
    public static synchronized ConexionDB getInstance() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    /**
     * Establece la conexión a la base de datos
     *
     * @return true si la conexión fue exitosa, false en caso contrario
     */
    public boolean conectar() {
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("Conexión a la base de datos exitosa");
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC no encontrado: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la conexión actual
     *
     * @return Objeto Connection
     */
    public Connection getConexion() {
        if (conexion == null) {
            conectar();
        }
        return conexion;
    }

    /**
     * Verifica si la conexión está activa
     *
     * @return true si la conexión está activa, false en caso contrario
     */
    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Desconexión de la base de datos exitosa");
            }
        } catch (SQLException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }
}
