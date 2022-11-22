package meta.algoritmo;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.*;

public class AED_Clase3_Grupo5 {
    public static void AED(int tampoblacion, int tam, int evaluaciones, double[] solucion, double rmin, double rmax, String funcion, Long semilla, Logger logger) {
        long tiempoInicial = System.nanoTime();
        Random aleatorio = new Random();
        int t = 0;
        List<double[]> cromosomas = new ArrayList<>(tampoblacion);
        double[] mejorCromosoma=new double[tampoblacion];
        double mejorCoste =Double.MAX_VALUE;
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCromosomaGlobal = mejorCromosoma;
        int contador = tampoblacion;
        double[] costes = new double[tampoblacion];
        double[] ale1 = new double[tampoblacion], ale2 = new double[tampoblacion], obj, nuevo = new double[tampoblacion], padre;
        int k1=0,k2=0,k3=0,k4=0,a1=0;
        double valor=0.5;

       cromosomas = cargaCromosomasIniciales(tampoblacion,tam, rmin, rmax,semilla);

        //Comienzan las iteraciones
        while (contador < evaluaciones) {

            //CRUZAMOS con operador ternario
            for(int i=0;i<tampoblacion;i++){
                padre = cromosomas.get(i);

                //eleccion de 2 aleatorios distintos entre si y del padre
                eleccion2Aleatorios(tampoblacion,cromosomas,costes,i,ale1,ale2);

                //aplicamos torneo sobre inviduo objetivo elegido entre k=3(distintos) y distintos a a1, a2 y el padre
                torneoK3(tampoblacion,i,a1,k1,k2,k3,k4);

                //ELEGIMOS EL  MEJOR
                if (costes[k1] < costes[k2] && costes[k1] < costes[k3])
                    obj = cromosomas.get(k1);
                else if (costes[k2] < costes[k1] && costes[k2] < costes[k3])
                    obj = cromosomas.get(k2);
                else
                    obj = cromosomas.get(k3);

                double Factor = aleatorio.nextDouble(); //Factor de mutacion diferente por cada elemento de la poblacion TIENE QUE SER UN 1 POR CIENTE

                for (int j = 0; j < tam; j++) { //UN FOR PARA DIMENSION
                    double d = aleatorio.nextDouble(); //% de elección de dimensiones entre el nuevo y el objetivo. Por cada dimension(posicion) del individuo

                    if (d > valor)
                        nuevo[j] = obj[j];
                    else {
                        nuevo[j] = operadorRecombinacion(padre,j,ale1,ale2,Factor); //operador de recombinacion a posteriori de la mutacion. Para cuando se salen los valores del rango de la funcion

                        if (nuevo[j] > rmax)
                            nuevo[j] = rmax;
                        else if (nuevo[j] < rmin)
                            nuevo[j] = rmin;
                    }
                }

                //REEMPLAZAMIENTO si el hijo es mejor q el padre
                double costeNuevo = evaluaCoste(nuevo, funcion);
                contador++;
                reemplazamiento(costeNuevo,i,costes,cromosomas,nuevo,mejorCoste,mejorCromosoma);

            }//llave for i

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION. Si mejora
            if (mejorCoste < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCoste;
                mejorCromosomaGlobal = mejorCromosoma;
            }

            t++;
            mejorCoste=Double.MAX_VALUE;
        }

        solucion = mejorCromosomaGlobal;
        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial);

        System.out.println("Total evaluaciones" + contador);
        System.out.println("Total Iteraciones" + t);
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