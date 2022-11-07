package meta;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import meta.utils.Lector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static meta.algoritmo.AEvBLXalfa_Clase3_Grupo5.*;



public class Main {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        Random aleatorio= new Random();
        Lector config = new Lector("src/main/java/meta/config_files/config_ini");
        int d = config.getD();
        double[] soluActu = new double[d];
        long iteraciones=config.getIteraciones();
        ArrayList<String> algoritmos = config.getAlgoritmos();
        int k = config.getK();
        float prob_cambio = config.getProb();
        ArrayList<String> funciones = config.getFunciones();
        double oscilacion = config.getProb();
        double [] rangoInf = config.getRangoInf();
        double [] rangoSup = config.getRangoSup();
        int i=0;
        long evaluar=1000;
        Long[] semillas;
        semillas = (Long[]) config.getSemilla();

        for (String funcion : funciones) {
            for (String algoritmo : algoritmos) {
                Logger logger = Logger.getLogger(funcion + "." + algoritmo);
                System.out.println(algoritmo);
                for (Long semilla : semillas) {
                    aleatorio.setSeed(semilla);
                    switch (algoritmo) {
                        /*
                        case "AEVBLXALFA" -> {
                            algBL3Clase1Grupo5(semilla, d, evaluar, soluActu, rangoInf[i], rangoSup[i], funcion, oscilacion, logger);
                        }
                        /*
                        case "blk" -> {
                            algBLkClase1Grupo5(semilla, d,evaluar, soluActu, rangoInf[i], rangoSup[i],funcion, oscilacion, logger);
                        }
                        case "tabu" -> {
                            algBTabuClase1Grupo5(semilla,iteraciones,soluActu,d,rangoInf[i],rangoSup[i],funcion,oscilacion,logger);
                        }
                        case "vns" ->{
                            algBTabuVNSClase1Grupo5(semilla,iteraciones,soluActu,d,rangoInf[i],rangoSup[i],funcion,oscilacion,logger);
                        }

                         */
                    }
                }
            }
            i++;
        }

    }
}