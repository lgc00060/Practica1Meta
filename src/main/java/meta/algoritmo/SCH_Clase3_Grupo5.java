package meta.algoritmo;

import org.apache.log4j.Logger;

import java.util.Random;

public class SCH_Clase3_Grupo5 {
    public static void SCH_Clase3_Grupo5(int tampoblacion, double q0, double p, double fi,int tam, int evaluaciones,double greedy,double[][] distancia, int[] solucion,double alfa,int beta,long semilla,Logger logger){
        long tiempoInicial = System.nanoTime();
        Random aleatorio = new Random();
        double[][] feromona = new double[tam][tam];
        double[] feromonaHeuristica = new double[tam];
        long tiempo=0;
        double[][] heuristica = new double[tam][tam];
        boolean marcados[][]=new boolean[tampoblacion][tam];
        int[][] hormigas = new int[tampoblacion][tam];
        double[] costes = new double[tampoblacion];
        int contador=0;
        int elegido = 0;
        double[] prob = new double[tam];
        double q = aleatorio.nextDouble();
        double denominador = 0.0;
        double argMax = 0.0;
        int posArgMax = 0;
        double mejorCosteActual;
        double mejorCosteGlobal = Double.MAX_VALUE;
        int[] mejorHormigaActual = new int[tam];

        logger.info("Empieza ejecucion algoritmo SCH: ");

        double fInicial = (float) 1 / (tampoblacion * greedy);
        for (int i = 0; i < tam - 1; i++) {
            for (int j = i + 1; j < tam; j++) {
                if (i != j) {
                    feromona[j][i] = feromona[i][j] = fInicial;
                    heuristica[j][i] = heuristica[i][j] = 1 / distancia[i][j];
                }
            }
        }

        for (int i = 0; i < tampoblacion; i++) {
            for (int j = 0; j < tam; j++) {
                marcados[i][j] = true;
            }
        }

        do {
            //Carga de las hormigas iniciales
            for (int i = 0; i < tampoblacion; i++)
                solucion[i] = aleatorio.nextInt(tam);

            char c;
            //GENERAMOS las n-1 componentes pdtes. de las hormigas
            for (int comp = 1; comp < tam; comp++) {
                //Para cada hormiga
                for (int h = 0; h < tampoblacion; h++) {

                    //ELECCION del ELEMENTO de los no elegidos aun, a incluir en la solucion

                    //calculo la cantidad total de feromonaxheuristica desde la ciudad actual al resto
                    //de ciudades no visitadas aun
                    for (int i = 0; i < tam; i++) {
                        if (!marcados[h][i])
                            feromonaHeuristica[i] = Math.pow(heuristica[hormigas[h][comp - 1]][i], beta) * Math.pow(feromona[hormigas[h][comp - 1]][i], alfa);
                    }


                    for (int i = 0; i < tam; i++) {
                        if (!marcados[h][i]) {
                            denominador += feromonaHeuristica[i];
                            if (feromonaHeuristica[i] > argMax) {
                                argMax = feromonaHeuristica[i];
                                posArgMax = i;
                            }
                        }
                    }

                    if (q0 <= q)
                        elegido = posArgMax;
                    else {
                        for (int i = 0; i < tam; i++) {
                            if (!marcados[h][i]) {
                                double numerador = feromonaHeuristica[i];
                                prob[i] = numerador / denominador;
                            }
                        }

                        //elegimos la componente a añadir buscando en los intervalos de probabilidad
                        double Uniforme = aleatorio.nextDouble();  //aleatorio para regla de transición
                        double acumulado = 0.0;
                        for (int i = 0; i < tam; i++) {
                            if (!marcados[h][i]) {
                                acumulado += prob[i];
                                if (Uniforme <= acumulado) {
                                    elegido = i;
                                    break;
                                }
                            }
                        }
                    }

                    hormigas[h][comp] = elegido;
                    marcados[h][elegido] = true;

                    feromona[hormigas[h][comp - 1]][hormigas[h][comp]] = ((1 - fi) * feromona[hormigas[h][comp - 1]][hormigas[h][comp]]) + (fi * fInicial);
                    feromona[hormigas[h][comp]][hormigas[h][comp - 1]] = feromona[hormigas[h][comp - 1]][hormigas[h][comp]];
                }
            }


            //MEJOR HORMIGA
            mejorCosteActual = Double.MAX_VALUE;
            for (int i = 0; i < tampoblacion; i++) {
                double coste = 0.0;
                for (int j = 0; j < tam - 1; j++)
                    coste += distancia[solucion[j]][solucion[j + 1]];
                coste += distancia[solucion[0]][solucion[tam - 1]];

                if (coste < mejorCosteActual) {
                    mejorCosteActual = coste;
                    mejorHormigaActual = hormigas[i];
                }
            }

            //ACTUALIZAMOS si la mejor actual mejora al mejor global
            if (mejorCosteActual < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteActual;
                solucion = mejorHormigaActual;
            }


            double deltaMejor = 1 / mejorCosteActual;
            for (int i = 0; i < tam - 1; i++) {
                feromona[mejorHormigaActual[i]][mejorHormigaActual[i + 1]] += (p * deltaMejor);
                feromona[mejorHormigaActual[i + 1]][mejorHormigaActual[i]] =
                        feromona[mejorHormigaActual[i]][mejorHormigaActual[i + 1]];
            }


            /*for (int i = 0; i < tam; i++) {
                for (int j = 0; j < tam; j++) {
                    if (i != j) {
                        feromona[i][j] = ((1 - p) * feromona[i][j]);
                    }
                }
            }*/


            //Tenemos que limpiar
            hormigas = new int[tampoblacion][tam];
            costes = new double[tampoblacion];
            for (int i = 0; i < tampoblacion; i++) {
                for (int j = 0; j < tam; j++) {
                    marcados[i][j] = true;
                }
            }

        }while (contador < evaluaciones && tiempo<600000);


        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial)/1000000;
        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("El coste del algoritmo SCH es:" + mejorCosteGlobal);
        logger.info("La semilla es:" + semilla);
    }

}
