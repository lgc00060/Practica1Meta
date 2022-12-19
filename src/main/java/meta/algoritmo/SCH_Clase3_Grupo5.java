package meta.algoritmo;

import org.apache.log4j.Logger;

import java.util.Random;

public class SCH_Clase3_Grupo5 {
    public static void SCH_Clase3_Grupo5(int tamHormigas, double q0, double p, double fi,int tamCiudades, int evaluaciones,double greedy,double[][] matrizDistancias, int[] solucion,double alfa,int beta,long semilla,Logger logger) {
        long tiempoInicial = System.nanoTime();
        Random aleatorio = new Random();
        double[][] feromona = new double[tamCiudades][tamCiudades];
        double[] feromonaHeuristica = new double[tamCiudades];
        double tiempoInicioAlg = 0;
        double[][] heuristica = new double[tamCiudades][tamCiudades];
        boolean verctorMarcaje[][] = new boolean[tamHormigas][tamCiudades];
        int[][] hormigas = new int[tamHormigas][tamCiudades];
        int contador = 0;
        int elegido = 0;
        double coste;
        double[] prob = new double[tamCiudades];
        double q = aleatorio.nextDouble();
        double denominador = 0.0;
        double argMax = 0.0;
        int posArgMax = 0;
        double mejorCosteActual;
        double mejorCosteGlobal = Double.MAX_VALUE;
        int[] mejorHormigaActual = new int[tamCiudades];
        double tiempoFinalAlg = 0.0;

        logger.info("Empieza ejecucion algoritmo SCH: ");

        tiempoFinalAlg=0;

        double feromonaInicial = 1 / (tamHormigas * greedy); //carga inicial de feromona y heuristica

        logger.info("Valor del greedy: "+ greedy);

        for (int i = 0; i < tamCiudades; i++) {
            for (int j = 0; j < tamCiudades; j++) {
                if (i != j) {
                    feromona[j][i] = feromonaInicial;
                    feromona[i][j] = feromonaInicial;
                    heuristica[j][i] = 1 / matrizDistancias[i][j]; //invertidos de las distancias
                    heuristica[i][j]= 1 / matrizDistancias[i][j];
                }
            }
        }

        while (contador < evaluaciones && tiempoFinalAlg < 600000){
            tiempoInicioAlg = System.nanoTime(); //por cada iteracion o generacion de hormigas

            for (int i = 0; i < tamHormigas; i++) {
                for (int j = 0; j < tamCiudades; j++) {
                    verctorMarcaje[i][j] = false; //para saber si ha pasado por esa ciudad o no
                }
            }

            //Carga de las hormigas iniciales
            for (int i = 0; i < tamHormigas; i++){
                hormigas[i][0] = aleatorio.nextInt(tamCiudades); //generamos la primera ciudad para las 10 hormigas
                verctorMarcaje[i][0]=true; //marcamos esa ciudad para las 10 hormigas
            }


            for (int ciudades = 1; ciudades < tamCiudades; ciudades++) { //para todas las ciudades de cada una de las cajas de la hormiga
                for (int hormig = 0; hormig < tamHormigas; hormig++) { //cada hormiga de las 10

                    for (int i = 0; i < tamCiudades; i++) { // calculamos la cantidad total de feromona x heuristica desde la ciudad donde estamos al resto de ciudades no visitadas tdvia
                        if (!verctorMarcaje[hormig][i]){
                            feromonaHeuristica[i] = Math.pow(heuristica[hormigas[hormig][ciudades - 1]][i], beta) * Math.pow(feromona[hormigas[hormig][ciudades - 1]][i], alfa);

                            denominador += feromonaHeuristica[i];
                            if (feromonaHeuristica[i] > argMax) {
                                argMax = feromonaHeuristica[i];
                                posArgMax = i;
                            }
                        }
                    }

                    if(q0<q){
                        for (int i = 0; i < tamCiudades; i++) {
                            if (!verctorMarcaje[hormig][i]) {
                                double numerador = feromonaHeuristica[i];
                                prob[i] = numerador / denominador;
                            }
                        }

                        double aleatorioUniforme = aleatorio.nextDouble();
                        double acumulado = 0.0;
                        for (int i = 0; i < tamCiudades; i++) {
                            if (!verctorMarcaje[hormig][i]) {
                                acumulado += prob[i];
                                if (aleatorioUniforme <= acumulado) {
                                    elegido = i;
                                    break;
                                }
                            }
                        }
                    }else{
                        if(q0>=q)
                            elegido = posArgMax;
                    }


                    hormigas[hormig][ciudades] = elegido; //siguiente ciudad a la que la hormiga se va
                    verctorMarcaje[hormig][elegido] = true; //esa ciudad ha sido visitada por la hormiga, no puedo volver a elegirla

                    feromona[hormigas[hormig][ciudades - 1]][hormigas[hormig][ciudades]] = ((1 - fi) * feromona[hormigas[hormig][ciudades - 1]][hormigas[hormig][ciudades]]) + (fi * feromonaInicial);
                    feromona[hormigas[hormig][ciudades]][hormigas[hormig][ciudades - 1]] = feromona[hormigas[hormig][ciudades - 1]][hormigas[hormig][ciudades]];
                }
            }

            //Nos quedamos con la mejor hormiga de las 10 que tengo
            mejorCosteActual = Double.MAX_VALUE;
            for (int i = 0; i < tamHormigas; i++) {
                coste = 0.0;
                for (int j = 0; j < tamCiudades - 1; j++)
                    coste += matrizDistancias[solucion[j]][solucion[j + 1]];
                    coste += matrizDistancias[solucion[0]][solucion[tamCiudades - 1]];

                if (coste < mejorCosteActual) {
                    mejorCosteActual = coste;
                    mejorHormigaActual = hormigas[i];
                }
            }

            //ACTUALIZO SII la mejor hormiga actual mejora a la mejor global
            if (mejorCosteActual < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteActual;
                solucion = mejorHormigaActual;
            }

            //DEMONIO
            double deltaMejor = 1 / mejorCosteActual;
            for (int i = 0; i < tamCiudades - 1; i++) {
                feromona[mejorHormigaActual[i]][mejorHormigaActual[i + 1]] = (p * deltaMejor);
                feromona[mejorHormigaActual[i + 1]][mejorHormigaActual[i]] =feromona[mejorHormigaActual[i]][mejorHormigaActual[i + 1]];
            }

            for (int i = 0; i < tamCiudades; i++) {
                for (int j = 0; j < tamCiudades; j++) {
                    if (i != j)
                        feromona[i][j] = ((1 - p) * feromona[i][j]);
                }
            }

            double tiempoFinAlg = System.nanoTime();
            tiempoFinalAlg = tiempoInicioAlg + tiempoFinAlg;
        }

        double tiempoFinal = System.nanoTime();
        double resultado = (tiempoFinal - tiempoInicial) / 1000000;
        logger.info("El tiempo total de ejecucion en ms es: " + resultado);
        logger.info("El coste del algoritmo SCH es:" + mejorCosteGlobal);
        logger.info("Mejor hormiga: "+ mejorHormigaActual);
        logger.info("La semilla es:" + semilla);
    }

}
