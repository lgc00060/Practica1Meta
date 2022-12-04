package meta.algoritmo;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.*;

public class AED_Clase3_Grupo5 {
    public static void AED(int tampoblacion, int tam, int evaluaciones, double[] solucion, double rmin, double rmax, String funcion, long semilla, Logger logger) {
        long tiempoInicial = System.nanoTime();
        Random aleatorio = new Random();
        int t = 0;
        List<double[]> cromosomas = new ArrayList<>();
        double[] mejorCromosoma=new double[tampoblacion];
        double mejorCoste =Double.MAX_VALUE;
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCromosomaGlobal = mejorCromosoma;
        int contador = tampoblacion;
        double[] costes = new double[tampoblacion];
        double[] ale1, ale2, obj, nuevo = new double[tam], padre;
        int k1,k2,k3;
        double valor=0.5;
        int a1,a2;

        logger.info("Empieza ejecucion algoritmo Diferencial: ");

        //cargamos los cromosomas iniciales con aleatorios
        double[] v= new double[tampoblacion];
        for (int j = 0; j < tampoblacion; j++) {
            v[j] = randDoubleWithRange(rmin, rmax);
            cromosomas.add(v);
        }


        for (int i = 0; i < tampoblacion; i++) {
            costes[i] = evaluaCoste(cromosomas.get(i), funcion);
            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCromosoma = cromosomas.get(i);
            }
        }


        //Comienzan las iteraciones
        while (contador < evaluaciones) {
            //CRUZAMOS con operador ternario
            for(int i=0;i<tampoblacion;i++){

                padre = cromosomas.get(i);


                do {
                    a1 = aleatorio.nextInt(tampoblacion);
                    while (a1 == (a2 = aleatorio.nextInt(tampoblacion))) ;
                } while (a1 != i && a2 != i);
                ale1 = cromosomas.get(a1);
                ale2 = cromosomas.get(a2);

                //
                // torneoK3(tampoblacion,i,a1,k1,k2,k3,k4);

                do {
                    k1 = aleatorio.nextInt(tampoblacion);
                    while (k1 == (k2 = aleatorio.nextInt(tampoblacion))) ;
                    while ((k2 == (k3 = aleatorio.nextInt(tampoblacion)))) ;
                } while (k1 != i &&  k1 != a1 && k1 != a2 &&
                         k2 != i &&  k2 != a1 && k2 != a2 &&
                         k3 != i &&  k3 != a1 && k3 != a2);

                //ELEGIMOS EL  MEJOR

                if (costes[k1] < costes[k2] && costes[k1] < costes[k3])
                    obj = cromosomas.get(k1);
                else if (costes[k2] < costes[k1] && costes[k2] < costes[k3])
                    obj = cromosomas.get(k2);
                else
                    obj = cromosomas.get(k3);

                double factor = aleatorio.nextDouble();//

                for (int j = 0; j < tam; j++) {
                    double d = aleatorio.nextDouble();//
                    if (d > valor)
                        nuevo[j] = obj[j];
                    else {
                        nuevo[j] = operadorRecombinacion(padre,j,ale1,ale2,factor);
                        if (nuevo[j] > rmax)
                            nuevo[j] = rmax;
                        else if (nuevo[j] < rmin)
                            nuevo[j] = rmin;
                    }
                }

                //reemplazamos si hijo mejor que padre
                double nuevoCoste = evaluaCoste(nuevo, funcion);
                contador++;
                reemplazamiento(nuevoCoste, i, costes, cromosomas, nuevo, mejorCoste, mejorCromosoma);

            }//llave for i

            //
            if (mejorCoste < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCoste;
                mejorCromosomaGlobal = mejorCromosoma;
            }

            t++;
            mejorCoste = Double.MAX_VALUE;
        }

        solucion = mejorCromosomaGlobal;
        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial)/1000000;


        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("Funcion:" + funcion);
        logger.info("RangoInf: " + rmin);
        logger.info("RangoSup: " + rmax);
        logger.info("El coste del algoritmo Diferencial es:" + mejorCosteGlobal);
        logger.info("La semilla es:" + semilla);
        logger.info("Total Evaluaciones:" + contador);
        logger.info(" Total Iteraciones:" + t);

    }
}