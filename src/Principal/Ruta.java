package Principal;

import java.util.List;

public class Ruta {
    private String id;
    private String nombre;
    private List<String> lugares;

    public Ruta(String id, String nombre, List<String> lugares) {
        this.id = id;
        this.nombre = nombre;
        this.lugares = lugares;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public List<String> getLugares() { return lugares; }
}
