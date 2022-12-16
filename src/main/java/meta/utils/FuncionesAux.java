package meta.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


public class FuncionesAux {

    public static double greedy(double[][] matriz,int tam){
        int posMenor=-1;
        double menor=Double.MAX_VALUE,coste=0.0;
        int[] vector=new int[tam];

        for(int i=0;i<tam;i++){
            for(int j=0;j<tam;j++){
                if(vector[i]!=j && matriz[vector[i]][j]< menor){
                    menor=matriz[vector[i]][j];
                    posMenor=j;
                }
            }
            vector[i]=posMenor;
        }

        for (int i=0; i<tam; i++){
            coste+= matriz[vector[i]][vector[i]];
        }
        coste+=matriz[vector[0]][vector[tam]];

        return coste;
    }


    public static double randDoubleWithRange(double min, double max) {
        Random random = new Random();
        return random.nextDouble() * (max - min) + min;
    }

    /*private static String convertToLogAppender(String algoritmo, String funcion, String semilla) {
        return "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                " = org.apache.log4j.FileAppender\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".file = src/main/java/meta/ArchivosLog" + funcion + "/" + algoritmo + "/" + semilla + ".log\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".append = false\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".layout = org.apache.log4j.PatternLayout\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".layout.ConversionPattern = %d %c{3} - %m%n\n\n" +
                "log4j.logger." + funcion + "." + algoritmo + "." + semilla +
                " = INFO, " + funcion + "." + algoritmo + "." + semilla + "\n\n";
    }*/


    /*public static void createAppendersLog(String archivosConfig, String ruta) throws IOException {
        FileOutputStream csvFile = new FileOutputStream("src/main/resources/log4j.properties");
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            Lector lector = new Lector(ruta + archivosConfig);
            List<String> algoritmos = lector.getAlgoritmos();
            Long[] semillas = lector.getSemilla();
            List<Long> semillasList = Arrays.stream(semillas).collect(Collectors.toList());
            for (String algoritmo : algoritmos) {
                for (int i = 0; i < lector.getFunciones().size(); i++) {
                    String funcion = lector.getFunciones().get(i);
                    ArrayList <String> ListaSemillas;
                    semillasList.stream()
                            .map(s -> convertToLogAppender(algoritmo, funcion, String.valueOf(s)))
                            .forEach(pw::print);

                    pw.println();

                }

            }
        }
    }*/

    public static String getFiles(final File folder){
        String fileName="";
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                getFiles(fileEntry);
            } else {
                fileName=(fileEntry.getName());
            }
        }
        return fileName;
    }

    public static String formatoVector(double []v){
        StringBuilder vector = new StringBuilder();
        vector.append("[");
        for (int i = 0; i < v.length - 1; i++) {
            vector.append(v[i]).append(", ");
        }
        vector.append(v[v.length - 1]).append("]");
        return vector.toString();
    }
}