package meta.algoritmo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import meta.Main;
import org.apache.log4j.Logger;
import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.Mutacion;
import static meta.utils.FuncionesAux.cargaAleatoria;

public class AEvBLXalfa_Clase3_Grupo5 {
    public AEvBLXalfa_Clase3_Grupo5(int poblacion, int d, int evaluar, double[] solu, double v, double v1, double prob_muta, double cruce, double alfa, String funcion, Long semilla, Logger logger) {
    }

    public static double AEVBLXALFA(int tp, int tam, double evaluaciones, double[] s, double rmin, double rmax,
                                    double kProbMuta, double kProbCruce, double alfa, String funcion, Long semilla) {

        long tiempoInicial = System.nanoTime();
        int t = 0;
        List<double[]> cromosomas=new ArrayList<>();
        List<double[]> nuevag=new ArrayList<>(tam);
        double[] costes= new double[tp];
        double[] costesHijo= new double[tp];
        int[] position=new int[tp];
        double[] mejorCruce=new double[tp];
        int peor=0;
        double peorCosteHijo=0;
        int mejorCruceHijo = 0;
        double mejorCoste =Double.MAX_VALUE;
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = mejorCruce;
        //Calculo de la probabilidad de mutacion
        double  probMutacion= kProbMuta;
        //Contador de evaluaciones de la poblacion
        int conta = tp;
        Random random = new Random();
        int p1 = 0;
        double[] h1 = new double[tam];
        double[] h2 = new double[tam];
        boolean[] marcados=new boolean[tp];  //marcamos los modificados
        double mejorcostehijo = Integer.MAX_VALUE;
        Logger logger = Logger.getLogger(Main.class);

        logger.info("Empieza ejecucion EvolutivoBLXAlfa: ");

        //Carga de los cromosomas iniciales
        for (int i = 0; i < tp; i++) {
            cargaAleatoria(tam, cromosomas.get(i), rmin, rmax);
            costes[i] = evaluaCoste(cromosomas.get(i), String.valueOf(funcion));

            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCruce = cromosomas.get(i);
            }
        }
        //PRINCIPAL: Comienzan las iteraciones

        while (conta < evaluaciones) {
            t++;
            //SELECCION por TORNEO: Calculo de los cromosomas mas prometedores entre cada 2 parejas aleatorias
            //durante tp enfrentamiento¡'
            for (int k = 0; k < tp; k++) {
                int i,j;
                i =  random.nextInt(tp - 1);
                while (i == (j = random.nextInt(tp - 1)));
                 position[k] = (costes[i] < costes[j]) ? i : j;
            }

            //Nos quedamos con los cromosomas mas prometedores
            for (int i = 0; i < tp; i++) {
                nuevag.add(i, cromosomas.get(position[i]));
                costesHijo[i] = costes[position[i]];
            }
            //CRUZAMOS los padres seleccionados con una probabilidad probCruce OPCION 1
            for (int i = 0; i < tp; i++) {
                double x = random.nextDouble();
                if (x < kProbCruce) {
                    while (i == (p1 = random.nextInt(tp - 1))) ;
                    //cruceBLX(tam, nuevag.get(i), nuevag.get(p1), alfa, h1, h2);
                    nuevag.add(i, h1);
                    nuevag.add(p1, h2);
                    marcados[i] = marcados[p1] = true;

                }
            }

            //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probMutacion
            for (int i = 0; i < tp; i++) {
                boolean m = false;
                for (int j = 0; j < tam; j++) {
                   double x = random.nextDouble();
                    if (x < probMutacion) {
                        m = true;
                        double valor = random.nextDouble() + rmin;
                        Mutacion(nuevag.get(i), j, valor);//cout << "mutando Cromosoma.." << endl;
                    }
                }
                if (m)
                    marcados[i] = true;        //marcamos los modificados
            }

            //actualizamos el coste de los modificados
            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion

            for (int i = 0; i < tp; i++) {
                if (marcados[i]) {
                    costesHijo[i] = evaluaCoste(nuevag.get(i), String.valueOf(funcion));
                    conta++;
                }

                if (costesHijo[i] > peorCosteHijo) {
                    peorCosteHijo = costesHijo[i];
                    peor = i;
                }

                if (costesHijo[i] < mejorcostehijo) {
                    mejorcostehijo = costesHijo[i];
                    mejorCruceHijo = i;
                }

            }

            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
            boolean enc = false;
            for (int i = 0; i < nuevag.size() && !enc; i++) {
                if (mejorCruce == nuevag.get(i)){
                    enc = true;
                }
            }
            mejorCruce = nuevag.get(mejorCruceHijo);
            costesHijo[peor] = mejorCoste;

            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCruce = nuevag.get(mejorCruceHijo);
            mejorCoste = mejorcostehijo;

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
            //si mejora
            if (mejorcostehijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorcostehijo;
                mejorCroGlobal = nuevag.get(mejorCruceHijo);
            }
            System.out.println("Mejor coste;" + mejorCosteGlobal);
            //Actualizo cromosomas con nuevag, para la siguiente generacion
            costes = costesHijo;
            cromosomas = nuevag;
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
        System.out.println("Total Evaluaciones:" + conta);
        System.out.println(" Total Iteraciones:" + t);
        return mejorCosteGlobal;
    }

}