package meta.utils;
import meta.funciones.Funciones;
import meta.algoritmo.AEvBLXalfa_Clase3_Grupo5;
import meta.algoritmo.AED_Clase3_Grupo5;
import meta.algoritmo.AEVMedia_CLase3_Grupo5;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GeneradorLog {
    public GeneradorLog(){
        texto=new StringBuilder();
    }
    private StringBuilder texto; //esto es lo que escribe en el fichero
    public static void Log(String ruta, String texto) {  //funcion que crea el fichero
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);
            pw.print(texto);
        } catch (IOException e) {

        } finally {
            try {
                if (fichero != null)
                    fichero.close();
            } catch (IOException e) {
            }

        }
    }
    public void escritura(String algoritmo,String funcion, String semilla,long tiempoInicial,long tiempoFinal, double[] solucion){ //aqui añadimos el texto que queramos mostrar
        String cadena= "algoritmo";
        texto.append(cadena);




        Log("/Users/laura/Desktop/Practica1Meta /src/main/java/meta/ArchivosLog"+ algoritmo + semilla + funcion + ".txt",getTexto());

    }
    public String getTexto(){          //funcion que devuelve contenido que hemos añadido a texto
        return texto.toString();
    };

}
