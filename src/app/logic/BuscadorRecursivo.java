package app.logic;

import app.models.Ruta;
import java.util.*;

public class BuscadorRecursivo {
    private List<Ruta> todasLasRutas;

    public BuscadorRecursivo(List<Ruta> rutas) {
        this.todasLasRutas = rutas;
    }

    /**
     * Método público para iniciar la búsqueda.
     */
    public List<List<Ruta>> buscarCaminosFiltrados(String actual, String destino, 
                                                   double maxPrecio, int maxHoras) {
        List<List<Ruta>> resultados = new ArrayList<>();
        buscarRecursivo(actual, destino, new ArrayList<>(), new HashSet<>(), 0.0, 0, 
                        maxPrecio, maxHoras, resultados);
        return resultados;
    }

    /**
     * ALGORITMO RECURSIVO (DFS con Backtracking)
     */
    private void buscarRecursivo(String actual, String destino, 
                                 List<Ruta> caminoActual, Set<String> visitados,
                                 double precioAcumulado, int tiempoAcumulado,
                                 double maxPrecio, int maxHoras,
                                 List<List<Ruta>> resultados) {
        
        // Caso base: Hemos llegado al destino
        if (actual.equalsIgnoreCase(destino)) {
            resultados.add(new ArrayList<>(caminoActual));
            return;
        }

        visitados.add(actual);

        for (Ruta ruta : todasLasRutas) {
            // Si la ruta sale de donde estamos y no hemos visitado el destino de esa ruta
            if (ruta.getOrigen().equalsIgnoreCase(actual) && !visitados.contains(ruta.getDestino())) {
                
                double nuevoPrecio = precioAcumulado + ruta.getPrecio();
                int nuevoTiempo = tiempoAcumulado + ruta.getDuracionHoras();

                // FILTROS: Solo seguimos explorando si no superamos los límites
                if (nuevoPrecio <= maxPrecio && nuevoTiempo <= maxHoras) {
                    caminoActual.add(ruta);
                    
                    // LLAMADA RECURSIVA
                    buscarRecursivo(ruta.getDestino(), destino, caminoActual, visitados, 
                                    nuevoPrecio, nuevoTiempo, maxPrecio, maxHoras, resultados);
                    
                    // BACKTRACKING: Quitamos la última ruta para probar otros caminos
                    caminoActual.remove(caminoActual.size() - 1);
                }
            }
        }

        visitados.remove(actual); // Limpiamos para otras ramas de la recursión
    }
}