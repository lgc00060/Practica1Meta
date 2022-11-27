
/*
package meta.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Archivos_Log {
    private StringBuilder texto; //esto es lo que escribe en el fichero

    public Archivos_Log() {
        texto = new StringBuilder();
    } //constructor

    public String getTexto() {          //funcion que devuelve contenido que hemos añadido a texto
        return texto.toString();
    }

    ; //gettexto

    public void addTexto(String texto) {
        this.texto.append(texto);
    } //añadimos texto


    public void estructura(String algoritmo, ArrayList<String> funcion, long[] semilla, double tiempoInicial, double tiempoFinal, double[] solucion) { //aqui añadimos el texto que queramos mostrar

        String cadena = "";
        cadena = "######################[" + algoritmo + "] ############################\n\n";
        texto.append(cadena);

        cadena = "FUNCION: " + funcion + '\n';
        texto.append(cadena);

        cadena = "SEMILLA: " + semilla + '\n';
        texto.append(cadena);

        cadena = "TIEMPO INICIAL: " + tiempoInicial + '\n';
        texto.append(cadena);

        cadena = "TIEMPO FINAL: " + tiempoFinal + '\n';
        texto.append(cadena);

        cadena = "RESULTADO DE LA EJECUCION:\n\n";
        texto.append(cadena);

        cadena = solucion + "\n";
        texto.append(cadena);

        //ahora cuando tenemos la estructura formada, imprimimos en un fichero
        Log("/Users/laura/eclipse-workspace/Practica1Meta/src/main/java/meta/ArchivosLog/" + algoritmo + funcion + semilla + tiempoInicial + tiempoFinal + solucion + ".txt", getTexto());
    }


        public static void Log (String ruta, String texto){  //funcion que crea el fichero
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


    }

 */




