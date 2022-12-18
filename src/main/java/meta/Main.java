package meta;
import meta.utils.Lector;
import meta.utils.LectorFicheros;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static meta.algoritmo.SCH_Clase3_Grupo5.SCH_Clase3_Grupo5;
import static meta.utils.FuncionesAux.createAppendersLog;
import static meta.utils.FuncionesAux.getFiles;

public class Main {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        Random aleatorio = new Random();
        double[][] distancia=new double[10][10];
        Lector config = new Lector("src/main/java/meta/config_files/config_ini");
        String ruta = ("src/main/java/meta/config_files/");
        ArrayList<String> algoritmos = config.getAlgoritmos();
        LectorFicheros leerFicheros = null;
        int tam=leerFicheros.getTamanio();
        int[] Solucion = new int[tam];
        int tampoblacion = config.getPoblacion();
        double alfa = config.getAlfa();
        int evaluaciones =config.getEvaluaciones();
        int beta=config.getBeta();
        double q0=config.getQ0();
        double p=config.getP();
        double fi=config.getFi();
        double greedy =0.1;
        Long[] semillas = config.getSemilla();
        final File folder = new File(ruta);
        String archivoConfig= getFiles(folder);
        LectorFicheros fichero= new LectorFicheros();

        for (String algoritmo : algoritmos) {
            System.out.println(algoritmo);
            for (Long semilla : semillas) {
                aleatorio.setSeed(semilla);
                Logger logger = Logger.getLogger(algoritmo + "." + semilla);
                switch (algoritmo) {
                    case "SCH" -> {
                        SCH_Clase3_Grupo5(tampoblacion,q0,p,fi,tam,evaluaciones,greedy,distancia,Solucion,alfa,beta,semilla,logger);
                    }
                }
                //createAppendersLog(archivoConfig,ruta);
            }
            //fichero.leeDatos("src/main/resources/ch130.tsp");
        }
    }
}