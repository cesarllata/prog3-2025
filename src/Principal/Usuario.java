package Principal;

// Clase simple para guardar los datos del usuario
public class Usuario {
    private String nombre;
    // ... otros campos: email, id, etc.

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}