package app.models;

/**
 * Clase modelo para representar una ruta de viaje
 * Cuenta con getters y setters para ser compatible con JDBC
 */
public class Ruta {
    private int id;
    private String nombre;
    private String descripcion;
    private String origen;
    private String destino;
    private int duracion;  // en minutos
    private double precio;

    /**
     * Constructor vacío requerido por JDBC/DAO
     */
    public Ruta() {
    }

    /**
     * Constructor con parámetros principales
     */
    public Ruta(String nombre, String descripcion, String origen, String destino, int duracion, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.origen = origen;
        this.destino = destino;
        this.duracion = duracion;
        this.precio = precio;
    }

    /**
     * Constructor completo con ID
     */
    public Ruta(int id, String nombre, String descripcion, String origen, String destino, int duracion, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.origen = origen;
        this.destino = destino;
        this.duracion = duracion;
        this.precio = precio;
    }

    // ===== GETTERS =====

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public int getDuracion() {
        return duracion;
    }

    public double getPrecio() {
        return precio;
    }

    // ===== SETTERS =====

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Ruta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", duracion=" + duracion + " min" +
                ", precio=$" + precio +
                '}';
    }
}
