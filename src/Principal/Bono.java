package Principal;

import java.util.Date;

/**
 * Clase modelo para representar un bono de viaje
 * Cuenta con getters y setters para ser compatible con JDBC
 */
public class Bono {
    private int id;
    private String nombre;
    private String descripcion;
    private double descuento;  // en porcentaje
    private int vialesIncluidos;  // número de viajes incluidos
    private Date fechaExpiracion;

    /**
     * Constructor vacío requerido por JDBC/DAO
     */
    public Bono() {
    }

    /**
     * Constructor con parámetros principales
     */
    public Bono(String nombre, String descripcion, double descuento, int vialesIncluidos, Date fechaExpiracion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.vialesIncluidos = vialesIncluidos;
        this.fechaExpiracion = fechaExpiracion;
    }

    /**
     * Constructor completo con ID
     */
    public Bono(int id, String nombre, String descripcion, double descuento, int vialesIncluidos, Date fechaExpiracion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.vialesIncluidos = vialesIncluidos;
        this.fechaExpiracion = fechaExpiracion;
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

    public double getDescuento() {
        return descuento;
    }

    public int getVialesIncluidos() {
        return vialesIncluidos;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
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

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public void setVialesIncluidos(int vialesIncluidos) {
        this.vialesIncluidos = vialesIncluidos;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    @Override
    public String toString() {
        return "Bono{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descuento=" + descuento + "%" +
                ", viajes=" + vialesIncluidos +
                ", expira=" + fechaExpiracion +
                '}';
    }
}