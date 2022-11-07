package meta;

import meta.algoritmo.AEvBLXalfa_Clase3_Grupo5;
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
                            AEVBLXALFA(tp)
                        }


 */


                    }
                }
            }
            i++;
        }

    }
}