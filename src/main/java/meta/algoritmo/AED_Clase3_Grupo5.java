package meta.algoritmo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.cargaCromosomasIniciales;

public class AED_Clase3_Grupo5 {
    public static void AED(int tampoblacion, int tam, int evaluaciones, double[] s, double rmin, double rmax, String funcion,Long semilla,Logger logger){
        long tiempoInicial = System.nanoTime();
        int t=0;
        List<double[]> cromosomas = new ArrayList<>();
        double[] costes = new double[tampoblacion];
        double mejorCoste = Double.MAX_VALUE;
        double[] mejorCruce = new double[tampoblacion];
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCruceGlobal=mejorCruce;
        Random random = new Random();
        //Contador de evaluaciones de la poblacion
        int contador=tampoblacion;


        cargaCromosomasIniciales(tampoblacion,cromosomas,rmin,rmax,funcion,costes,tam,mejorCoste,mejorCruce);

        //mostrarVector(mejorCroGlobal);
        System.out.println("Coste" + mejorCosteGlobal);

        //Comienzan las iteraciones
        while (contador<evaluaciones){
            t++;

            //CRUZAMOS con operador de recombinacion ternaria
            double[] ale1, ale2, obj, nuevo = new double[tampoblacion], padre;
            int a1, a2, k1, k2, k3;

            for (int i = 0; i < tampoblacion; i++) {
                padre = cromosomas.get(i);
                do {
                    a1 = random.nextInt(tampoblacion - 1 - 0);
                    while (a1 == (a2 = random.nextInt(tampoblacion - 1 - 0))) ;
                } while (a1 != i && a2 != i);
                if (a1 >= tampoblacion)
                    a1 = tampoblacion - 1;
                ale1 = cromosomas.get(a1);
                ale2 = cromosomas.get(a2);

                //un objetivo elegido entre k=3(distintos) y distintos a a1, a2 y el padre
                do {
                    k1 = random.nextInt(tampoblacion - 1 - 0);
                    while (k1 == (k2 = random.nextInt(tampoblacion - 1 - 0))) ;
                    //while (k1 == (k2 == (k3 = random.nextInt(tp - 1 - 0)))) ;
                    k3 = random.nextInt(tampoblacion - 1 - 0);
                } while (k1 != i && k1 != a1 && k1 != a2 &&
                        k2 != i && k2 != a1 && k2 != a2 &&
                        k3 != i && k3 != a1 && k3 != a2);
                if (costes[k1] < costes[k2] && costes[k1] < costes[k3])
                    obj = cromosomas.get(k1);
                else if (costes[k2] < costes[k1] && costes[k2] < costes[k3])
                    obj = cromosomas.get(k2);
                else
                    obj = cromosomas.get(k3);


                double F = random.nextDouble();  //Factor de mutacion diferente por cada elemento de la poblacion

                for (int j=0; j<tam; j++){
                    double d=random.nextDouble(); //% de elección de dimensiones entre el nuevo y el objetivo
                    // por cada dimension(posicion) del individuo
                    if (d>0.5)
                        nuevo[j]=obj[j];
                    else{
                        nuevo[j]= padre[j]+ (F*(ale1[j]-ale2[j])); //operador de recombinacion a posteriori de la mutacion
                        //para cuando se salen los valores del rango de la funcion
                        if (nuevo[j]>rmax)
                            nuevo[j]=rmax;
                        else
                        if (nuevo[j]<rmin)
                            nuevo[j]=rmin;
                    }
                }

                //REEMPLAZAMIENTO si el hijo es mejor q el padre
                double costeNuevo=evaluaCoste(nuevo,funcion);
                contador++;
                if (costeNuevo<costes[i]){
                    cromosomas.add(i,nuevo);
                    costes[i]=costeNuevo;
                    if (costeNuevo<mejorCoste){
                        mejorCoste=costeNuevo;
                        mejorCruce=nuevo;
                    }
                }
            }

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
            //si mejora
            if (mejorCoste<mejorCosteGlobal){
                mejorCosteGlobal=mejorCoste;
                mejorCruceGlobal=mejorCruce;
            }
        }

        s=mejorCruceGlobal;
        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial);

        System.out.println("Total evaluaciones" + contador);
        System.out.println("Total Iteraciones" +t);
        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("Funcion:" + funcion);
        logger.info("RangoInf: " + rmin);
        logger.info("RangoSup: " + rmax);
        logger.info("El coste del algoritmo Evolutivo es:" + mejorCosteGlobal);
        logger.info("La semilla es:" + semilla);
        System.out.println("Total Evaluaciones:" + contador);
        System.out.println(" Total Iteraciones:" + t);
        //return mejorCosteGlobal;

    }


}
