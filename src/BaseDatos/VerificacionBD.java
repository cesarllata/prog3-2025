package BaseDatos;

/**
 * Clase simple para verificar que MySQL está correctamente configurado
 * Ejecuta: java -cp "bin;lib/mysql-connector-java-8.0.33.jar" BaseDatos.VerificacionBD
 */
public class VerificacionBD {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  VERIFICACIÓN DE CONFIGURACIÓN JDBC/MySQL ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        verificarDriver();
        verificarConexion();
        verificarTablas();
        
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║           VERIFICACIÓN COMPLETADA          ║");
        System.out.println("╚════════════════════════════════════════════╝");
    }
    
    private static void verificarDriver() {
        System.out.println("1. Verificando driver JDBC...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("   ✅ Driver MySQL encontrado\n");
        } catch (ClassNotFoundException e) {
            System.out.println("   ❌ ERROR: Driver no encontrado");
            System.out.println("   Asegúrate de que mysql-connector-java-8.0.33.jar");
            System.out.println("   está en la carpeta lib/ del proyecto\n");
        }
    }
    
    private static void verificarConexion() {
        System.out.println("2. Verificando conexión a MySQL...");
        ConexionDB db = ConexionDB.getInstance();
        
        if (db.conectar()) {
            System.out.println("   ✅ Conexión exitosa");
            System.out.println("   Base de datos: prog3_2025\n");
            db.desconectar();
        } else {
            System.out.println("   ❌ ERROR: No se pudo conectar");
            System.out.println("   Verifica:");
            System.out.println("   - MySQL Server está en ejecución");
            System.out.println("   - Base de datos 'prog3_2025' existe");
            System.out.println("   - Usuario y contraseña en ConexionDB.java\n");
        }
    }
    
    private static void verificarTablas() {
        System.out.println("3. Verificando tablas...");
        ConexionDB db = ConexionDB.getInstance();
        
        if (!db.conectar()) {
            System.out.println("   ⚠️  No se pudo verificar (sin conexión)\n");
            return;
        }
        
        try {
            String[] tablas = {"usuarios", "rutas", "bonos", "reservas"};
            for (String tabla : tablas) {
                if (verificarTablaExiste(tabla)) {
                    System.out.println("   ✅ Tabla '" + tabla + "' existe");
                } else {
                    System.out.println("   ⚠️  Tabla '" + tabla + "' no existe");
                }
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("   ⚠️  No se pudieron verificar las tablas");
            System.out.println("   Ejecuta InicializadorBD para crearlas\n");
        } finally {
            db.desconectar();
        }
    }
    
    private static boolean verificarTablaExiste(String nombreTabla) {
        try {
            ConexionDB db = ConexionDB.getInstance();
            var stmt = db.getConexion().prepareStatement(
                "SELECT 1 FROM information_schema.tables WHERE table_schema = 'prog3_2025' AND table_name = ?");
            stmt.setString(1, nombreTabla);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }
}
