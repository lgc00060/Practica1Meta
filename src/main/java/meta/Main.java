package meta;

import meta.algoritmo.AEvBLXalfa_Clase3_Grupo5;
import meta.utils.Archivos_Log;
import org.apache.log4j.BasicConfigurator;
import meta.utils.Lector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {


    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        long tiempoInicial = System.nanoTime();
        long tiempoFinal = System.nanoTime();
        Random aleatorio = new Random();
        Lector config = new Lector("/Users/laura/eclipse-workspace/Practica1Meta/src/main/java/meta/utils/config_ini");
        int d = config.getD();
        ArrayList<String> algoritmos = config.getAlgoritmos();
        Archivos_Log archivosLog = new Archivos_Log();
        double[] solucion = new double[d];
        ArrayList<String> funciones = config.getFunciones();
        double[] rangoInf = config.getRangoInf();
        double[] rangoSup = config.getRangoSup();
        int poblacion = config.getPoblacion();
        double cruce = config.getCruce();
        double alfa = config.getAlfa();
        double prob_muta = config.getMutacion();
        int i = 0;
        int evaluaciones = 10000;
        Long[] semillas;
        semillas = (Long[]) config.getSemilla();
        for (String algoritmo : algoritmos) {
            for (long semilla : semillas) {
                double[] vSolucion = new double[d];
                archivosLog.estructura(algoritmo,funciones,semillas,tiempoInicial,tiempoFinal, solucion);
                switch (algoritmo) {
                    case "algevblxalfa" -> {
                        new AEvBLXalfa_Clase3_Grupo5(poblacion, d, evaluaciones, solucion, rangoInf, rangoSup, prob_muta, cruce, alfa, funciones, semillas, archivosLog);
                    }

                    }
                }
            }
        }

        //convertir los resultados a CSV
        //  exportCSV(resultadoBL3, "BL3");
        //exportCSV(resultadoBLk, "BLk");
    }
