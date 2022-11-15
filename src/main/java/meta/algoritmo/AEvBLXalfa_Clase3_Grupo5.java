package meta.algoritmo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import meta.Main;
import org.apache.log4j.Logger;
import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.*;

public class AEvBLXalfa_Clase3_Grupo5 {
    public static void AEVBLXALFA(int tampoblacion, int tam, double evaluaciones, double[] s, double rmin, double rmax,double kProbMuta, double probabilidadCruce, double alfa, String funcion, Long semilla, Logger logger) {
        long tiempoInicial = System.nanoTime();
        int t = 0;
        List<double[]> cromosomas = new ArrayList<>();
        List<double[]> nuevaGeneracion=new ArrayList<>(tam);
        double[] costes= new double[tampoblacion];
        double[] costeNuevaGeneracion= new double[tampoblacion];
        int[] posicion=new int[tampoblacion];
        double[] mejorCruce=new double[tampoblacion];
        int peor=0;
        double peorCosteHijo=0.0;
        int mejorCruceHijo = 0;
        double mejorCoste =Double.MAX_VALUE;
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = mejorCruce;
        double  probabilidadMutacion= kProbMuta;
        int contador = tampoblacion;
        Random aleatorio = new Random();
        boolean[] marcados=new boolean[tampoblacion];  //marcamos los modificados
        double mejorcostehijo = Integer.MAX_VALUE;

        logger.info("Empieza ejecucion algoritmo evolutivoBLXAlfa: ");

        cargaCromosomasIniciales(tampoblacion,cromosomas,rmin,rmax,funcion,costes,tam,mejorCoste,mejorCruce);

        while (contador < evaluaciones) {
            t++;

            torneo(tampoblacion,posicion,costes,cromosomas,nuevaGeneracion,costeNuevaGeneracion);

            //CRUZAMOS los padres seleccionados con una probabilidad probCruce OPCION 1
            cruce(tampoblacion,nuevaGeneracion,marcados,probabilidadCruce,tam);

            //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probabilidadMutacion
            mutar(tampoblacion,tam,probabilidadMutacion,rmin,nuevaGeneracion,marcados);

            //actualizamos el coste de los modificados
            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion
            calculaMejorPeor(tampoblacion,marcados,costeNuevaGeneracion,nuevaGeneracion,funcion,contador,peorCosteHijo,peor,mejorcostehijo,mejorCruceHijo);
            

            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
            boolean enc = false;
            for (int i = 0; i < nuevaGeneracion.size() && !enc; i++) {
                if (mejorCruce == nuevaGeneracion.get(i)){
                    enc = true;
                }
            }
            mejorCruce = nuevaGeneracion.get(mejorCruceHijo);
            costeNuevaGeneracion[peor] = mejorCoste;

            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCruce = nuevaGeneracion.get(mejorCruceHijo);
            mejorCoste = mejorcostehijo;

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
            //si mejora
            if (mejorcostehijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorcostehijo;
                mejorCroGlobal = nuevaGeneracion.get(mejorCruceHijo);
            }
            System.out.println("Mejor coste;" + mejorCosteGlobal);
            //Actualizo cromosomas con nuevaGeneracion, para la siguiente generacion
            costes = costeNuevaGeneracion;
            cromosomas = nuevaGeneracion;
        }
        s = mejorCroGlobal;

        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial);

        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("Funcion:" + funcion);
        logger.info("RangoInf: " + rmin);
        logger.info("RangoSup: " + rmax);
        logger.info("El coste del algoritmo Evolutivo es:" + mejorCosteGlobal);
        logger.info("La semilla es:" + semilla);
        System.out.println("Total Evaluaciones:" + contador);
        System.out.println(" Total Iteraciones:" + t);
        //return mejorCosteGlobal;
    }

}