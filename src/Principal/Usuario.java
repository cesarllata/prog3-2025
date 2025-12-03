package Principal;

/**
 * Clase modelo para representar un usuario del sistema
 * Cuenta con getters y setters para ser compatible con JDBC
 */
public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String contrasena;
    private String telefono;

    /**
     * Constructor vacío requerido por JDBC/DAO
     */
    public Usuario() {
    }

    /**
     * Constructor con parámetros
     */
    public Usuario(String nombre, String email, String contrasena, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
    }

    /**
     * Constructor completo con ID
     */
    public Usuario(int id, String nombre, String email, String contrasena, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
    }

    // ===== GETTERS =====

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    // ===== SETTERS =====

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}