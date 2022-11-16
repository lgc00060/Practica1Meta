package meta.algoritmo;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.*;

public class AEVMedia_CLase3_Grupo5 {
    public static void AEVMedia(int tampoblacion, int tam, int evaluaciones, double[] s, double rmin, double rmax,
                                double kProbMuta, double kProbCruce, double alfa, String funcion, Long semilla, Logger logger) {
        int t = 0;
        long tiempoInicial = System.nanoTime();
        List<double[]> cromosomas = new ArrayList<>();
        List<double[]> nuevageneracion = new ArrayList<>(tam);
        double[] costesHijo = new double[tampoblacion];
        int[] position = new int[tampoblacion];
        double[] mejorCruce = new double[tampoblacion];
        int peor = 0;
        double peorCosteHijo = 0;
        int mejorCruceHijo = 0;
        double mejorCoste = Double.MAX_VALUE;
        Random random = new Random();
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = mejorCruce;
        double probMutacion = kProbMuta; //Calculo de la probabilidad de mutacion
        int conta = tampoblacion;  //Contador de evaluaciones de la poblacion
        int c1, c2, c3, c4;
        int posAnt=0;
        double[] h = new double[tam];
        double[] costes = new double[tampoblacion], costesH = new double[tampoblacion], costesHH = new double[tampoblacion];
        boolean[] marcados=new boolean[tampoblacion];  //marcamos los modificados
        double[] mejor1, mejor2;
        double costeMejor1, costeMejor2;
        double mejorcostehijo = Double.MAX_VALUE;

        logger.info("Empieza ejecucion EvolutivoMedia: ");


        //Carga de los cromosomas iniciales
        cargaCromosomasIniciales(tampoblacion,cromosomas,rmin,rmax,funcion,costes,tam,mejorCoste,mejorCruce);


        //PRINCIPAL: Comienzan las iteraciones
        while (conta < evaluaciones) {
            t++;
            //SELECCION por TORNEO: Calculo de los cromosomas mas prometedores entre cada 2 parejas aleatorias
            //durante tp enfrentamiento¡'
            for (int k = 0; k < tampoblacion; k++) {
                int i, j;
                i = random.nextInt(tampoblacion - 1 - 0) + 0;
                while (i == (j = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
                position[k] = (costes[i] < costes[j]) ? i : j;
            }
            //Nos quedamos con los cromosomas mas prometedores
            for (int i=0;i<tampoblacion;i++){
                if (position[i]==50)
                    position[i]--;
                nuevageneracion.add(i, cromosomas.get(position[i]));
                costesHijo[i] = costes[position[i]];
            }
            //CRUZAMOS los padres seleccionados con una probabilidad probCruce
            for (int i = 0; i < tampoblacion; i++) {
                marcados[i] = false;
            }
            for (int i = 0; i < tampoblacion; i++) {
                int  h1 = random.nextInt((tampoblacion - 1 - 0) + 0);
                while (h1 == (c2 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
                if (costesHijo[h1] < costesHijo[c2]) {
                    mejor1 = nuevageneracion.get(h1);
                    costeMejor1 = costesHijo[h1];
                } else {
                    mejor1 = nuevageneracion.get(c2);
                    costeMejor1 = costesHijo[c2];
                }
                while (posAnt == (c3 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;
                while (posAnt == (c4 = random.nextInt(tampoblacion - 1 - 0) + 0)) ;


                if (costesHijo[c3] < costesHijo[c4]) {
                    mejor2 = nuevageneracion.get(c3);
                    costeMejor2 = costesHijo[c3];
                } else {
                    mejor2 = nuevageneracion.get(c4);
                    costeMejor2 = costesHijo[c4];
                }
                double x = random.nextDouble();
                if (x < kProbCruce ) { //si se cumple la probabilidad de cruce  cojo otro aleatorio
                    //cruceMedia(tam, mejor1, mejor2, h); y hacemos el cruce entre los dos
                    nuevageneracion.add(i, h); //algunos cambiaran y otros no
                    marcados[i] = true; //sobre las posiciones de los padres elegidos
                } else {
                    nuevageneracion.add(i, mejor1);
                    costesHH[i] = costeMejor1;
                }
            }
            nuevageneracion = nuevageneracion; //cojo la nuevagg y la copio en nuevag
            costesHijo = costesHH;

            //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probMutacion
            for (int i = 0; i < tampoblacion; i++) {
                boolean m = false;
                for (int j = 0; j < tam; j++) {
                    double x = random.nextDouble();
                    if (x < kProbMuta) {
                        m = true;
                        double valor = random.nextDouble() + rmin;
                        //Mutacion(nuevaAg.get(i),j,valor); //en la posicion j del vector quitamos el valor que tiene y  metemos el valor nuevo
                    }
                }
                if (m)
                    marcados[i] = true; //marcamos los cromosomas modificados
            }

            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion
            for (int i = 0; i < tampoblacion; i++) {
                if (marcados[i]) {  //todo cromosoma modificado le hacemos el calculo del coste sobre el nuevo cromosoma y calculo el nuevo coste
                    costesHijo[i] = evaluaCoste(nuevageneracion.get(i), String.valueOf(funcion));
                    conta++;
                }
                if (costesHijo[i] < mejorcostehijo) { //de la nueva poblacion nos quedamos con el mejor hijo que ssera el mejor padre para la sioguiente generacion
                    mejorcostehijo = costesHijo[i];
                    mejorCruceHijo = i;
                }

            }
            //ELITILISMO
            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
            boolean enc = false;
            for (int i = 0; i < nuevageneracion.size() && !enc; i++) { //pasamos por la nueva generacion
                if (mejorCruce == nuevageneracion.get(i)){ //mirando si el mejorcruce(ahi esta guardado el primer cromosoma de la poblacion inicial) aqui guardamos los nuevos cromosomas de esta poblacion el mejor padre
                    enc = true; //lo buscamos en la poblacion de hijos, lo encontramos no hacemos modificacion, el mejor padre tiene que seguir perviviendo en la siguiente generacion si no lo encuentro es cvuando sustituyo en la posicion del peor
                }
            }
            if (!enc) {
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
                nuevageneracion.add(peor, mejorCruce);
                costesH[peor] = mejorCoste;

                if (mejorCoste < mejorcostehijo) { //calculamos mejor cromosoma para la siguiete vuelta
                    mejorcostehijo = mejorCoste;
                    nuevageneracion.add(mejorCruceHijo, mejorCruce);
                }
            }
            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCruce = nuevageneracion.get(mejorCruceHijo);
            mejorCoste = mejorcostehijo;

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
            //si mejora me quedo con el mejor coste y con el mejorCrhijo
            if (mejorcostehijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorcostehijo;
                mejorCroGlobal = nuevageneracion.get(mejorCruceHijo);
            }
            System.out.println("Mejor coste;" + mejorCosteGlobal);
            //Actualizo cromosomas con nuevag, para la siguiente generacion
            costes = costesHijo;
            cromosomas = nuevageneracion;
        }
        s = mejorCroGlobal;

        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial);
        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("Funcion:" + funcion);
        logger.info("RangoInf: " + rmin);
        logger.info("RangoSup: " + rmax);
        logger.info("El coste del algoritmo EvolutivoMedia es:" + mejorCosteGlobal);
        logger.info("La semilla es:" + semilla);
        System.out.println("Total Evaluaciones:" + conta);
        System.out.println(" Total Iteraciones:" + t);
    }

}