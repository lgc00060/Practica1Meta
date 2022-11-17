package meta.algoritmo;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Random;
import static meta.utils.FuncionesAux.*;

public class AEvBLXalfa_Clase3_Grupo5 {
    public static void AEVBLXALFA(int tampoblacion, int tam, double evaluaciones, double[] solucion, double rmin, double rmax,double kProbMuta, double probabilidadCruce, double alfa, String funcion, Long semilla, Logger logger) {
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

        logger.info("Empieza ejecucion algoritmo evolutivoBLXAlfa: ");

        for (int i = 0; i < tampoblacion; i++) {
            marcados[i] = false;
        }

        cargaCromosomasIniciales(tampoblacion,cromosomas,rmin,rmax,funcion,costes,tam,mejorCoste,mejorCromosoma);

        while (contador < evaluaciones) {
            //seleccion por torneo
            torneo(tampoblacion,posicion,costes,cromosomas,nuevaGeneracion,costeNuevaGeneracion);

            cruceTorneo2a2(tam,tampoblacion,h,costes,nuevaGeneracion,probabilidadCruce,marcados,nuevaGeneracionSegunda,costeNuevaGeneracion,costeNuevaGeneracionSegunda,
            alfa,rmin,rmax);

            //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probabilidadMutacion
            mutar(tampoblacion,tam,probabilidadMutacion,rmin,rmax,nuevaGeneracion,marcados);

            //actualizamos el coste de los modificados
            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion
            calculaMejorPeor(tampoblacion,marcados,costeNuevaGeneracion,nuevaGeneracion,funcion,contador,peorCosteHijo,peor,mejorCosteHijo,mejorCromosomaHijo);

            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
            boolean enc = false;
            for (int i = 0; i < nuevaGeneracion.size() && !enc; i++) {
                if (mejorCromosoma == nuevaGeneracion.get(i)) {
                    enc = true;
                }
            }
            if (!enc) { //si no sobrevive planteamos un torneo k=4 para elegir el sustituto de la nueva poblacion
                int p1, p2, p3 = random.nextInt(tampoblacion - 1 - 0) + 0, p4 = random.nextInt(tampoblacion - 1 - 0) + 0;
                p1 = random.nextInt(tampoblacion - 1 - 0) + 0;

                while (p1 == (p2 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
                while (p1 == p2 && p2 == p3) ;
                while (p1 == p2 && p2 == p3 && p3 == p4) ;

                if (costeNuevaGeneracion[p1] > costeNuevaGeneracion[p2] && costeNuevaGeneracion[p1] > costeNuevaGeneracion[p3] && costeNuevaGeneracion[p1] > costeNuevaGeneracion[p4])
                    peor = p1;
                else if (costeNuevaGeneracion[p2] > costeNuevaGeneracion[p1] && costeNuevaGeneracion[p2] > costeNuevaGeneracion[p3] && costeNuevaGeneracion[p2] > costeNuevaGeneracion[p4])
                    peor = p2;
                else if (costeNuevaGeneracion[p3] > costeNuevaGeneracion[p1] && costeNuevaGeneracion[p3] > costeNuevaGeneracion[p2] && costeNuevaGeneracion[p3] > costeNuevaGeneracion[p4])
                    peor = p3;
                else
                    peor = p4;

                nuevaGeneracion.add(peor, mejorCromosoma);
                costeNuevaGeneracion[peor] = mejorCoste;

                //actualizamos el mejor con el elite si acaso lo mejora NEW
                if(mejorCoste<mejorCosteHijo){
                    mejorCosteHijo = mejorCoste;
                    nuevaGeneracion.add(mejorCromosomaHijo, mejorCromosoma);
                }
            }
            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCromosoma = nuevaGeneracion.get(mejorCromosomaHijo);
            mejorCoste = mejorCosteHijo;

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
            //si mejora
            actualizarMejorCromosoma(mejorCosteHijo,mejorCosteGlobal,mejorCromosomaGlobal,nuevaGeneracion,mejorCromosomaHijo);

            //Actualizo cromosomas con nuevaGeneracion, para la siguiente generacion
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
        logger.info("El coste del algoritmo Evolutivo es:" + mejorCosteGlobal);
        logger.info("La semilla es:" + semilla);
        System.out.println("Total Evaluaciones:" + contador);
        System.out.println(" Total Iteraciones:" + t);

    }

}