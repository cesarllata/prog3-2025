package BaseDatos;

import Principal.*;
import java.util.*;

/**
 * Clase auxiliar con métodos útiles para operaciones comunes
 * Simplifica el acceso a los datos desde la UI
 */
public class GestorDatos {
    
    /**
     * Autentica un usuario por email y contraseña
     */
    public static Usuario autenticar(String email, String contrasena) {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) return null;
            
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.obtenerPorEmail(email);
            
            if (usuario != null && usuario.getContrasena().equals(contrasena)) {
                return usuario;
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error en autenticación: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Registra un nuevo usuario
     */
    public static boolean registrarUsuario(String nombre, String email, String contrasena, String telefono) {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) return false;
            
            // Verificar que el email no exista
            UsuarioDAO dao = new UsuarioDAO();
            if (dao.obtenerPorEmail(email) != null) {
                System.out.println("El email ya está registrado");
                return false;
            }
            
            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario(nombre, email, contrasena, telefono);
            return dao.insertar(nuevoUsuario);
        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene todas las rutas disponibles
     */
    public static List<Ruta> obtenerRutas() {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) return new ArrayList<>();
            
            RutaDAO dao = new RutaDAO();
            return dao.obtenerTodas();
        } catch (Exception e) {
            System.err.println("Error al obtener rutas: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Busca rutas por origen y destino
     */
    public static List<Ruta> buscarRutas(String origen, String destino) {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) return new ArrayList<>();
            
            RutaDAO dao = new RutaDAO();
            return dao.obtenerPorOrigenDestino(origen, destino);
        } catch (Exception e) {
            System.err.println("Error al buscar rutas: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene una ruta por su ID
     */
    public static Ruta obtenerRuta(int id) {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) return null;
            
            RutaDAO dao = new RutaDAO();
            return dao.obtenerPorId(id);
        } catch (Exception e) {
            System.err.println("Error al obtener ruta: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene todos los bonos vigentes
     */
    public static List<Bono> obtenerBonos() {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) return new ArrayList<>();
            
            BonoDAO dao = new BonoDAO();
            return dao.obtenerVigentes();
        } catch (Exception e) {
            System.err.println("Error al obtener bonos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene un bono por su ID
     */
    public static Bono obtenerBono(int id) {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) return null;
            
            BonoDAO dao = new BonoDAO();
            return dao.obtenerPorId(id);
        } catch (Exception e) {
            System.err.println("Error al obtener bono: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene un usuario por su ID
     */
    public static Usuario obtenerUsuario(int id) {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) return null;
            
            UsuarioDAO dao = new UsuarioDAO();
            return dao.obtenerPorId(id);
        } catch (Exception e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Actualiza los datos de un usuario
     */
    public static boolean actualizarUsuario(Usuario usuario) {
        try {
            ConexionDB db = ConexionDB.getInstance();
            if (!db.conectar()) return false;
            
            UsuarioDAO dao = new UsuarioDAO();
            return dao.actualizar(usuario);
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene los orígenes disponibles (ciudades únicas)
     */
    public static Set<String> obtenerOrigenes() {
        Set<String> origenes = new HashSet<>();
        try {
            List<Ruta> rutas = obtenerRutas();
            for (Ruta ruta : rutas) {
                origenes.add(ruta.getOrigen());
            }
        } catch (Exception e) {
            System.err.println("Error al obtener orígenes: " + e.getMessage());
        }
        return origenes;
    }
    
    /**
     * Obtiene los destinos disponibles (ciudades únicas)
     */
    public static Set<String> obtenerDestinos() {
        Set<String> destinos = new HashSet<>();
        try {
            List<Ruta> rutas = obtenerRutas();
            for (Ruta ruta : rutas) {
                destinos.add(ruta.getDestino());
            }
        } catch (Exception e) {
            System.err.println("Error al obtener destinos: " + e.getMessage());
        }
        return destinos;
    }
    
    /**
     * Verifica si la base de datos está disponible
     */
    public static boolean verificarConexion() {
        try {
            ConexionDB db = ConexionDB.getInstance();
            return db.conectar();
        } catch (Exception e) {
            System.err.println("Error al verificar conexión: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Calcula el precio final con descuento de bono
     */
    public static double calcularPrecioConBono(double precioBase, Bono bono) {
        if (bono == null) return precioBase;
        
        double descuento = precioBase * (bono.getDescuento() / 100.0);
        return precioBase - descuento;
    }
    
    /**
     * Obtiene una lista de nombres de bonos para combobox
     */
    public static List<String> obtenerNombresBonos() {
        List<String> nombres = new ArrayList<>();
        try {
            List<Bono> bonos = obtenerBonos();
            for (Bono bono : bonos) {
                nombres.add(bono.getNombre());
            }
        } catch (Exception e) {
            System.err.println("Error al obtener nombres de bonos: " + e.getMessage());
        }
        return nombres;
    }
}
