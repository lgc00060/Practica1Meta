package meta.algoritmo;

import meta.funciones.Funciones;
import org.apache.log4j.Logger;

import java.util.Random;

import static meta.funciones.Funciones.formatoVector;
import static meta.utils.MyRandom.randDoubleWithRange;

public class AlgBL3_Clase3_Grupo5 {
    public static void algBL3Clase1Grupo5(long semilla, int tam, long evalua, double[] soluActu, double rmin, double rmax, String funcion, double oscilacion,Logger logger) {
        double tiempoInicial = System.nanoTime();
        logger.info("Empieza ejecucion algoritmo BL3: ");
        Random aleatorio = new Random();

        for (int i = 0; i < tam; i++) {
            soluActu[i] = randDoubleWithRange(rmin,rmax);
        }

        double[] vecino = new double[tam];
        double[] mejorVecino =soluActu;
        double mejorCosteVecino=0;
        double costvecino = 0;
        int iter = 0;
        Boolean mejor = true;

        double mejorCoste = Funciones.evaluaCoste(soluActu, funcion);

        while (mejor && iter < evalua) {
            mejor = false;
            mejorCosteVecino = Double.MAX_VALUE;
            for (int j = 1; j <= 3; j++) { //aqui calculamos el coste del mejor vecino. Cada nuevo vecindario que se valua el valor va cambiando a cada vuelta que da
                for (int z = 0; z < tam; z++) {
                    double nVecinos = aleatorio.nextDouble();
                    if (nVecinos <= oscilacion) {
                        double inf = soluActu[z] * 0.9;
                        double sup = soluActu[z] * 1.1;
                        if (inf < rmin)
                            inf = rmin;
                        if (sup > rmin)
                            sup = rmax;
                        vecino[z] = randDoubleWithRange(inf,sup);
                    } else
                        vecino[z] = soluActu[z];
                }

                costvecino = Funciones.evaluaCoste(vecino, funcion);
                if (costvecino < mejorCosteVecino) {
                    mejorVecino = vecino;
                    mejorCosteVecino = costvecino;
                }

            }

            if (mejorCosteVecino < mejorCoste) {
                soluActu = mejorVecino;
                mejorCoste = mejorCosteVecino;
                mejor = true;
                iter++;
            }

        }

        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial)/1000000;
        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("Funcion:" + funcion);
        logger.info("RangoInf: " + rmin);
        logger.info("RangoSup: " + rmax);
        logger.info("El coste del algoritmo BL3 es:" + mejorCoste);
        logger.info("El numero de iteraciones recorridas es:" + iter);
        logger.info("El vector seria: "+ formatoVector(soluActu));
        logger.info("La semilla es:" + semilla);

    }
}

