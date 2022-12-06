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
        double[] aleatorio1 = new double[tampoblacion], aleatorio2 = new double[tampoblacion], obj, nuevo = new double[tam], padre;
        int k1=0,k2=0,k3=0;
        double valor=0.5;
        int a1=0,a2=0;

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

        logger.info("Poblacion inicial: ");
        for(int i=0;i<tampoblacion;i++){
            for(int j=0;j<tampoblacion;j++){
                logger.info("Cromosoma de la poblacion inicial: "+ cromosomas.get(i)[j]);
            }
        }

        logger.info("Mejor cromosoma de la poblacion inicial es: " + formatoVector(mejorCromosoma));

        //Comenzaran las iteraciones
        while (contador < evaluaciones) {
            for(int i=0;i<tampoblacion;i++){

                padre = cromosomas.get(i);

                aleatorios(tampoblacion,cromosomas,a1,a2,i,aleatorio1,aleatorio2);

                torneoK3(tampoblacion,i,a1,k1,k2,k3,a2);

                //ELEGIMOS EL  MEJOR
                if (costes[k1] < costes[k2] && costes[k1] < costes[k3])
                    obj = cromosomas.get(k1);
                else if (costes[k2] < costes[k1] && costes[k2] < costes[k3])
                    obj = cromosomas.get(k2);
                else
                    obj = cromosomas.get(k3);

                //elegimos el nuevo cromosoma
                double factor = aleatorio.nextDouble(); //factor mutacion
                for (int j = 0; j < tam; j++) {
                    double d = aleatorio.nextDouble();
                    if (d > valor)
                        nuevo[j] = obj[j];
                    else {
                        nuevo[j] = operadorRecombinacion(padre,j,aleatorio1,aleatorio2,factor);
                        //por si se saliera del rango
                        if (nuevo[j] > rmax)
                            nuevo[j] = rmax;
                        else if (nuevo[j] < rmin)
                            nuevo[j] = rmin;
                    }
                }

                //reemplazamos SII hijo mejor que padre
                double nuevoCoste = evaluaCoste(nuevo, funcion);
                contador++;
                reemplazamiento(nuevoCoste, i, costes, cromosomas, nuevo, mejorCoste, mejorCromosoma);

            }//llave for i

            //actualizamos
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
        logger.info("Mejor cromosoma de la nueva poblacion: "+formatoVector(solucion));
    }
}