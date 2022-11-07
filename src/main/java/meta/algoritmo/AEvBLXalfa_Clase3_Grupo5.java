package meta.algoritmo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.BasicConfigurator;
import static meta.funciones.Funciones.evaluaCoste;
import static meta.funciones.Funciones.formatoVector;
import static meta.utils.FuncionesAux.Mutacion;
import static meta.utils.FuncionesAux.cargaAleatoria;
import static meta.Main.*;


public class AEvBLXalfa_Clase3_Grupo5 {
    public static double AEVBLXALFA(int tp, int tam, int evaluaciones, double[] s, double rmin, double rmax,
                                    double kProbMuta, double kProbCruce, double alfa, int funcion, int semilla,int logger) {
        int t = 0;
        long tiempoInicial = System.nanoTime();
        List<double[]> cromosomas=new ArrayList<>();
        List<double[]> nuevag=new ArrayList<>(tam);
        double[] costes= new double[tp];
        double[] costesHijo= new double[tp];
        int[] position=new int[tp];
        double[] mejorCr=new double[tp];
        int peor=0;
        double peorCosteHijo=0;
        int mejorCruceHijo = 0;
        double mejorCoste =Double.MAX_VALUE;
        double mejorCosteHijo= Double.MAX_VALUE;
        Random random = new Random();

        //Carga de los cromosomas iniciales
        for (int i = 0; i < tp; i++) {
            cargaAleatoria(tam, cromosomas.get(i), rmin, rmax);
            costes[i] = evaluaCoste(cromosomas.get(i), String.valueOf(funcion));

            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCr = cromosomas.get(i);
            }
        }

        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = mejorCr;

        //Calculo de la probabilidad de mutacion
        double  probMutacion= kProbMuta;

        //Contador de evaluaciones de la poblacion
        int conta = tp;

        //PRINCIPAL: Comienzan las iteraciones

        while (conta < evaluaciones) {
            t++;
            //SELECCION por TORNEO: Calculo de los cromosomas mas prometedores entre cada 2 parejas aleatorias
            //durante tp enfrentamiento¡'
            for (int k = 0; k < tp; k++) {
                int i,j;
                i =  random.nextInt(tp - 1 - 0)+0;
                while (i == (j = random.nextInt(tp - 1 - 0) + 0));
                 position[k] = (costes[i] < costes[j]) ? i : j;
            }

            //Nos quedamos con los cromosomas mas prometedores
            for (int i = 0; i < tp; i++) {
                nuevag.add(i, cromosomas.get(position[i]));
                costesHijo[i] = costes[position[i]];
            }
            //CRUZAMOS los padres seleccionados con una probabilidad probCruce OPCION 1
            int p1 = 0;
            double[] h1 = new double[tam];
            double[] h2 = new double[tam];
            boolean[] marcados=new boolean[tp];  //marcamos los modificados

            for (int i = 0; i < tp; i++) {
                double x = random.nextDouble();
                if (x < kProbCruce) {
                    while (i == (p1 = random.nextInt(tp - 1 -0)+ 0)) ;
                    //cruceBLX(tam, nuevag.get(i), nuevag.get(p1), alfa, h1, h2);
                    nuevag.add(i, h1);
                    nuevag.add(p1, h2);
                    marcados[i] = marcados[p1] = true;

                }
            }

            //CRUZAMOS los padres seleccionados con una probabilidad probCruce OPCION 2

            int c1;
            int c2;
            List<double[]>nuevagg=nuevag;

            for (int i = 0; i < tp; i++) {
                c1 = random.nextInt(tp - 1 -0)+ 0;
               double x = random.nextDouble();
                if (x < kProbCruce) {
                    while (c1 == (c2 = random.nextInt(tp - 1 -0)+ 0)) ;
                    //cruceBLX(tam, nuevag.get(i), nuevag.get(p1), alfa, h1, h2);
                    nuevagg.add(c1, h1);
                    nuevagg.add(c2, h2);
                    marcados[c1] = marcados[c2] = true;

                }
            }
            nuevagg = nuevag; //la dejamos en nuevag para proseguir

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
            double mejorcostehijo = Integer.MAX_VALUE;
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
                if (mejorCr == nuevag.get(i)){
                    enc = true;
                }
            }
            mejorCr = nuevag.get(mejorCruceHijo);
            costesHijo[peor] = mejorCoste;

            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCr = nuevag.get(mejorCruceHijo);
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
        /*
        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("Funcion:" + funcion);
        logger.info("RangoInf: " + rmin);
        logger.info("RangoSup: " + rmax);
        logger.info("El coste del algoritmo Evolutivo es:" + mejorCoste);
        //logger.info("El numero de iteraciones recorridas es:" + iter);
        //logger.info("El vector seria: "+ formatoVector(soluActu));
        logger.info("La semilla es:" + semilla);

         */
        System.out.println("Total Evaluaciones:" + conta);
        System.out.println(" Total Iteraciones:" + t);
        return mejorCosteGlobal;
    }

}