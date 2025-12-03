package BaseDatos;

import Principal.*;
import java.util.Date;
import java.util.List;

/**
 * Clase de prueba para verificar que JDBC funciona correctamente
 */
public class PruebaJDBC {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("PRUEBA DE JDBC - Sistema de Reservas");
        System.out.println("========================================\n");
        
        // Paso 1: Inicializar base de datos
        System.out.println("1. INICIALIZANDO BASE DE DATOS...");
        if (!InicializadorBD.inicializarBD()) {
            System.err.println("Error: No se pudo inicializar la base de datos");
            return;
        }
        
        System.out.println("\n2. PROBANDO OPERACIONES CON USUARIOS...");
        pruebaUsuarios();
        
        System.out.println("\n3. PROBANDO OPERACIONES CON RUTAS...");
        pruebaRutas();
        
        System.out.println("\n4. PROBANDO OPERACIONES CON BONOS...");
        pruebaBonos();
        
        System.out.println("\n========================================");
        System.out.println("✓ TODAS LAS PRUEBAS COMPLETADAS");
        System.out.println("========================================");
    }
    
    private static void pruebaUsuarios() {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) {
                System.err.println("Error: No se pudo conectar");
                return;
            }
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            
            // Crear nuevo usuario
            System.out.println("  • Creando nuevo usuario...");
            Usuario usuario = new Usuario("Pedro González", "pedro@email.com", "pass123", "612345678");
            if (usuarioDAO.insertar(usuario)) {
                System.out.println("    ✓ Usuario creado exitosamente");
            }
            
            // Obtener usuario por email
            System.out.println("  • Buscando usuario por email...");
            Usuario encontrado = usuarioDAO.obtenerPorEmail("pedro@email.com");
            if (encontrado != null) {
                System.out.println("    ✓ Usuario encontrado: " + encontrado.getNombre());
            }
            
            // Listar todos los usuarios
            System.out.println("  • Listando todos los usuarios...");
            List<Usuario> usuarios = usuarioDAO.obtenerTodos();
            System.out.println("    ✓ Total de usuarios: " + usuarios.size());
            for (Usuario u : usuarios) {
                System.out.println("      - " + u.getNombre() + " (" + u.getEmail() + ")");
            }
            
            db.desconectar();
        } catch (Exception e) {
            System.err.println("Error en prueba de usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void pruebaRutas() {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) {
                System.err.println("Error: No se pudo conectar");
                return;
            }
            
            RutaDAO rutaDAO = new RutaDAO();
            
            // Crear nueva ruta
            System.out.println("  • Creando nueva ruta...");
            Ruta ruta = new Ruta("Vizcaya Express", "Viaje rápido por la costa vasca", 
                                "Bilbao", "Vitoria", 120, 35.50);
            if (rutaDAO.insertar(ruta)) {
                System.out.println("    ✓ Ruta creada exitosamente");
            }
            
            // Listar todas las rutas
            System.out.println("  • Listando todas las rutas...");
            List<Ruta> rutas = rutaDAO.obtenerTodas();
            System.out.println("    ✓ Total de rutas: " + rutas.size());
            for (Ruta r : rutas) {
                System.out.println("      - " + r.getNombre() + " (" + r.getOrigen() + " → " + r.getDestino() + ")");
            }
            
            // Buscar rutas por origen y destino
            System.out.println("  • Buscando rutas Bilbao → Madrid...");
            List<Ruta> rutasEncontradas = rutaDAO.obtenerPorOrigenDestino("Bilbao", "Madrid");
            System.out.println("    ✓ Rutas encontradas: " + rutasEncontradas.size());
            
            db.desconectar();
        } catch (Exception e) {
            System.err.println("Error en prueba de rutas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void pruebaBonos() {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) {
                System.err.println("Error: No se pudo conectar");
                return;
            }
            
            BonoDAO bonoDAO = new BonoDAO();
            
            // Crear nuevo bono
            System.out.println("  • Creando nuevo bono...");
            Date fechaExpiracion = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000); // 30 días
            Bono bono = new Bono("Bono Navideño", "Descuento especial para Navidad", 
                               25.0, 15, fechaExpiracion);
            if (bonoDAO.insertar(bono)) {
                System.out.println("    ✓ Bono creado exitosamente");
            }
            
            // Listar todos los bonos
            System.out.println("  • Listando todos los bonos...");
            List<Bono> bonos = bonoDAO.obtenerTodos();
            System.out.println("    ✓ Total de bonos: " + bonos.size());
            for (Bono b : bonos) {
                System.out.println("      - " + b.getNombre() + " (" + b.getDescuento() + "% descuento)");
            }
            
            // Listar bonos vigentes
            System.out.println("  • Listando bonos vigentes...");
            List<Bono> bonosaActivos = bonoDAO.obtenerVigentes();
            System.out.println("    ✓ Bonos vigentes: " + bonosaActivos.size());
            
            db.desconectar();
        } catch (Exception e) {
            System.err.println("Error en prueba de bonos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
