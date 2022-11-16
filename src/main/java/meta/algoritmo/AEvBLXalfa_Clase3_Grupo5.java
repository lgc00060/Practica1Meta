package meta.algoritmo;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Random;
import static meta.utils.FuncionesAux.*;

public class AEvBLXalfa_Clase3_Grupo5 {
    public static void AEVBLXALFA(int tampoblacion, int tam, double evaluaciones, double[] solucion, double rmin, double rmax,double kProbMuta, double probabilidadCruce, double alfa, String funcion, Long semilla, Logger logger) {
        long tiempoInicial = System.nanoTime();
        int t = 0;
        List<double[]> cromosomas = new ArrayList<>();
        List<double[]> nuevaGeneracion=new ArrayList<>(tam);
        double[] costeNuevaGeneracion= new double[tampoblacion];
        int[] posicion=new int[tampoblacion];
        double[] mejorCruce=new double[tampoblacion];
        int peor=0;
        Random random = new Random();
        double peorCosteHijo=0.0;
        int mejorCruceHijo = 1;
        double mejorCoste =Double.MAX_VALUE;
        double mejorcostehijo = Integer.MAX_VALUE;
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = mejorCruce;
        double  probabilidadMutacion= kProbMuta;
        int contador = tampoblacion;
        double[] h = new double[tam];
        double[] costes = new double[tampoblacion], costesH = new double[tampoblacion], costesHH = new double[tampoblacion];
        int[] posi = new int[tampoblacion];
        double x;
        int posAnt = 0;
        int c1, c2, c3, c4;
        double costeMejor1, costeMejor2;
        double[] mejor1, mejor2;
        boolean[] marcados=new boolean[tampoblacion];  //marcamos los modificados


        logger.info("Empieza ejecucion algoritmo evolutivoBLXAlfa: ");

        cargaCromosomasIniciales(tampoblacion,cromosomas,rmin,rmax,funcion,costes,tam,mejorCoste,mejorCruce);

        while (contador < evaluaciones) {
            t++;
            //seleccion por torneo
            torneo(tampoblacion,posicion,costes,cromosomas,nuevaGeneracion,costeNuevaGeneracion);

            ////CRUZAMOS la poblacion tipo torneo de 2 a 2,  tp veces


            for (int i = 0; i < tampoblacion; i++) {
                marcados[i] = false;
            }
            for (int i = 0; i < tampoblacion; i++) {
                c1 = random.nextInt((tampoblacion - 1 - 0) + 0);
                while (c1 == (c2 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
                if (costes[c1] < costes[c2]) {
                    mejor1 = nuevaGeneracion.get(c1);
                    costeMejor1 = costes[c1];
                } else {
                    mejor1 = nuevaGeneracion.get(c2);
                    costeMejor1 = costes[c2];
                }
                while (posAnt == (c3 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
                while (posAnt == (c4 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;

                c3 = random.nextInt(tampoblacion - 1 - 0) + 0;
                while (c3 == (c4 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
                if (costes[c3] < costes[c4]) {
                    mejor2 = nuevaGeneracion.get(c3);
                    costeMejor2 = costes[c3];
                } else {
                    mejor2 = nuevaGeneracion.get(c4);
                    costeMejor2 = costes[c4];
                }
                x = random.nextDouble();
                if (x < probabilidadCruce) {
                    //cruceBlX(tam, mejor1, mejor2, alfa, h, rangoMin, rangoMax);
                    nuevaGeneracion.add(i, h);
                    marcados[i] = true;
                } else {
                    nuevaGeneracion.add(i, mejor1);
                    costesHH[i] = costeMejor1;
                }
            }
            nuevaGeneracion = nuevaGeneracion;
            costeNuevaGeneracion = costesHH;

            //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probabilidadMutacion
            mutar(tampoblacion,tam,probabilidadMutacion,rmin,nuevaGeneracion,marcados); //mirar esto

            //actualizamos el coste de los modificados
            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion
            calculaMejorPeor(tampoblacion,marcados,costeNuevaGeneracion,nuevaGeneracion,funcion,contador,peorCosteHijo,peor,mejorcostehijo,mejorCruceHijo);

            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
            boolean enc = false;
            for (int i = 0; i < nuevaGeneracion.size() && !enc; i++) {
                if (mejorCruce == nuevaGeneracion.get(i)) {
                    enc = true;
                }
            }
            if (!enc) { //si no sobrevive planteamos un torneo k=4 para elegir el sustituto de la nueva poblacion
                int p1, p2, p3 = random.nextInt(tampoblacion - 1 - 0) + 0, p4 = random.nextInt(tampoblacion - 1 - 0) + 0;
                p1 = random.nextInt(tampoblacion - 1 - 0) + 0;
                while (p1 == (p2 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
//                while (p1 == p2 == p3) ;
//                while (p1 == p2 == p3 == p4) ;
                if (costesH[p1] > costesH[p2] && costesH[p1] > costesH[p3] && costesH[p1] > costesH[p4])
                    peor = p1;
                else if (costesH[p2] > costesH[p1] && costesH[p2] > costesH[p3] && costesH[p2] > costesH[p4])
                    peor = p2;
                else if (costesH[p3] > costesH[p1] && costesH[p3] > costesH[p2] && costesH[p3] > costesH[p4])
                    peor = p3;
                else
                    peor = p4;

                nuevaGeneracion.add(peor, mejorCruce);
                costesH[peor] = mejorCoste;

                //actualizamos el mejor con el elite si acaso lo mejora NEW
                if(mejorCoste<mejorcostehijo){
                    mejorcostehijo = mejorCoste;
                    nuevaGeneracion.add(mejorCruceHijo, mejorCruce);
                }
            }
            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCruce = nuevaGeneracion.get(mejorCruceHijo);
            mejorCoste = mejorcostehijo;

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
            //si mejora
            actualizarMejorCromosoma(mejorcostehijo,mejorCosteGlobal,mejorCroGlobal,nuevaGeneracion,mejorCruceHijo);

            //Actualizo cromosomas con nuevaGeneracion, para la siguiente generacion
            costes = costeNuevaGeneracion;
            cromosomas = nuevaGeneracion;
        }
        solucion = mejorCroGlobal;

        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial);

//dfsgdfgfdgf
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
