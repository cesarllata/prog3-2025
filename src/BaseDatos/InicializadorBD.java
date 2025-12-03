package BaseDatos;

import java.sql.Statement;

/**
 * Clase para inicializar la base de datos
 * Ejecuta los scripts SQL necesarios para crear tablas
 */
public class InicializadorBD {
    
    /**
     * Inicializa la base de datos creando todas las tablas necesarias
     * @return true si la inicialización fue exitosa
     */
    public static boolean inicializarBD() {
        ConexionDB db = ConexionDB.getInstance();
        
        if (!db.conectar()) {
            System.err.println("No se pudo conectar a la base de datos");
            return false;
        }
        
        try {
            Statement stmt = db.getConexion().createStatement();
            
            // Crear tablas
            System.out.println("Creando tablas...");
            String[] sentenciasCrearTablas = ScriptsSQL.CREAR_TABLAS.split(";");
            for (String sentencia : sentenciasCrearTablas) {
                if (!sentencia.trim().isEmpty()) {
                    stmt.execute(sentencia.trim());
                }
            }
            System.out.println("✓ Tablas creadas exitosamente");
            
            // Crear índices
            System.out.println("Creando índices...");
            String[] sentenciasIndices = ScriptsSQL.CREAR_INDICES.split(";");
            for (String sentencia : sentenciasIndices) {
                if (!sentencia.trim().isEmpty()) {
                    try {
                        stmt.execute(sentencia.trim());
                    } catch (Exception e) {
                        // Los índices podrían ya existir, ignoramos el error
                        System.out.println("  (Índice ya existe o error menor)");
                    }
                }
            }
            System.out.println("✓ Índices creados exitosamente");
            
            stmt.close();
            System.out.println("\n✓ Base de datos inicializada correctamente");
            return true;
            
        } catch (Exception e) {
            System.err.println("✗ Error al inicializar base de datos: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            db.desconectar();
        }
    }
    
    /**
     * Inserta datos de ejemplo en la base de datos
     * @return true si la inserción fue exitosa
     */
    public static boolean insertarDatosEjemplo() {
        ConexionDB db = ConexionDB.getInstance();
        
        if (!db.conectar()) {
            System.err.println("No se pudo conectar a la base de datos");
            return false;
        }
        
        try {
            Statement stmt = db.getConexion().createStatement();
            
            System.out.println("Insertando datos de ejemplo...");
            String[] sentenciasDatos = ScriptsSQL.DATOS_EJEMPLO.split(";");
            for (String sentencia : sentenciasDatos) {
                if (!sentencia.trim().isEmpty()) {
                    try {
                        stmt.executeUpdate(sentencia.trim());
                    } catch (Exception e) {
                        // Ignoramos si ya existen
                        System.out.println("  (Registro puede ya existir)");
                    }
                }
            }
            System.out.println("✓ Datos de ejemplo insertados");
            
            stmt.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al insertar datos: " + e.getMessage());
            return false;
        } finally {
            db.desconectar();
        }
    }
}
