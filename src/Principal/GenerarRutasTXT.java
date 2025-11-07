package Principal;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GenerarRutasTXT {
    public static void main(String[] args) {
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
            // --- Ruta muy larga (norte → centro) ---
            "RUTA,G1,Gijon - Oviedo - León - Valladolid - Madrid",
            "Gijon,Oviedo,35,4.00",
            "Oviedo,Leon,90,9.50",
            "Leon,Valladolid,115,11.00",
            "Valladolid,Madrid,120,12.00",
            ""
        };

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("rutas.txt"))) {
            for (String s : bloques) {
                bw.write(s);
                bw.newLine();
            }
            System.out.println("✅ Archivo rutas.txt generado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
