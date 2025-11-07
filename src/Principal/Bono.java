package Principal;

public class Bono {
    private int id; // <-- CAMBIO: de String a int
    private String nombre;
    private String validez;
    private double precio;
    private String descripcion;
    private String vigencia;

    public Bono(int id, String nombre, String validez, double precio, String descripcion,String vigencia) { // <-- CAMBIO
        this.id = id;
        this.nombre = nombre;
        this.validez = validez;
        this.precio = precio;
        this.descripcion = descripcion;
        this.vigencia=vigencia;
    }

    // Getters
    public int getId() { // <-- CAMBIO
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    public String getVigencia() {
        return vigencia;
    }

    public String getValidez() {
        return validez;
    }

    public double getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }
}