package app.models;

/**
 * Clase modelo para representar un tramo de viaje.
 * Utilizada por el buscador recursivo para calcular rutas complejas.
 */
public class Ruta {
    private String origen;
    private String destino;
    private double precio;
    private int duracionHoras;

    /**
     * Constructor completo requerido para la búsqueda recursiva y filtros.
     */
    public Ruta(String origen, String destino, double precio, int duracionHoras) {
        this.origen = origen;
        this.destino = destino;
        this.precio = precio;
        this.duracionHoras = duracionHoras;
    }

    // --- GETTERS ---
    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public double getPrecio() {
        return precio;
    }

    public int getDuracionHoras() {
        return duracionHoras;
    }

    // --- SETTERS (Opcionales) ---
    public void setOrigen(String origen) { this.origen = origen; }
    public void setDestino(String destino) { this.destino = destino; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setDuracionHoras(int duracionHoras) { this.duracionHoras = duracionHoras; }

    @Override
    public String toString() {
        return origen + " -> " + destino + " (" + precio + "€, " + duracionHoras + "h)";
    }
}