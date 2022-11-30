package meta;

import meta.algoritmo.AEVMedia_CLase3_Grupo5;
import meta.algoritmo.AEvBLXalfa_Clase3_Grupo5;
import meta.algoritmo.AEVMedia_CLase3_Grupo5.*;
import meta.utils.Daido;
import org.apache.log4j.BasicConfigurator;
import meta.utils.Lector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import meta.utils.FuncionesAux.*;
import org.apache.log4j.Logger;

import static meta.algoritmo.AEvBLXalfa_Clase3_Grupo5.Aevblxalfa_clase3_grupo5;
import static meta.utils.DaidoLector.daidos;

public class Main {


    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        Random aleatorio = new Random();
        Lector config = new Lector("src/main/java/meta/config_files/config_ini");
        int d = config.getD();
        ArrayList<String> algoritmos = config.getAlgoritmos();
        double[] Solucion = new double[d];
        ArrayList<String> funciones = config.getFunciones();
        double[] rangoInf = config.getRangoInf();
        double[] rangoSup = config.getRangoSup();
        int poblacion = config.getPoblacion();
        double cruce = config.getCruce();
        double alfa = config.getAlfa();
        double prob_muta = config.getMutacion();
        int evaluaciones = 10000;
        int i = 0;
        Long[] semillas = config.getSemilla();

        for (String funcion : funciones) {
            for (String algoritmo : algoritmos) {
                Logger logger = Logger.getLogger(funciones + "." + algoritmo);
                System.out.println(algoritmo);
                for (Long semilla : semillas) {
                    aleatorio.setSeed(semilla);
                    switch (algoritmo) {
                        case "algevblxalfa" -> {
                            Aevblxalfa_clase3_grupo5(poblacion, d, evaluaciones, Solucion, rangoInf[i], rangoSup[i], prob_muta, cruce, alfa,funcion,semillas, logger);
                        }
                    }
                }
                List<Daido> daidos = daidos("src/main/java/meta/config_files/daido-tra.dat");
            }
            i++;
        }
    }
}
