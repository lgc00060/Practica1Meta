package meta.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


public class FuncionesAux {
     /**
     * @brief Funcion que sirve para
     * @param v: vector
     * @param pos: posicion del vector
     * @param valor: valor
     */
    public static void Mutacion(double[] v, int pos, double valor) {
        v[pos] = valor;
    }

    public static double MAPE(double[] real, double[] estimacion) {
        int N = real.length;
        double score;
        double sum = 0.0;
        double num = 0.0;
        for (int i = 0; i < N; i++) {
            if (real[i] != 0) {
                sum += Math.abs((real[i] - estimacion[i]) / Math.abs(real[i]));
                num++;
            }
        }
        score = sum / num;
        return score;
    }

    public static double RMSE(double[] real, double[] estimacion) {
        int N = real.length;
        double score;
        double sum = 0;
        for (int i = 0; i < N; i++) {
            sum += Math.pow(real[i] - estimacion[i], 2);
        }
        score = Math.sqrt(1.0 / N * sum);
        return score;
    }

    public static void cruceMedia(int tam, double[] a, double[] b, double[] h) {
        for (int i = 0; i < tam; i++) {
            h[i] = (a[i] + b[i]) / 2;
        }
    }

    public static void torneo(int tampoblacion, int[] posicion, double[] costes, List<double[]> cromosomas, List<double[]> nuevaGeneracion, double[] costeNuevaGeneracion) {
        Random aleatorio = new Random();
        for (int i = 0; i < tampoblacion; i++) {
            int j, k;
            j = aleatorio.nextInt(tampoblacion - 1);
            while (j == (k = aleatorio.nextInt(10 + - 1))) ;
            posicion[i] = (costes[i] < costes[k]) ? j : k;
        }
        for (int i = 0; i < tampoblacion ; i++) {
            nuevaGeneracion.add(i, cromosomas.get(posicion[i]));
            costeNuevaGeneracion[i] = costes[posicion[i]];
        }
    }

    public static void cruceBlX(int tam, double[] v, double[] w, double alfa, double[] h1, double rmin, double rmax) {
        double Cmax, Cmin, x = 0.0;

        for (int i = 0; i < tam; i++) {
            Cmax = Math.max(v[i], w[i]);
            Cmin = Math.min(v[i], w[i]);
            double r1 = Cmin - (x * alfa);

            x = Cmax - Cmin;


            if (r1 < rmin)
                r1 = rmin;

            double r2 = Cmax + (x * alfa);

            if (r2 > rmax)
                r2 = rmax;

            h1[i] =  randDoubleWithRange(r1,r2);
        }
    }

    public static double funcionPotencia(double[] v,String tipoError,List <Daido> observaciones){
        double potencia;
        int filas=observaciones.size();//
        double[] real = new double[filas], estimado = new double[filas];

        for (int i=0; i<filas; i++){
            potencia=observaciones.get(i).getDni() * (v[0] + (v[1] * observaciones.get(i).getDni()) + (v[2]
                    * observaciones.get(i).getTemp_amb()) + (v[3] * observaciones.get(i).getVel_viento()) +
                    (v[4] * observaciones.get(i).getSmr()));

            estimado[i]=potencia;
            real[i]=observaciones.get(i).getPotencia();

        }

        return tipoError.equals("MAPE") ? MAPE(real, estimado) : RMSE(real, estimado);

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

    public static void elitismo(int tampoblacion, List<double[]> nuevaGeneracion, double[] mejorCromosoma, double[] costeNuevaGeneracion, double mejorCoste) {
        Random aleatorio = new Random();
        int peor;
        int p1, p2, p3, p4;
        p1 = aleatorio.nextInt(tampoblacion);
        p2 = aleatorio.nextInt(tampoblacion);
        p3 = aleatorio.nextInt(tampoblacion);
        p4 = aleatorio.nextInt(tampoblacion);
        while (p1 == p2) ;
        while (p1 == p3) ;
        while (p1 == p4) ;

        if (costeNuevaGeneracion[p1] > costeNuevaGeneracion[p2] && costeNuevaGeneracion[p1] > costeNuevaGeneracion[p3]
                && costeNuevaGeneracion[p1] > costeNuevaGeneracion[p4])
            peor = p1;
        else if (costeNuevaGeneracion[p2] > costeNuevaGeneracion[p1] && costeNuevaGeneracion[p2] > costeNuevaGeneracion[p3]
                && costeNuevaGeneracion[p2] > costeNuevaGeneracion[p4])
            peor = p2;
        else if (costeNuevaGeneracion[p3] > costeNuevaGeneracion[p1] && costeNuevaGeneracion[p3] > costeNuevaGeneracion[p2]
                && costeNuevaGeneracion[p3] > costeNuevaGeneracion[p4])
            peor = p3;
        else
            peor = p4;
        nuevaGeneracion.add(peor, mejorCromosoma);
        costeNuevaGeneracion[peor] = mejorCoste;
    }


    public static void aleatorios(int tampoblacion, List<double[]> cromosomas, int a1, int a2, int i, double[] ale1, double[] ale2) {
        Random aleatorio = new Random();

        do {
            a1 = aleatorio.nextInt(tampoblacion);
            while (a1 == (a2 = aleatorio.nextInt(tampoblacion))) ;
        } while (a1 != i && a2 != i);
        ale1 = cromosomas.get(a1);
        ale2 = cromosomas.get(a2);
    }

    public static void torneoK3(int tampoblacion, int i, int a1, int k1, int k2, int k3,int a2) {
        Random aleatorio = new Random();

        do {
            k1 = aleatorio.nextInt(tampoblacion);
            while (k1 ==(k2 = aleatorio.nextInt(tampoblacion))) ;
            while ((k2 ==(k3 = aleatorio.nextInt(tampoblacion)))) ;
        } while (k1 != i &&  k1 != a1 && k1 != a2 &&
                 k2 != i &&  k2 != a1 && k2 != a2 &&
                 k3 != i &&  k3 != a1 && k3 != a2);
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
    public static void createAppendersLog(String archivosConfig, String ruta) throws IOException {
        FileOutputStream csvFile = new FileOutputStream("src/main/resources/log4j.properties");
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            Lector lector = new Lector(ruta + archivosConfig);
            List<String> algoritmos = lector.getAlgoritmos();
            Long[] semillas = lector.getSemilla();
            List<Long> semillasList = Arrays.stream(semillas).collect(Collectors.toList());
            for (String algoritmo : algoritmos) {
                ArrayList <String> ListaSemillas;
                semillasList.stream()
                        .map(s -> convertToLogAppender(algoritmo, lector.getFunciones(), String.valueOf(s)))
                        .forEach(pw::print);

                pw.println();
            }
        }
    }
    public static String getFiles(final File folder){
        String fileName="";
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                getFiles(fileEntry);
            } else {
                fileName=(fileEntry.getName());
            }
        }
        return fileName;

    }
}