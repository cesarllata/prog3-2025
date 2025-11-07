package Principal;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class GestorRutas {

    private final Map<String, Ruta> rutasPorId = new LinkedHashMap<>();
    private final Map<String, Set<String>> rutasPorParada = new HashMap<>();

    public void cargarDesdeFichero(String fichero) {
        rutasPorId.clear();
        rutasPorParada.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            Ruta actual = null;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) { actual = null; continue; }

                if (linea.startsWith("RUTA,")) {
                    String[] p = linea.split(",", 3);
                    actual = new Ruta(p[1].trim(), p.length > 2 ? p[2].trim() : "");
                    rutasPorId.put(actual.getId(), actual);
                } else {
                    String[] p = linea.split(",");
                    if (p.length < 4) continue;
                    Segmento s = new Segmento(p[0].trim(), p[1].trim(),
                            Integer.parseInt(p[2].trim()),
                            Double.parseDouble(p[3].trim()));
                    if (actual != null) {
                        actual.segmentos.add(s);
                        rutasPorParada.computeIfAbsent(s.origen, k -> new HashSet<>()).add(actual.id);
                        rutasPorParada.computeIfAbsent(s.destino, k -> new HashSet<>()).add(actual.id);
                    }
                }
            }
            System.out.println("✅ Rutas cargadas: " + rutasPorId.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getTodasParadas() {
        return new ArrayList<>(rutasPorParada.keySet());
    }

    public List<OpcionDestino> getDestinosDirectosDesde(String origen) {
        List<OpcionDestino> res = new ArrayList<>();
        Set<String> rutas = rutasPorParada.getOrDefault(origen, Collections.emptySet());

        for (String id : rutas) {
            Ruta r = rutasPorId.get(id);
            List<String> paradas = r.getParadasOrdenadas();
            int idx = paradas.indexOf(origen);
            if (idx == -1) continue;

            for (int j = idx + 1; j < paradas.size(); j++) {
                int min = 0;
                double cost = 0.0;

                for (int k = idx; k < j; k++) {
                    Segmento seg = r.segmentos.get(k);
                    min += seg.minutos;
                    cost += seg.coste;
                }

                res.add(new OpcionDestino(r.id, r.nombre, paradas.get(j), min, cost));
            }
        }

        res.sort(Comparator.comparingInt(OpcionDestino::getMinutos));
        return res;
    }

    // ---------- MODELOS ----------
    public static class Ruta {
        private final String id;
        private final String nombre;
        private final List<Segmento> segmentos = new ArrayList<>();

        Ruta(String id, String nombre) { this.id = id; this.nombre = nombre; }

        public String getId() { return id; }
        public String getNombre() { return nombre; }

        public List<String> getParadasOrdenadas() {
            List<String> p = new ArrayList<>();
            if (segmentos.isEmpty()) return p;
            p.add(segmentos.get(0).origen);
            for (Segmento s : segmentos) p.add(s.destino);
            return p;
        }
    }

    public static class Segmento {
        private final String origen, destino;
        private final int minutos;
        private final double coste;
        Segmento(String o, String d, int m, double c) { origen=o; destino=d; minutos=m; coste=c; }
    }

    public static class OpcionDestino {
        private final String idRuta, nombreRuta, destino;
        private final int minutos;
        private final double coste;
        public OpcionDestino(String idRuta, String nombreRuta, String destino, int minutos, double coste) {
            this.idRuta=idRuta; this.nombreRuta=nombreRuta; this.destino=destino; this.minutos=minutos; this.coste=coste;
        }
        public int getMinutos() { return minutos; }

        @Override public String toString() {
            return destino + "  (" + idRuta + ") — " + minutos + " min — " + String.format("%.2f", coste) + "€";
        }
    }
    
}
