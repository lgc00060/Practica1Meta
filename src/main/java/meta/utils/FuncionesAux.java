package meta.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import static meta.utils.DaidoLector.daidos;


public class FuncionesAux {
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

    public static void cruceMedia(int tam, double[] a, double[] b, double[] res) {
        for (int i = 0; i < tam; i++) {
            res[i] = (a[i] + b[i]) / 2;
        }
    }

    /**
     * @brief Funcion que sirve para seleccionar por torneo a los cromosomas que sean mas favorables entre cada 2 parejas aleatorias
     * @param tampoblacion: tamaño de poblacion
     * @param posicion: posiciones de los cromosomas que guardo pq gana el torneo
     * @param costes: coste de los cromosomas
     * @param cromosomas cromosomas
     * @param nuevaGeneracion cromosomas de la nueva generacion
     * @param costeNuevaGeneracion coste de los cromosomas de la nueva generacion
     */
    public static void torneo(int tampoblacion, int[] posicion, double[] costes, List<double[]> cromosomas, List<double[]> nuevaGeneracion, double[] costeNuevaGeneracion) {
        Random aleatorio = new Random();
        for (int i = 0; i < tampoblacion; i++) {
            int j, k;
            j = aleatorio.nextInt(tampoblacion);
            while (j == (k = aleatorio.nextInt(tampoblacion))) ;
            posicion[i] = (costes[i] < costes[k]) ? j : k;
        }

        for (int i = 0; i < tampoblacion ; i++) {
            nuevaGeneracion.add(i, cromosomas.get(posicion[i]));
            costeNuevaGeneracion[i] = costes[posicion[i]];
        }
    }

    /**
     * @brief Funcion que sirve para cruzar 2 padres obtenidos por torneo 2 a 2 y obtener un hijo
     * @param tam: tamaño del vector
     * @param v: vector mejor primero
     * @param w: vector mejor segundo
     * @param alfa valor alfa
     * @param hijo hijo que genero
     * @param rmin rango minimo
     * @param rmax rango maximo
     */
    public static void cruceBlX(int tam, double[] v, double[] w, double alfa, double[] hijo, double rmin, double rmax) {
        double Cmax, Cmin, diferencia = 0.0;

        for (int i = 0; i < tam; i++) {
            Cmax = Math.max(v[i], w[i]);
            Cmin = Math.min(v[i], w[i]);
            diferencia = Cmax - Cmin;

            double valor1 = Cmin - (diferencia * alfa);
            if (valor1 < rmin)
                valor1 = rmin;

            double valor2 = Cmax + (diferencia * alfa);
            if (valor2 > rmax)
                valor2 = rmax;

            hijo[i] =  randDoubleWithRange(valor1,valor2);
        }
    }

    public static double funcionPotenciaMAPE(double[] v) throws IOException {
        double potencia;
        List<Daido> vectorob= daidos("src/main/resources/daido-tra.dat");
        int filas=vectorob.size();//
        double[] valorReal = new double[filas], valorEst = new double[filas];

        for (int i=0; i<filas; i++){
            potencia=vectorob.get(i).getDni() * (v[0] + (v[1] * vectorob.get(i).getDni()) + (v[2]
                    * vectorob.get(i).getTemp_amb()) + (v[3] * vectorob.get(i).getVel_viento()) +
                    (v[4] * vectorob.get(i).getSmr()));

            valorEst[i]=potencia;
            valorReal[i]=vectorob.get(i).getPotencia();

        }
        return MAPE(valorReal, valorEst);
    }

    public static double funcionPotenciaRMSE(double[] v) throws IOException {
        double potencia;
        List<Daido> vectorob= daidos("src/main/resources/daido-tra.dat");
        int filas=vectorob.size();//
        double[] valorReal = new double[filas], valorEst = new double[filas];

        for (int i=0; i<filas; i++){
            potencia=vectorob.get(i).getDni() * (v[0] + (v[1] * vectorob.get(i).getDni()) + (v[2]
                    * vectorob.get(i).getTemp_amb()) + (v[3] * vectorob.get(i).getVel_viento()) +
                    (v[4] * vectorob.get(i).getSmr()));

            valorEst[i]=potencia;
            valorReal[i]=vectorob.get(i).getPotencia();

        }
        return RMSE(valorReal, valorEst);
    }

    /**
     * @brief Funcion que sirve para mutar los padres cruzados con la probabilidad de mutacion
     * @param tampoblacion: tamaño de la poblacion
     * @param tam: tamaño del vector
     * @param probabilidadMutacion: probabilidad de que haya mutacion
     * @param rmin rango minimo
     * @param rmax rango maximo
     * @param nuevaGeneracion cromosomas de la nueva generacion
     * @param marcados marco los cromosomas que se hayan modificado
     */
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

    /**
     * @brief Funcion que sirve para sustituir al peor cromosoma de la nueva generacion por el mejor de la poblacion anterior
     * @param tampoblacion tamaño de poblacion
     * @param nuevaGeneracion cromosomas de la nueva generacion
     * @param mejorCromosoma mejor cromosoma de la poblacion anterior(padre)
     * @param costeNuevaGeneracion coste de los cromosomas de la nueva generacion
     * @param mejorCoste coste del mejor cromosoma
     */
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

    /**
     * @brief Funcion que sirve para generar 2 aleatorios distintos entre si
     * @param tampoblacion tamaño de la poblacion
     * @param cromosomas cromosomas
     * @param a1 valor1
     * @param a2 valor 2
     * @param i iterador
     * @param ale1 aleatorio 1
     * @param ale2 aleatorio 2
     */
    public static void aleatorios(int tampoblacion, List<double[]> cromosomas, int a1, int a2, int i, double[] ale1, double[] ale2) {
        Random aleatorio = new Random();

        do {
            a1 = aleatorio.nextInt(tampoblacion);
            while (a1 == (a2 = aleatorio.nextInt(tampoblacion))) ;
        } while (a1 != i && a2 != i);
        ale1 = cromosomas.get(a1);
        ale2 = cromosomas.get(a2);
    }

    /**
     * @brief Funcion que sirve para elegir un individuo objetivo por torneo k=3, distinto de a1,a2 y del padre
     * @param tampoblacion tamaño de poblacion
     * @param i iterador
     * @param a1 aleatorio1 elegido antes
     * @param k1 valor aleatorio
     * @param k2 valor aleatorio
     * @param k3 valor aleatorio
     * @param a2 aleatorio 2 elegido antes
     */
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

    /**
     * @brief Funcion que sirve para reemplazar si y solo si el hijo es mejor que el padre
     * @param costeNuevo nuevo coste
     * @param i iterador
     * @param costes costes de los cromosomas
     * @param cromosomas cromosomas
     * @param nuevo nuevo hijo
     * @param mejorCoste mejor coste hasta ahora
     * @param mejorCromosoma mejor cromosoma hasta ahora
     */
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

    private static String convertToLogAppender(String algoritmo, String funcion, String semilla) {
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


    public static void createAppendersLog(String archivosConfig, String ruta) throws IOException {
        FileOutputStream csvFile = new FileOutputStream("src/main/resources/log4j.properties");
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            Lector lector = new Lector(ruta + archivosConfig);
            List<String> algoritmos = lector.getAlgoritmos();
            Long[] semillas = lector.getSemilla();
            List<Long> semillasList = Arrays.stream(semillas).collect(Collectors.toList());
            for (String algoritmo : algoritmos) {
                for (int i = 0; i < lector.getFunciones().size(); i++) {
                    String funcion = lector.getFunciones().get(i);
                    ArrayList <String> ListaSemillas;
                    semillasList.stream()
                            .map(s -> convertToLogAppender(algoritmo, funcion, String.valueOf(s)))
                            .forEach(pw::print);

                    pw.println();

                }

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

    public static String formatoVector(double []v){
        StringBuilder vector = new StringBuilder();
        vector.append("[");
        for (int i = 0; i < v.length - 1; i++) {
            vector.append(v[i]).append(", ");
        }
        vector.append(v[v.length - 1]).append("]");
        return vector.toString();
    }
}