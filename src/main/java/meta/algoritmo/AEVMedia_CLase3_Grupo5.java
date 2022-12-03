package meta.algoritmo;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.*;

public class AEVMedia_CLase3_Grupo5 {
    public static void AEVMedia(int tampoblacion, int tam, int evaluaciones, double[] solucion, double rmin, double rmax,
                                double kProbMuta, double probabilidadCruce, double alfa, String funcion, long semilla, Logger logger) {
        long tiempoInicial = System.nanoTime();
        Random aleatorio = new Random();
        int t = 0;
        List<double[]> cromosomas = new ArrayList<>();
        List<double[]> nuevaGeneracion=new ArrayList<>(tampoblacion);
        List<double[]> nuevaGeneracionSegunda=new ArrayList<>(tampoblacion);
        double[] costeNuevaGeneracion= new double[tampoblacion];
        int[] posicion=new int[tampoblacion];
        double[] mejorCromosoma=new double[tampoblacion];
        int mejorCromosomaHijo = 1;
        double mejorCoste =Double.MAX_VALUE;
        double mejorCosteHijo = Double.MAX_VALUE;
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCromosomaGlobal = mejorCromosoma;
        double  probabilidadMutacion= kProbMuta;
        int contador = tampoblacion;
        double[] h = new double[tam];
        double[] mejorPrimero = new double[tampoblacion],mejorSegundo = new double[tampoblacion];
        double[] costes = new double[tampoblacion], costeNuevaGeneracionSegunda = new double[tampoblacion];
        boolean[] marcados=new boolean[tampoblacion];
        double costeMejorPrimero = 0.0;
        double costeMejorSegundo = 0.0;
        boolean enc;

        logger.info("Empieza ejecucion EvolutivoMedia: ");

        double[] v= new double[tampoblacion];
        for (int j = 0; j < tampoblacion; j++) {
            v[j] = randDoubleWithRange(rmin, rmax);
            cromosomas.add(v);
            marcados[j] = false;
        }

        for (int i = 0; i < tampoblacion; i++) {
            costes[i] = evaluaCoste(cromosomas.get(i), funcion);
            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCromosoma = cromosomas.get(i);
            }
        }

        while (contador < evaluaciones) {
            //SELECCION por TORNEO: Calculo de los cromosomas mas prometedores entre cada 2 parejas aleatorias durante tp enfrentamientos
            torneo(tampoblacion,posicion,costes,cromosomas,nuevaGeneracion,costeNuevaGeneracion);

            for (int i = 0; i < tampoblacion; i++) {
                //cruceTorneo2a2(tampoblacion, nuevaGeneracion, costeNuevaGeneracion, mejorPrimero, mejorSegundo,i,costeMejorPrimero,costeMejorSegundo);
                int c1, c2, c3, c4;
                int posAnt = 0;

                c1 = aleatorio.nextInt((tampoblacion));
                while (c1 == (c2 = aleatorio.nextInt(tampoblacion))) ;

                if (costeNuevaGeneracion[c1] < costeNuevaGeneracion[c2]) {
                    mejorPrimero = nuevaGeneracion.get(c1);
                    costeMejorPrimero = costeNuevaGeneracion[c1];
                } else {
                    mejorPrimero = nuevaGeneracion.get(c2);
                    costeMejorPrimero = costeNuevaGeneracion[c2];
                }

                while (posAnt == (c3 = aleatorio.nextInt(tampoblacion))) ;
                while (posAnt == (c4 = aleatorio.nextInt(tampoblacion)));


                if (costeNuevaGeneracion[c3] < costeNuevaGeneracion[c4]) {
                    mejorSegundo = nuevaGeneracion.get(c3);
                    costeMejorSegundo = costeNuevaGeneracion[c3];
                } else {
                    mejorSegundo = nuevaGeneracion.get(c4);
                    costeMejorSegundo = costeNuevaGeneracion[c4];
                }

                double num = aleatorio.nextDouble();
                if (num < probabilidadCruce) {
                    cruceMedia(tam,mejorPrimero,mejorSegundo,h);
                    nuevaGeneracionSegunda.add(i, h);
                    marcados[i] = true;
                } else {
                    nuevaGeneracionSegunda.add(i, mejorPrimero);
                    costeNuevaGeneracionSegunda[i] = costeMejorPrimero;
                }
            }

            nuevaGeneracion = nuevaGeneracionSegunda;
            costeNuevaGeneracion = costeNuevaGeneracionSegunda;

            //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probMutacion
            mutar(tampoblacion,tam,probabilidadMutacion, rmin,rmax, nuevaGeneracion, marcados);

            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion
            //calculaMejorNuevaPoblacion(tampoblacion,marcados,costeNuevaGeneracion,nuevaGeneracion,funcion,contador,mejorCosteHijo,mejorCromosomaHijo);
            for (int i = 0; i < tampoblacion; i++) {
                if (marcados[i]) {
                    costeNuevaGeneracion[i] = evaluaCoste(nuevaGeneracion.get(i), funcion);
                    contador++;
                }

                if (costeNuevaGeneracion[i] < mejorCosteHijo) {
                    mejorCosteHijo = costeNuevaGeneracion[i];
                    mejorCromosomaHijo = i;
                }
            }

            //ELITILISMO
            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
            enc=false;
            for (int i = 0; i < nuevaGeneracion.size() && !enc; i++) {
                if (mejorCromosoma == nuevaGeneracion.get(i))
                    enc = true;
            }

            if(!enc){ //si no sobrevive planteamos un torneo k=4 para elegir el sustituto de la nueva poblacion
                elitismo(tampoblacion,nuevaGeneracion,mejorCromosoma,costeNuevaGeneracion,mejorCoste);

                //actualizamos el mejor con el elite si acaso lo mejora
                if(mejorCoste<mejorCosteHijo){
                    mejorCosteHijo = mejorCoste;
                    nuevaGeneracion.add(mejorCromosomaHijo, mejorCromosoma);
                }
            }

            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCromosoma = nuevaGeneracion.get(mejorCromosomaHijo);
            mejorCoste = mejorCosteHijo;

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
        logger.info("Total Evaluaciones:" + contador);
        logger.info("Total Iteraciones:" + t);
    }

}