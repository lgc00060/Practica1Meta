package meta.algoritmo;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static meta.funciones.Funciones.*;
import static meta.utils.MyRandom.randDoubleWithRange;

public class AlgBTabuVNS_Clase3_Grupo5 {
    public static void diversificacion(long[][] matriz, double[] solNu, double rmin, double rmax) {
        Random aleatorio = new Random();
        int tam = solNu.length;
        int poscol = 0;
        int columnas[] = new int[3];
        double menor,inicio,fin,ancho;

        for (int i = 0; i < tam; i++) {
            for (int k = 0; k < 3; k++) {
                menor = Integer.MAX_VALUE;
                for (int j = 0; j < 10; j++) {
                    if (matriz[i][j] <= menor) {
                        menor = matriz[i][j];
                        poscol = j;
                    }
                }
                columnas[k] = poscol;
                matriz[i][poscol] = Integer.MAX_VALUE;
            }

            int random = aleatorio.nextInt(2);
            int col = columnas[random];
            ancho = (rmax - rmin + 1) / 10;

            inicio = rmin + (col * ancho);
            fin = inicio + ancho;
            solNu[i] = randDoubleWithRange(inicio,fin);
        }
    }

    public static void intensificacion(long[][] matriz, double[] solNu, double rmin, double rmax) {
        Random aleatorio = new Random();
        int tam = solNu.length;
        int poscol = 0;
        int columnas[] = new int[3];
        double mayor,inicio,fin,ancho;

        for (int i = 0; i < tam; i++) {
            for (int k = 0; k < 3; k++) {
                mayor = Integer.MIN_VALUE;
                for (int j = 0; j < 10; j++) {
                    if (matriz[i][j] >= mayor) {
                        mayor = matriz[i][j];
                        poscol = j;
                    }
                }
                columnas[k] = poscol;
                matriz[i][poscol] = Integer.MIN_VALUE;
            }

            int random = aleatorio.nextInt(2);
            int col = columnas[random];
            ancho = (rmax - rmin + 1) / 10;

            inicio = rmin + (col * ancho);
            fin = inicio + ancho;
            solNu[i] = randDoubleWithRange(inicio,fin);
        }
    }

    public static void algBTabuVNSClase1Grupo5(long semilla, long iteraciones, double[] soluActu, int tam, double rmin, double rmax, String funcion, double oscilacion, Logger logger) {
        double tiempoInicial = System.nanoTime();
        double inf, sup;
        int contador = 0;
        Random aleatorio = new Random();
        List<double[]> lTabu = new ArrayList<>();
        List<List<Integer>> lTabuMovimientos = new ArrayList<>();
        List<Integer> cambiosDeVecino = new ArrayList<>();
        List<Integer> cambiosDeMejorVecino = new ArrayList<>();
        double[] vVecino = new double[10];
        double mejorVecino[] = new double[10];
        double mejorPeores[] = new double[10];
        double valoresAnt[] = new double[10];
        int iter = 0;
        boolean mejor,esTabu;
        double mejorCosteVecino = Double.MAX_VALUE;
        int contNoTabu;
        double costeMejorMomento = Double.MAX_VALUE;
        double costeActual = evaluaCoste(soluActu, funcion);
        double CGlobal = costeActual;
        int osc = 0;
        int tenenciaTabu = 0;
        long memFrec[][] = new long[10][10];
        double CosteMejorPeor;

        int ma=1;

        double[] solucGlobal=soluActu;

        double[] solNu = new double[10];

        logger.info("Empieza ejecucion MULTIARRANQUE: ");

        for (int i = 0; i < tam; i++) {
            soluActu[i] = randDoubleWithRange(rmin,rmax);
        }

        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                memFrec[i][j] = 0;

        lTabu.add(soluActu);

        while (iter < iteraciones) {
            iter++;
            mejor = false;
            CosteMejorPeor = Double.MAX_VALUE;
            contNoTabu = 0;
            int tamx = aleatorio.nextInt(10 - 4) + 4;
            for (int j = 1; j <= tamx; j++) { // con el blk
                for (int k = 0; k < tam; k++) {
                    float compara = aleatorio.nextFloat();
                    if (compara <= oscilacion) {
                        cambiosDeVecino.add(k,1);
                        if (ma == 0) {
                            inf = soluActu[k] * 0.9;
                            sup = soluActu[k] * 1.1;
                            if (soluActu[k] < 0) {
                                intercambia(inf,sup);
                            }
                            if (inf < rmin)
                                inf = rmin;
                            if (sup > rmax)
                                sup = rmax;
                            vVecino[k] = randDoubleWithRange(inf,sup);
                        } else {
                            if (ma == 1) {
                                vVecino[k] = randDoubleWithRange(rmin,rmax);
                            } else {
                                if(ma== 2) {
                                    vVecino[k] = -soluActu[k];
                                }
                            }
                        }
                    } else {
                        vVecino[k] = soluActu[k];
                        cambiosDeVecino.add(k,0);
                    }
                }

                esTabu = false;

                for (int i = 0; i < lTabu.size(); i++) {
                    int cont = 0;
                    for (int p = 0; p < tam; p++) {
                        double valor = lTabu.get(i)[p];
                        inf = valor * 0.99;
                        sup = valor * 1.01;
                        if (valor < 0) {
                            intercambia(inf,sup);
                        }
                        if (vVecino[p] < inf || vVecino[p] > sup) {
                            cont++;
                            break;
                        }
                    }
                    if (cont == 0) {
                        esTabu = true;
                        break;
                    }
                }

                if (!esTabu) {
                    for (int i = 0; i < lTabuMovimientos.size(); i++) {
                        if (cambiosDeVecino == (lTabuMovimientos.get(i))) {
                            esTabu = true;
                            break;
                        }
                    }
                }

                if (!esTabu) {
                    contNoTabu++;
                    double costeVecino = evaluaCoste(vVecino, funcion);

                    if (costeVecino < mejorCosteVecino) {
                        mejorVecino = vVecino;
                        mejorCosteVecino = costeVecino;
                        cambiosDeMejorVecino = cambiosDeVecino;

                    }
                }
            }

            if (contNoTabu != 0) {
                if(valoresAnt!=mejorVecino) {
                double ancho = (rmax - rmin - 1) / 10;
                for (int i = 0; i < tam; i++) {
                    int posCol = 0;
                    for (double j = rmin; j < rmax; j += ancho) {
                        if (j < 0)
                            if (Math.abs(mejorVecino[i]) >= Math.abs(j) && Math.abs(mejorVecino[i]) < Math.abs(j + ancho)) {
                                memFrec[i][posCol]++;
                                break;
                            } else if (mejorVecino[i] >= j && mejorVecino[i] < j + ancho) {
                                memFrec[i][posCol]++;
                                break;
                            }
                        posCol++;
                    }
                }

                lTabu.add(mejorVecino);
                if (lTabu.size() > tenenciaTabu)
                    lTabu.remove(0);

                lTabuMovimientos.add(cambiosDeMejorVecino);
                if (lTabuMovimientos.size() > tenenciaTabu)
                    lTabuMovimientos.remove(0);
                }

                valoresAnt=mejorVecino;

                if (mejorCosteVecino < costeActual) {
                    soluActu = mejorVecino;
                    costeActual = mejorCosteVecino;
                    mejor = true;
                } else {
                    if (mejorCosteVecino < CosteMejorPeor) {
                        CosteMejorPeor = mejorCosteVecino;
                        mejorPeores = soluActu;
                    }
                }

                if (!mejor) {

                    costeActual = CosteMejorPeor;
                    soluActu = mejorPeores;
                    contador++;

                    ma=(ma)%3; // tengo caso 0,caso 1 y caso 2, para cuando genero los vecinos ver si empeora, si empeora cambia, si no continúa

                } else {
                    contador = 0;
                    if (costeActual < CGlobal) {
                        CGlobal = costeActual;
                        solucGlobal = soluActu;

                    }
                }

                if (contador == 50) {
                    if (osc == 0) {
                        if (costeMejorMomento > costeActual) {
                            costeMejorMomento = costeActual;
                        }
                    } else {
                        if (costeMejorMomento > costeActual) {
                            costeMejorMomento = costeActual;
                        }
                    }

                    contador = 0;

                    int prob = aleatorio.nextInt(100 - 1) - 1;
                    if (prob <= 50) {
                        osc = 0;
                        diversificacion(memFrec, solNu, rmin, rmax);
                    } else {
                        osc = 1;
                        intensificacion(memFrec, solNu, rmin, rmax);
                    }

                    soluActu = solNu;
                    costeActual = evaluaCoste(soluActu, funcion);

                    if (costeActual < CGlobal) {
                        CGlobal = costeActual;
                        solucGlobal=soluActu;
                    }

                    for (int i = 0; i < tam; i++)
                        for (int j = 0; j < 10; j++)
                            memFrec[i][j] = 0;
                    lTabu.clear();
                    lTabuMovimientos.clear();
                }
            }
        }

        soluActu=solucGlobal;

        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial)/1000000;
        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("Funcion:" + funcion);
        logger.info("RangoInf: " + rmin);
        logger.info("RangoSup: " + rmax);
        logger.info("El coste del algoritmo ma es:" + costeActual);
        logger.info("El numero de iteraciones recorridas es:" + iter);
        logger.info("El vector seria: "+ formatoVector(solucGlobal));
        logger.info("La semilla es:" + semilla);
    }
}
