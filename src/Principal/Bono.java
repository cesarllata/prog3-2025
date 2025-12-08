package Principal;

import java.util.Date;

public class Bono {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int viajesIncluidos;
    private Date fechaExpiracion; // Fecha tope del sistema (ej: fin de año)
    private int duracionDias;     // NUEVO: Días de validez tras la compra (ej: 30)

    public Bono() {
    }

    // Constructor completo actualizado
    public Bono(int id, String nombre, String descripcion, double precio, int viajesIncluidos, Date fechaExpiracion, int duracionDias) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.viajesIncluidos = viajesIncluidos;
        this.fechaExpiracion = fechaExpiracion;
        this.duracionDias = duracionDias;
    }

    // Constructor sin ID (para inserciones manuales si hiciera falta)
    public Bono(String nombre, String descripcion, double precio, int viajesIncluidos, Date fechaExpiracion, int duracionDias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.viajesIncluidos = viajesIncluidos;
        this.fechaExpiracion = fechaExpiracion;
        this.duracionDias = duracionDias;
    }

    // ===== GETTERS Y SETTERS NUEVOS =====
    public int getDuracionDias() { return duracionDias; }
    public void setDuracionDias(int duracionDias) { this.duracionDias = duracionDias; }

    // ===== GETTERS Y SETTERS EXISTENTES =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getViajesIncluidos() { return viajesIncluidos; }
    public void setViajesIncluidos(int viajesIncluidos) { this.viajesIncluidos = viajesIncluidos; }
    public Date getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Date fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
}