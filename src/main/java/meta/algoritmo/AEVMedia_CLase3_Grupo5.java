package meta.algoritmo;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.*;

public class AEVMedia_CLase3_Grupo5 {
    public static void AEVMedia(int tampoblacion, int tam, int evaluaciones, double[] solucion, double rmin, double rmax,
                                double kProbMuta, double kProbCruce, double alfa, String funcion, Long semilla, Logger logger) {
        long tiempoInicial = System.nanoTime();
        Random random = new Random();
        int t = 0;
        List<double[]> cromosomas = new ArrayList<>();
        List<double[]> nuevaGeneracion=new ArrayList<>(tam);
        List<double[]> nuevaGeneracionSegunda=new ArrayList<>(tam);
        double[] costeNuevaGeneracion= new double[tampoblacion];
        int[] posicion=new int[tampoblacion];
        double[] mejorCromosoma=new double[tampoblacion];
        int peor=0;
        double peorCosteHijo=0.0;
        int mejorCromosomaHijo = 1;
        double mejorCoste =Double.MAX_VALUE;
        double mejorCosteHijo = Integer.MAX_VALUE;
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCromosomaGlobal = mejorCromosoma;
        double  probabilidadMutacion= kProbMuta;
        int contador = tampoblacion;
        double[] h = new double[tam];
        double[] costes = new double[tampoblacion], costeNuevaGeneracionSegunda = new double[tampoblacion];
        boolean[] marcados=new boolean[tampoblacion];
        int c2,c3,c4; //posiciones donde almaceno los padres
        int posAnt = 0;
        double [] mejorCromosomaPrimero = new double[tampoblacion];
        double[]mejorCromosomaSegundo= new double[tampoblacion];
        double costeMejorCromosomaPrimero,costeMejorCromosomaSegundo;


        logger.info("Empieza ejecucion EvolutivoMedia: ");

        for (int i = 0; i < tampoblacion; i++) {
            marcados[i] = false;
        }

        cargaCromosomasIniciales(tampoblacion,cromosomas,rmin,rmax,funcion,costes,tam,mejorCoste,mejorCromosoma);

        while (contador < evaluaciones) {
            //SELECCION por TORNEO: Calculo de los cromosomas mas prometedores entre cada 2 parejas aleatorias durante tp enfrentamientos
            torneo(tampoblacion,posicion,costes,cromosomas,nuevaGeneracion,costeNuevaGeneracion);

            //CRUZAMOS los padres seleccionados con una probabilidad probCruce
            cruceTorneo2a2Media(tam,tampoblacion,h,nuevaGeneracion,kProbCruce,marcados,nuevaGeneracionSegunda,costeNuevaGeneracion,costeNuevaGeneracionSegunda,mejorCromosomaPrimero,mejorCromosomaSegundo);

            //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probMutacion
            mutar(tampoblacion,tam,probabilidadMutacion, rmin,rmax, nuevaGeneracion, marcados);

            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion
            calculaMejorNuevaPoblacion(tampoblacion,marcados,costeNuevaGeneracion,nuevaGeneracion,funcion,contador,mejorCosteHijo,mejorCromosomaHijo);

            //ELITILISMO
            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
            elitismo(tampoblacion,nuevaGeneracion,mejorCromosoma,costeNuevaGeneracion,mejorCosteHijo,
            mejorCromosomaHijo,mejorCoste);

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION .Si mejora me quedo con el mejor coste y con el mejorCrhijo
            if (mejorCosteHijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteHijo;
                mejorCromosomaGlobal = nuevaGeneracion.get(mejorCromosomaHijo);
            }

            //Actualizo cromosomas con nuevag, para la siguiente generacion
            costes = costeNuevaGeneracion;
            cromosomas = nuevaGeneracion;

            t++;
        }
        solucion = mejorCromosomaGlobal;

        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial);
        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("Funcion:" + funcion);
        logger.info("RangoInf: " + rmin);
        logger.info("RangoSup: " + rmax);
        logger.info("El coste del algoritmo EvolutivoMedia es:" + mejorCosteGlobal);
        logger.info("La semilla es:" + semilla);
        System.out.println("Total Evaluaciones:" + contador);
        System.out.println(" Total Iteraciones:" + t);
    }

}