package meta;
import meta.algoritmo.SCH_Clase3_Grupo5;
import meta.utils.FuncionesAux;
import meta.utils.Lector;
import meta.utils.LectorTSP;
import meta.utils.TSP;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


import static meta.utils.FuncionesAux.createAppendersLog;
import static meta.utils.FuncionesAux.getFiles;
import static meta.utils.LectorTSP.tspLector;

public class Main {
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        Random aleatorio = new Random();
        Lector config = new Lector("src/main/java/meta/config_files/config_ini");
        String ruta = ("src/main/java/meta/config_files/");
        ArrayList<String> algoritmos = config.getAlgoritmos();
        int tam;
        double[][]distancia;
        int[] Solucion;


        ArrayList<String>Datos=config.getDatos();
        System.out.println(Datos.get(0));

        TSP tsp = tspLector("src/main/resources/ch130.tsp");



        int tampoblacion = config.getPoblacion();
        double alfa = config.getAlfa();

        double coste = 0.0;

        int evaluaciones =config.getEvaluaciones();
        int beta=config.getBeta();
        double q0=config.getQ0();
        double p=config.getP();
        double fi=config.getFi();
        Long[] semillas = config.getSemilla();
        final File folder = new File(ruta);
        String archivoConfig= getFiles(folder);

        for(String datos: Datos) {
            System.out.println(datos);
            for (String algoritmo : algoritmos) {
                for (Long semilla : semillas) {
                    aleatorio.setSeed(semilla);
                    Logger logger = Logger.getLogger(datos + "." + algoritmo + "." + semilla);
                    switch (datos) {
                        case "ch130.tsp" -> {
                                distancia=tsp.getMatriz();
                                tam=tsp.getDimension();
                                coste=FuncionesAux.greedy(distancia,tam);

                            SCH_Clase3_Grupo5 alg1=new SCH_Clase3_Grupo5();
                            alg1.SCH_Clase3_Grupo5(tampoblacion, q0, p, fi, tam, evaluaciones, coste, distancia, alfa, beta, semilla, logger);
                        }

                        /*case "d15112.tsp" -> {
                                d15112.LectorDatos("src/main/resources/d15112.tsp");
                                tam= d15112.getTamanio();
                                distancia=new double[tam][tam];
                                distancia=d15112.getMatrizDistancias();
                                Solucion = new int[tam];
                                greedy=f.greedy(distancia,tam);

                                SCH_Clase3_Grupo5(tampoblacion, q0, p, fi, tam, evaluaciones, greedy, distancia, Solucion, alfa, beta, semilla, logger);
                        }

                        case "pr144.tsp" -> {
                                pr144.LectorDatos("src/main/resources/pr144.tsp");
                                tam= pr144.getTamanio();
                                distancia=new double[tam][tam];
                                distancia=pr144.getMatrizDistancias();
                                Solucion = new int[tam];
                                greedy=f.greedy(distancia,tam);

                                SCH_Clase3_Grupo5(tampoblacion, q0, p, fi, tam, evaluaciones, greedy, distancia, Solucion, alfa, beta, semilla, logger);
                        }*/
                    }
                    createAppendersLog(archivoConfig,ruta);
                }

            }
        }
    }
}