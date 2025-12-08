package Principal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerarRutasTXT {
    public static void main(String[] args) {
        // Tus datos originales (intactos)
        String[] bloques = {
            // --- Rutas del Norte ---
            "RUTA,A0,Bilbao - Gijón vía Santander",
            "Bilbao,Santander,60,7.00",
            "Santander,Gijon,75,8.00",
            "",
            "RUTA,A1,Bilbao - San Sebastián - Pamplona",
            "Bilbao,San Sebastian,70,7.50",
            "San Sebastian,Pamplona,90,9.00",
            "",
            "RUTA,A2,Bilbao - Vitoria - Logroño - Zaragoza",
            "Bilbao,Vitoria,65,7.00",
            "Vitoria,Logrono,55,6.50",
            "Logrono,Zaragoza,90,9.50",
            "",
            // --- Madrid y Centro ---
            "RUTA,M1,Madrid - Burgos - Bilbao",
            "Madrid,Burgos,145,15.00",
            "Burgos,Bilbao,95,10.00",
            "",
            "RUTA,M2,Madrid - Zaragoza - Barcelona",
            "Madrid,Zaragoza,190,18.00",
            "Zaragoza,Barcelona,200,20.00",
            "",
            // --- Mediterráneo ---
            "RUTA,C1,Barcelona - Tarragona - Valencia",
            "Barcelona,Tarragona,70,6.00",
            "Tarragona,Castellon,65,6.50",
            "Castellon,Valencia,65,6.00",
            "",
            "RUTA,C2,Valencia - Alicante - Murcia - Cartagena",
            "Valencia,Alicante,105,9.00",
            "Alicante,Murcia,65,6.00",
            "Murcia,Cartagena,45,5.00",
            "",
            // --- Galicia ---
            "RUTA,N1,Oporto - Vigo - A Coruña",
            "Oporto,Vigo,95,11.00",
            "Vigo,A Coruna,120,13.00",
            "",
            // --- Ruta Gijon Madrid ---
            "RUTA,G1,Gijon - Oviedo - León - Valladolid - Madrid",
            "Gijon,Oviedo,35,4.00",
            "Oviedo,Leon,90,9.50",
            "Leon,Valladolid,115,11.00",
            "Valladolid,Madrid,120,12.00",
            ""
        };

        // Aseguramos que la carpeta exista
        File carpeta = new File("resources/csv");
        if (!carpeta.exists()) carpeta.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("resources/csv/rutas.csv"))) {
            
            String idRutaActual = "";
            String descRutaActual = "";

            for (String s : bloques) {
                s = s.trim();
                if (s.isEmpty()) continue;

                // Si es cabecera (RUTA,...) guardamos los datos para usarlos después
                if (s.startsWith("RUTA,")) {
                    String[] partes = s.split(",", 3);
                    idRutaActual = partes[1].trim(); // Ejemplo: A0
                    descRutaActual = partes[2].trim(); // Ejemplo: Bilbao - Gijón...
                } 
                // Si es un tramo normal (Bilbao,Santander...) lo escribimos en el CSV
                else {
                    String[] datos = s.split(",");
                    if (datos.length >= 4) {
                        String origen = datos[0].trim();
                        String destino = datos[1].trim();
                        String tiempo = datos[2].trim();
                        String precio = datos[3].trim();

                        // Construimos un nombre único: "A0: Bilbao - Santander"
                        String nombreCompuesto = idRutaActual + ": " + origen + " - " + destino;

                        // Escribimos en formato CSV estándar separado por punto y coma
                        // Formato: Nombre;Descripcion;Origen;Destino;Duracion;Precio
                        String lineaCSV = nombreCompuesto + ";" + descRutaActual + ";" + origen + ";" + destino + ";" + tiempo + ";" + precio;
                        
                        bw.write(lineaCSV);
                        bw.newLine();
                    }
                }
            }
            System.out.println("✅ Archivo resources/csv/rutas.csv generado correctamente.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}