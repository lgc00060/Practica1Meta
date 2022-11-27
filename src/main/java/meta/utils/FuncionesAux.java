package meta.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static meta.funciones.Funciones.evaluaCoste;
//import static sun.awt.image.MultiResolutionCachedImage.map;

public class FuncionesAux {

    public static void Mutacion(double[] v, int pos, double valor) {
        v[pos] = valor;
    }

    public double MAPE(double[] real, double[] estimation) {
        int N = real.length;
        double score;
        double sum = 0.0;
        double num = 0.0;
        for (int i = 0; i < N; i++) {
            if (real[i] != 0) {
                sum += Math.abs((real[i] - estimation[i]) / Math.abs(real[i]));
                num++;
            }
        }
        score = sum / num;
        return score;
    }

    public double RMSE(double[] real, double[] estimation) {
        int N = real.length;
        double score;
        double sum = 0;
        for (int i = 0; i < N; i++) {
            sum += Math.pow(real[i] - estimation[i], 2);
        }
        score = Math.sqrt(1.0 / N * sum);
        return score;
    }

    public static List<double[]> cargaCromosomasIniciales(int tampoblacion, int tam, double rmin, double rmax, long semilla) {
        Random aleatorio = new Random();
        aleatorio.setSeed(semilla);
        List<double[]> vector = new ArrayList<>();
        double[] v = new double[tam];
        for (int i = 0; i < tampoblacion; i++) {
            for (int j = 0; j < tam; j++) {
                v[j] = aleatorio.nextDouble() * (rmax - rmin) + rmin;
            }
            vector.add(i, v);
        }
        return vector;
    }

    public static void cruceMedia(int tam, double[] a, double[] b, double[] h) {
        for (int i = 0; i < tam; i++) {
            h[i] = (a[i] + b[i]) / 2;
        }
    }

    public static void torneo(int tampoblacion, int[] posicion, double[] costes, List<double[]> cromosomas, List<double[]> nuevaGeneracion, double[] costeNuevaGeneracion) {
        Random aleatorio = new Random();
        int x, j;
        j = aleatorio.nextInt(tampoblacion - 1);

        for (int k = 0; k < tampoblacion; k++) {
            x = aleatorio.nextInt(tampoblacion - 1);
            while (x == j) ;
            posicion[k] = (costes[x] < costes[j]) ? x : j;
        }

        //Nos quedamos con los cromosomas mas prometedores
        for (int i = 0; i < tampoblacion; i++) {
            if (posicion[i] == 50)
                posicion[i]--;

            nuevaGeneracion.add(i, cromosomas.get(posicion[i]));
            costeNuevaGeneracion[i] = costes[posicion[i]];
        }
    }

    public static void cruceBlX(int tam, double[] v, double[] w, double alfa, double[] h1, double rmin, double rmax) {
        Random aleatorio = new Random();
        double Cmax, Cmin, x = 0.0;

        for (int i = 0; i < tam; i++) {
            Cmax = Math.max(v[i], w[i]);
            Cmin = Math.min(v[i], w[i]);
            x = Cmax - Cmin;

            double r1 = Cmin - (x * alfa);

            if (r1 < rmin)
                r1 = rmin;

            double r2 = Cmax + (x * alfa);

            if (r2 > rmax)
                r2 = rmax;

            h1[i] = aleatorio.nextDouble() * (r2 + r1);
        }
    }

    public static void cruceTorneo2a2(int tampoblacion, List<double[]> nuevaGeneracion, double[] costeNuevaGeneracion, double[] mejorPrimero, double[] mejorSegundo, int i, double costeMejorPrimero, double costeMejorSegundo) {
        Random aleatorio = new Random();
        int c1, c2, c3, c4;
        int posAnt = 0;

        c1 = aleatorio.nextInt((tampoblacion - 1) + 0);
        while (c1 == (c2 = aleatorio.nextInt(tampoblacion - 1) + 0)) ;

        if (costeNuevaGeneracion[c1] < costeNuevaGeneracion[c2]) {
            mejorPrimero = nuevaGeneracion.get(c1);
            costeMejorPrimero = costeNuevaGeneracion[c1];
        } else {
            mejorPrimero = nuevaGeneracion.get(c2);
            costeMejorPrimero = costeNuevaGeneracion[c2];
        }

        while (posAnt == (c3 = aleatorio.nextInt(tampoblacion - 1) + 0)) ;
        while (posAnt == (c4 = aleatorio.nextInt(tampoblacion - 1) + 0)) ;

        if (costeNuevaGeneracion[c3] < costeNuevaGeneracion[c4]) {
            mejorSegundo = nuevaGeneracion.get(c3);
            costeMejorSegundo = costeNuevaGeneracion[c3];
        } else {
            mejorSegundo = nuevaGeneracion.get(c4);
            costeMejorSegundo = costeNuevaGeneracion[c4];
        }
    }

    public static void mutar(int tampoblacion, int tam, double probabilidadMutacion, double rmin, double rmax, List<double[]> nuevaGeneracion, boolean[] marcados) {
        Random aleatorio = new Random();

        for (int i = 0; i < tampoblacion; i++) {
            boolean m = false;
            for (int j = 0; j < tam; j++) {
                double x = aleatorio.nextDouble();
                if (x < probabilidadMutacion) {
                    m = true;
                    double valor = randDoubleWithRange(rmin, rmax);
                    Mutacion(nuevaGeneracion.get(i), j, valor);
                }
            }
            if (m)
                marcados[i] = true;
        }
    }

    public static void calculaMejorNuevaPoblacion(int tampoblacion, boolean[] marcados, double[] costeNuevaGeneracion, List<double[]> nuevaGeneracion, String funcion, int contador, double mejorCosteHijo, int mejorCromosomaHijo) {
        mejorCosteHijo = Double.MAX_VALUE;

        for (int i = 0; i < tampoblacion; i++) {
            if (marcados[i]) {
                costeNuevaGeneracion[i] = evaluaCoste(nuevaGeneracion.get(i), funcion);
                contador++;
            }

            if (costeNuevaGeneracion[i] < mejorCosteHijo) {
                mejorCosteHijo = costeNuevaGeneracion[i];
                mejorCromosomaHijo = i;
            }
        }
    }

    public static void elitismo(int tampoblacion, List<double[]> nuevaGeneracion, double[] mejorCromosoma, double[] costeNuevaGeneracion, double mejorCoste) {
        Random aleatorio = new Random();
        int peor;
        int p1 = aleatorio.nextInt(tampoblacion - 1 - 0) + 0, p2 = aleatorio.nextInt(tampoblacion - 1 - 0) + 0,
                p3 = aleatorio.nextInt(tampoblacion - 1 - 0) + 0, p4 = aleatorio.nextInt(tampoblacion - 1 - 0) + 0;

        while (p1 == p2) ;
        while (p1 == p2 && p2 == p3) ;
        while (p1 == p2 && p2 == p3 && p3 == p4) ;

        if (costeNuevaGeneracion[p1] > costeNuevaGeneracion[p2] && costeNuevaGeneracion[p1] > costeNuevaGeneracion[p3] && costeNuevaGeneracion[p1] > costeNuevaGeneracion[p4])
            peor = p1;
        else if (costeNuevaGeneracion[p2] > costeNuevaGeneracion[p1] && costeNuevaGeneracion[p2] > costeNuevaGeneracion[p3] && costeNuevaGeneracion[p2] > costeNuevaGeneracion[p4])
            peor = p2;
        else if (costeNuevaGeneracion[p3] > costeNuevaGeneracion[p1] && costeNuevaGeneracion[p3] > costeNuevaGeneracion[p2] && costeNuevaGeneracion[p3] > costeNuevaGeneracion[p4])
            peor = p3;
        else
            peor = p4;

        nuevaGeneracion.add(peor, mejorCromosoma);
        costeNuevaGeneracion[peor] = mejorCoste;
    }

    public static void eleccion2Aleatorios(int tampoblacion, List<double[]> cromosomas, double[] costes, int i, double[] ale1, double[] ale2) {
        Random aleatorio = new Random();
        double[] obj, nuevo = new double[tampoblacion]; //ALEATORIO 1, 2
        int a1, a2;
        a2 = aleatorio.nextInt(tampoblacion - 1);

        do {
            a1 = aleatorio.nextInt(tampoblacion - 1);
            while (a1 == a2) ;
        } while (a1 != i && a2 != i);

        ale1 = cromosomas.get(a1);
        ale2 = cromosomas.get(a2);
    }

    public static void torneoK3(int tampoblacion, int i, int a1, int k1, int k2, int k3, int k4) {
        Random aleatorio = new Random();
        int a2;
        a2 = aleatorio.nextInt(tampoblacion - 1);
        k2 = aleatorio.nextInt(tampoblacion - 1);
        k3 = aleatorio.nextInt(tampoblacion - 1);

        do {
            k1 = aleatorio.nextInt(tampoblacion - 1);
            while (k1 == k2) ;
            while (k1 == k2 && k2 == k3) ;
        } while (k1 != i && k1 != a1 && k1 != a2 &&
                k2 != i && k2 != a1 && k2 != a2 &&
                k3 != i && k3 != a1 && k3 != a2);
    }

    public static double operadorRecombinacion(double[] padre, int j, double[] ale1, double[] ale2, double Factor) {
        return padre[j] + (Factor * (ale1[j] - ale2[j]));
    }

    public static void reemplazamiento(double costeNuevo, int i, double[] costes, List<double[]> cromosomas, double[] nuevo, double mejorCoste, double[] mejorCromosoma) {
        if (costeNuevo < costes[i]) {
            cromosomas.add(i, nuevo);
            costes[i] = costeNuevo;
            if (costeNuevo < mejorCoste) {
                mejorCoste = costeNuevo;
                mejorCromosoma = nuevo;
            }
        }
    }

    public static double randDoubleWithRange(double min, double max) {
        Random random = new Random();
        return random.nextDouble() * (max - min) + min;
    }

    private static String convertToLogAppender(String algoritmo, ArrayList<String> funcion, String semilla) {
        return "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                " = org.apache.log4j.FileAppender\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".file = src/main/java/meta/ArchivosLog" + funcion + "/" + algoritmo + "/" + semilla + ".log\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".append = false\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".layout = org.apache.log4j.PatternLayout\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".layout.ConversionPattern = %d %c{3} - %m%n\n\n" +
                "log4j.logger." + funcion + "." + algoritmo + "." + semilla +
                " = INFO, " + funcion + "." + algoritmo + "." + semilla + "\n\n";
    }


    //se ejecuta una vez para crear el log properties
    public static void createAppendersLog(List<String> archivosConfig, String ruta) throws IOException {
        FileOutputStream csvFile = new FileOutputStream("log/log4j.properties");
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            for (String archivo : archivosConfig) {
                Lector lector = new Lector(ruta + archivo);
                List<String> algoritmos = lector.getAlgoritmos();
                Long[] semillas = lector.getSemilla();
                //List<Long> semillasList = Arrays.stream(semillas).boxed().collect(Collectors.toList());
                for (String algoritmo : algoritmos) {
                    ArrayList <String> ListaSemillas;
                    //ListaSemillas.stream();
                            //.map(s -> convertToLogAppender(algoritmo, lector.getFunciones(), String.valueOf(s)))
                           // .forEach(pw::print);

                    pw.println();
                }
            }
        }
    }
}