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
        double[] resultante = new double[tam];
        double[] mejorPrimero = new double[tampoblacion],mejorSegundo = new double[tampoblacion];
        double[] costes = new double[tampoblacion], costeNuevaGeneracionSegunda = new double[tampoblacion];
        boolean[] marcados=new boolean[tampoblacion];
        double costeMejorPrimero = 0.0;
        double costeMejorSegundo = 0.0;
        boolean enc;
        int c1, c2, c3, c4;
        int posAnt = 0;

        logger.info("Empieza ejecucion EvolutivoMedia: ");

        //cargamos los cromosomas iniciales con aleatorios
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

        logger.info("Poblacion inicial: ");
        for(int i=0;i<tampoblacion;i++){
            for(int j=0;j<tampoblacion;j++){
                logger.info("Cromosoma de la poblacion inicial: "+ cromosomas.get(i)[j]);
            }
        }

        logger.info("Mejor cromosoma de la poblacion inicial es: " + formatoVector(mejorCromosoma));

        while (contador < evaluaciones) {
            //durante 50 veces (tampoblacion) selecciono por torneo a los cromosomas que sean los mas favorables
            torneo(tampoblacion,posicion,costes,cromosomas,nuevaGeneracion,costeNuevaGeneracion);

            //cruce torneo 2 a 2 llamar a la funcion
            for (int i = 0; i < tampoblacion; i++) {
                //cruceTorneo2a2(tampoblacion, nuevaGeneracion, costeNuevaGeneracion, mejorPrimero, mejorSegundo,i,costeMejorPrimero,costeMejorSegundo);


                c1 = aleatorio.nextInt((tampoblacion));
                while (c1 == (c2 = aleatorio.nextInt(tampoblacion))) ;

                //comprobamos si es el mejor1

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
                    cruceMedia(tam,mejorPrimero,mejorSegundo,resultante);
                    nuevaGeneracionSegunda.add(i, resultante);
                    marcados[i] = true;
                } else {
                    nuevaGeneracionSegunda.add(i, mejorPrimero);
                    costeNuevaGeneracionSegunda[i] = costeMejorPrimero;
                }
            }// for

            nuevaGeneracion = nuevaGeneracionSegunda; //continuamos
            costeNuevaGeneracion = costeNuevaGeneracionSegunda;

            //Se realiza la mutacion de los dos padres(genes) cruzados con la probabilidad de mutacion y marcamos los que se han modificado
            mutar(tampoblacion,tam,probabilidadMutacion, rmin,rmax, nuevaGeneracion, marcados);

            // Procedemos a reemplazar.1 calculamos el menos favorable de la POBLACION NUEVA
            for (int i = 0; i < tampoblacion; i++) {
                if (marcados[i]) {
                    costeNuevaGeneracion[i] = evaluaCoste(nuevaGeneracion.get(i), funcion);//Actualizar coste
                    contador++; //Para evaluaciones
                }

                if (costeNuevaGeneracion[i] < mejorCosteHijo) {
                    mejorCosteHijo = costeNuevaGeneracion[i];
                    mejorCromosomaHijo = i;
                }
            }


            enc=false;
            //
            for (int i = 0; i < nuevaGeneracion.size() && !enc; i++) {
                if (mejorCromosoma == nuevaGeneracion.get(i))
                    enc = true;
            }

            if(!enc){ //torneo k=4
                elitismo(tampoblacion,nuevaGeneracion,mejorCromosoma,costeNuevaGeneracion,mejorCoste);

                //comparamos el mejor con el elite. MEJORA?
                if(mejorCoste<mejorCosteHijo){
                    mejorCosteHijo = mejorCoste;
                    nuevaGeneracion.add(mejorCromosomaHijo, mejorCromosoma);
                }
            }

            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCromosoma = nuevaGeneracion.get(mejorCromosomaHijo);
            mejorCoste = mejorCosteHijo;

            //Actualizamos el mejor global y su coste
            if (mejorCosteHijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteHijo;
                mejorCromosomaGlobal = nuevaGeneracion.get(mejorCromosomaHijo);
            }

            //Actualizamos cromosomas
            costes = costeNuevaGeneracion;
            cromosomas = nuevaGeneracion;
            t++;
        }
        solucion = mejorCromosomaGlobal;

        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial)/1000000;

        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("La semilla utilizada es: " + semilla);
        logger.info("Funcion: " + funcion);
        logger.info("RangoInf: " + rmin);
        logger.info("RangoSup: " + rmax);
        logger.info("El coste del algoritmo EvolutivoMedia es: " + mejorCosteGlobal);
        logger.info("Numero total de Evaluaciones: " + contador);
        logger.info("Numero total de Iteraciones: " + t);
        logger.info("Mejor cromosoma de la nueva poblacion: "+formatoVector(solucion));
    }

}