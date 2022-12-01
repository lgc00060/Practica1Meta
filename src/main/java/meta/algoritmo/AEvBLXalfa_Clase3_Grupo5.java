package meta.algoritmo;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

//import meta.utils.Archivos_Log;

import java.util.Random;

import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.*;

public class AEvBLXalfa_Clase3_Grupo5 {
    public static void Aevblxalfa_clase3_grupo5(int tampoblacion, int tam, double evaluaciones, double[] solucion, double rmin, double rmax, double kProbMuta, double probabilidadCruce, double alfa, String funcion, Long[] semilla,  Logger logger) {
        //Logger log = Logger.getLogger();
        logger.info("##################EJECUCION AEVBLXALFA############################");
        long tiempoInicial = System.nanoTime();
        Random aleatorio = new Random();
        int t = 0;
        List<double[]> cromosomas = new ArrayList<>();
        List<double[]> nuevaGeneracion=new ArrayList<>(tampoblacion);
        List<double[]> nuevaGeneracionSegunda=new ArrayList<>(tampoblacion);
        double[] costeNuevaGeneracion= new double[tampoblacion];
        int[] posicion=new int[tampoblacion];
        double[] mejorCromosoma=new double[tampoblacion];
        double peorCosteHijo=0.0;
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
        boolean enc=false;

        //logger.info("Empieza ejecucion algoritmo evolutivoBLXAlfa: ");

        double[] v= new double[tam];
        for (int j = 0; j < tampoblacion; j++) {
                v[j] = randDoubleWithRange(rmin, rmax);
            cromosomas.add(v);// Hay que rellenar cromosomas porque lo lee vacío, entonces no se puede hacer un evaluaCoste de una lista vacía
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
            //SELECCION por TORNEO: Calculo de los cromosomas mas prometedores entre cada 2 parejas aleatorias durante tampoblacion enfrentamientos
            torneo(tampoblacion,posicion,costes,cromosomas,nuevaGeneracion,costeNuevaGeneracion);

            for (int i = 0; i < tampoblacion; i++) {
                cruceTorneo2a2(tampoblacion, nuevaGeneracion, costeNuevaGeneracion, mejorPrimero, mejorSegundo,i,costeMejorPrimero,costeMejorSegundo);

                double num = aleatorio.nextDouble();
                if (num < probabilidadCruce) {
                    cruceBlX(tam, mejorPrimero, mejorSegundo, alfa, h, rmin, rmax);
                    nuevaGeneracionSegunda.add(i, h);
                    marcados[i] = true;
                } else {
                    nuevaGeneracionSegunda.add(i, mejorPrimero);
                    costeNuevaGeneracionSegunda[i] = costeMejorPrimero;
                }

            }


            nuevaGeneracion = nuevaGeneracionSegunda;
            costeNuevaGeneracion = costeNuevaGeneracionSegunda;


            //MUTAMOS los genes de los dos padres ya cruzados con probabilidadMutacion
            mutar(tampoblacion,tam,probabilidadMutacion,rmin,rmax,nuevaGeneracion,marcados);

            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion. Actualizamos el coste de los modificados
            calculaMejorNuevaPoblacion(tampoblacion,marcados,costeNuevaGeneracion,nuevaGeneracion, String.valueOf(funcion),contador,peorCosteHijo,mejorCromosomaHijo);

            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
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

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION   si mejora
            if (mejorCosteHijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteHijo;
                mejorCromosomaGlobal = nuevaGeneracion.get(mejorCromosomaHijo);
            }

            //Actualizo cromosomas con nuevaGeneracion, para la siguiente generacion
            costes = costeNuevaGeneracion;
            cromosomas = nuevaGeneracion;

            t++;
        }
        solucion = mejorCromosomaGlobal;
        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial)/1000000;
       // ArchivosLog.estructura("Algoritmo EVBLXAlfa",funcion,semilla,tiempoInicial,tiempoFinal,solucion);


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