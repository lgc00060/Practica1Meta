package meta;

import meta.algoritmo.AEvBLXalfa_Clase3_Grupo5;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import meta.utils.Lector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static meta.algoritmo.AED_Clase3_Grupo5.AED;
import static meta.algoritmo.AEvBLXalfa_Clase3_Grupo5.*;
import static meta.algoritmo.AEVMedia_CLase3_Grupo5.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        Random aleatorio = new Random();
        Lector config = new Lector("src/main/java/meta/config_files/config_ini");
        int d = config.getD();
        ArrayList<String> algoritmos = config.getAlgoritmos();
        double[] solu = new double[d];
        ArrayList<String> funciones = config.getFunciones();
        double[] rangoInf = config.getRangoInf();
        double[] rangoSup = config.getRangoSup();
        int poblacion = config.getPoblacion();
        double cruce = config.getCruce();
        double alfa = config.getAlfa();
        double prob_muta = config.getMutacion();
        int i = 0;
        int evaluar = 10000;
        Long[] semillas;
        semillas = (Long[]) config.getSemilla();
    }
}

        //convertir los resultados a CSV
        //  exportCSV(resultadoBL3, "BL3");
        //exportCSV(resultadoBLk, "BLk");
