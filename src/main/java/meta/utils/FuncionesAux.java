package meta.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


public class FuncionesAux {

    public static double greedy(double[][] matrizDistancias,int tam){
        Random random = new Random();
        int[] solucion = new int[tam];
        boolean[] marcado = new boolean[tam];
        solucion[0] = random.nextInt(tam);
        marcado[solucion[0]] = true;
        for (int i = 0; i < tam - 1; i++) {
            double menosDist = Double.MAX_VALUE;
            int posMenor = 0;
            for (int j = 0; j < tam; j++) {
                if (solucion[i] != j && matrizDistancias[solucion[i]][j] < menosDist && !marcado[j]) {
                    menosDist = matrizDistancias[solucion[i]][j];
                    posMenor = j;
                }
            }
            solucion[i + 1] = posMenor;
            marcado[posMenor] = true;
        }
        return coste(matrizDistancias, tam,solucion);
    }


    public static double randDoubleWithRange(double min, double max) {
        Random random = new Random();
        return random.nextDouble() * (max - min) + min;
    }

    public static double coste(double[][] matrizDistancias,int tam,int[] vector){
        double coste = 0;
        for (int i = 0; i < tam - 1; i++) {
            coste += matrizDistancias[vector[i]][vector[i + 1]];
        }
        coste += matrizDistancias[vector[0]][vector[tam - 1]];
        return coste;
    }

    private static String convertToLogAppender(String algoritmo, String semilla) {
        return "log4j.appender." + algoritmo + "." + semilla +
                " = org.apache.log4j.FileAppender\n" +
                "log4j.appender." + algoritmo + "." + semilla +
                ".file = src/main/java/meta/ArchivosLog"  + "/" + algoritmo + "/" + semilla + ".log\n" +
                "log4j.appender."  + algoritmo + "." + semilla +
                ".append = false\n" +
                "log4j.appender." + algoritmo + "." + semilla +
                ".layout = org.apache.log4j.PatternLayout\n" +
                "log4j.appender." + algoritmo + "." + semilla +
                ".layout.ConversionPattern = %d %c{3} - %m%n\n\n" +
                "log4j.logger." + algoritmo + "." + semilla +
                " = INFO, " + algoritmo + "." + semilla + "\n\n";
    }


    public static void createAppendersLog(String archivosConfig, String ruta) throws IOException {
        FileOutputStream csvFile = new FileOutputStream("src/main/resources/log4j.properties");
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            Lector lector = new Lector(ruta + archivosConfig);
            List<String> algoritmos = lector.getAlgoritmos();
            Long[] semillas = lector.getSemilla();
            List<Long> semillasList = Arrays.stream(semillas).collect(Collectors.toList());
            for (String algoritmo : algoritmos) {
                    ArrayList <String> ListaSemillas;
                    semillasList.stream()
                            .map(s -> convertToLogAppender(algoritmo, String.valueOf(s)))
                            .forEach(pw::print);

                    pw.println();
            }
        }
    }

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