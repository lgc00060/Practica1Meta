package meta.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


public class FuncionesAux {

    public static double greedy(double[][] matrizDistancias,int tam){
        boolean visitadas[] = new boolean[tam];
        double coste=0.0;
        int contador = 1;
        int posAct = -1;
        double valorMenor;
        int posicionMenor;

        for(int i = 0; i < tam; i++)
            visitadas[i] = false;

        visitadas[0] = true;

        while(contador < tam) {
            valorMenor = Double.MAX_VALUE;
            posicionMenor = 0;
            for(int i = 0; i < tam; i++) {
                if(visitadas[i] == false && matrizDistancias[posAct][i] < valorMenor && i != posAct){
                    valorMenor = matrizDistancias[posAct][i];
                    posicionMenor = i;
                }
            }

            visitadas[posicionMenor] = true;
            coste += valorMenor;
            posAct = posicionMenor;
            contador++;
        }

        return coste;
    }


    public static double randDoubleWithRange(double min, double max) {
        Random random = new Random();
        return random.nextDouble() * (max - min) + min;
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