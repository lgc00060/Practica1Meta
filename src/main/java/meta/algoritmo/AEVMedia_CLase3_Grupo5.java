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
        int c2,c3,c4;
        //double[] h1=new double[tam];
        int posAnt = 0;
        double [] mejorCromosomaPrimero,mejorCromosomaSegundo;
        double costeMejorCromosomaPrimero,costeMejorCromosomaSegundo;


        logger.info("Empieza ejecucion EvolutivoMedia: ");


        //Carga de los cromosomas iniciales
        cargaCromosomasIniciales(tampoblacion,cromosomas,rmin,rmax,funcion,costes,tam,mejorCoste,mejorCromosoma);


        //PRINCIPAL: Comienzan las iteraciones
        while (contador < evaluaciones) {
            t++;  //HAY QUE PONERLO ABAJO DEL TODO LO DIJO CRISTOBAL
            //SELECCION por TORNEO: Calculo de los cromosomas mas prometedores entre cada 2 parejas aleatorias
            //durante tp enfrentamiento¡'
            for (int k = 0; k < tampoblacion; k++) {
                int i, j;
                i = random.nextInt(tampoblacion - 1 - 0) + 0;
                while (i == (j = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
                posicion[k] = (costes[i] < costes[j]) ? i : j;
            }
            //Nos quedamos con los cromosomas mas prometedores
            for (int i=0;i<tampoblacion;i++){
                if (posicion[i]==50)
                    posicion[i]--;
                nuevaGeneracion.add(i, cromosomas.get(posicion[i]));
                costeNuevaGeneracion[i] = costes[posicion[i]];
            }
            //CRUZAMOS los padres seleccionados con una probabilidad probCruce
            for (int i = 0; i < tampoblacion; i++) {
                marcados[i] = false;
            }
            for (int i = 0; i < tampoblacion; i++) {
                int  c1 = random.nextInt((tampoblacion - 1 - 0) + 0);
                while (c1 == (c2 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
                if (costeNuevaGeneracion[c1] < costeNuevaGeneracion[c2]) {
                    mejorCromosomaPrimero = nuevaGeneracion.get(c1);
                    costeMejorCromosomaPrimero = costeNuevaGeneracion[c1];
                } else {
                    mejorCromosomaPrimero = nuevaGeneracion.get(c2);
                    costeMejorCromosomaPrimero = costeNuevaGeneracion[c2];
                }
                while (posAnt == (c3 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
                while (posAnt == (c4 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;


                if (costeNuevaGeneracion[c3] < costeNuevaGeneracion[c4]) {
                    mejorCromosomaSegundo = nuevaGeneracion.get(c3);
                    costeMejorCromosomaSegundo = costeNuevaGeneracion[c3];
                } else {
                    mejorCromosomaSegundo = nuevaGeneracion.get(c4);
                    costeMejorCromosomaSegundo = costeNuevaGeneracion[c4];
                }
                double x = random.nextDouble();
                if (x < kProbCruce ) { //si se cumple la probabilidad de cruce  cojo otro aleatorio
                    cruceMedia(tam, mejorCromosomaPrimero, mejorCromosomaSegundo, h); //y hacemos el cruce entre los dos
                    nuevaGeneracion.add(i, h); //algunos cambiaran y otros no
                    marcados[i] = true; //sobre las posiciones de los padres elegidos
                } else {
                    nuevaGeneracion.add(i, mejorCromosomaPrimero);
                    costeNuevaGeneracionSegunda[i] = costeMejorCromosomaPrimero;
                }
            }
            nuevaGeneracion = nuevaGeneracion; //cojo la nuevagg y la copio en nuevag
            costeNuevaGeneracion = costeNuevaGeneracionSegunda;

            //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probMutacion
            mutar(tampoblacion,tam,probabilidadMutacion, rmin,rmax, nuevaGeneracion, marcados);

            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion
            for (int i = 0; i < tampoblacion; i++) {
                if (marcados[i]) {  ////todo cromosoma modificado le hacemos el calculo del coste sobre el nuevo cromosoma y calculo el nuevo coste
                    costeNuevaGeneracion[i] = evaluaCoste(nuevaGeneracion.get(i), String.valueOf(funcion));
                    contador++;
                }
                if (costeNuevaGeneracion[i] < mejorCosteHijo) { //de la nueva poblacion nos quedamos con el mejor hijo que ssera el mejor padre para la sioguiente generacion
                    mejorCosteHijo = costeNuevaGeneracion[i];
                    mejorCromosomaHijo = i;
                }

            }
            //ELITILISMO
            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
            boolean enc = false;
            for (int i = 0; i < nuevaGeneracion.size() && !enc; i++) { //pasamos por la nueva generacion
                if (mejorCromosoma == nuevaGeneracion.get(i)){ //mirando si el mejorcruce(ahi esta guardado el primer cromosoma de la poblacion inicial) aqui guardamos los nuevos cromosomas de esta poblacion el mejor padre
                    enc = true; //lo buscamos en la poblacion de hijos, lo encontramos no hacemos modificacion, el mejor padre tiene que seguir perviviendo en la siguiente generacion si no lo encuentro es cvuando sustituyo en la posicion del peor
                }
            }
            if (!enc) {
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

                if (mejorCoste < mejorCosteHijo) { //calculamos mejor cromosoma para la siguiete vuelta
                    mejorCosteHijo = mejorCoste;
                    nuevaGeneracion.add(mejorCromosomaHijo, mejorCromosoma);
                }
            }
            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCromosoma = nuevaGeneracion.get(mejorCromosomaHijo);
            mejorCoste = mejorCosteHijo;

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
            //si mejora me quedo con el mejor coste y con el mejorCrhijo
            if (mejorCosteHijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteHijo;
                mejorCromosomaGlobal = nuevaGeneracion.get(mejorCromosomaHijo);
            }
            System.out.println("Mejor coste;" + mejorCosteGlobal);
            //Actualizo cromosomas con nuevag, para la siguiente generacion
            costes = costeNuevaGeneracion;
            cromosomas = nuevaGeneracion;
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