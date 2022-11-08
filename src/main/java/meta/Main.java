package meta;

import meta.algoritmo.AEvBLXalfa_Clase3_Grupo5;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import meta.utils.Lector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static meta.algoritmo.AEvBLXalfa_Clase3_Grupo5.*;
import static meta.algoritmo.AEVMedia.*;



public class Main {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        Random aleatorio= new Random();
        Lector config = new Lector("src/main/java/meta/config_files/config_ini");
        int d = config.getD();
        ArrayList<String> algoritmos = config.getAlgoritmos();
        double[] solu = new double[d];
        int k = config.getK();
        float prob_cambio = config.getProb();
        ArrayList<String> funciones = config.getFunciones();
        double oscilacion = config.getProb();
        double [] rangoInf = config.getRangoInf();
        double [] rangoSup = config.getRangoSup();
        int  poblacion= config.getPoblacion();
        double cruce= config.getCruce();
        double alfa= config.getAlfa();
        double prob_muta= config.getMutacion();

        int i=0;
        long evaluar=10000;
        Long[] semillas;
        semillas = (Long[]) config.getSemilla();

        for (String funcion : funciones) {
            for (String algoritmo : algoritmos) {
                Logger logger = Logger.getLogger(funcion + "." + algoritmo);
                System.out.println(algoritmo);
                for (Long semilla : semillas) {
                    aleatorio.setSeed(semilla);
                    switch (algoritmo) {

                        case "AEVBLXALFA" -> {
                            AEVBLXALFA(poblacion,d,evaluar,solu,rangoInf[i],rangoSup[i],prob_muta,cruce,alfa,funcion, semilla,logger);
                        }

                        /*case "AEVMedia" -> {
                            AEVMedia();
                        }*/








                    }
                }
            }

            i++;
        }
    }
}