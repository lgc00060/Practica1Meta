package meta.utils;

import java.util.List;
import java.util.Random;

import static meta.funciones.Funciones.evaluaCoste;

public class FuncionesAux {
    public static void cargaAleatoria(int tam, double[] v, double rmin, double rmax) {
        for (int i = 0; i < tam; i++) {
            Random random = new Random();
            v[i] = random.nextDouble();
        }
    }

    public static void Mutacion(double[] v, int pos, double valor) {
        v[pos] = valor;

    }

    public static double MAPE(double[] real, double[] estimation) {
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

    public static double RMSE(double[] real, double[] estimation) {
        int N = real.length;
        double score;
        double sum = 0;
        for (int i = 0; i < N; i++) {
            sum += Math.pow(real[i] - estimation[i], 2);
        }
        score = Math.sqrt(1.0 / N * sum);
        return score;
    }

    public static void cargaCromosomasIniciales(int tampoblacion, List<double[]> cromosomas, double rmin, double rmax, String funcion, double[] costes, int tam, double mejorCoste, double[] mejorCruce){
        for (int i = 0; i < tampoblacion; i++) {
            cargaAleatoria(tam, cromosomas.get(i), rmin, rmax);
            costes[i] = evaluaCoste(cromosomas.get(i), funcion);

            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCruce = cromosomas.get(i);
            }
        }
    }

    public static void torneo(int tampoblacion, int[] posicion, double[] costes,List<double[]> cromosomas,List<double[]> nuevaGeneracion,double[] costeNuevaGeneracion){
        Random aleatorio = new Random();
        for (int k = 0; k < tampoblacion; k++) {
            int i,j;
            i =  aleatorio.nextInt(tampoblacion - 1);
            while (i == (j = aleatorio.nextInt(tampoblacion - 1)));
            posicion[k] = (costes[i] < costes[j]) ? i : j;
        }

        //Nos quedamos con los cromosomas mas prometedores
        for (int i = 0; i < tampoblacion; i++) {
            nuevaGeneracion.add(i, cromosomas.get(posicion[i]));
            costeNuevaGeneracion[i] = costes[posicion[i]];
        }
    }

    public static void cruceBlX(int tam, double[] v, double[] w, double alfa, double[] h1,double rmin, double rmax){
        Random aleatorio = new Random();
        double Cmax,Cmin,x=0.0;

            for (int i=0; i<tam; i++){
                Cmax=Math.max(v[i],w[i]);
                Cmin=Math.min(v[i],w[i]);
                double r1=Cmin-(x*alfa);

                if (r1<rmin)
                    r1=rmin;

                double r2=Cmax+(x*alfa);

                if (r2<rmin)
                    r2=rmin;

                h1[i]= aleatorio.nextDouble()*(r2+r1);
            }
    }

    public static void cruceTorneo2a2(int tam,int tampoblacion,double[] h,double[] costes,List<double[]> nuevaGeneracion,double probabilidadCruce,boolean[] marcados,
                                      List<double[]> nuevaGeneracionSegunda,double[] costeNuevaGeneracion,double[] costeNuevaGeneracionSegunda,double alfa,double rmin,double rmax){
        Random aleatorio = new Random();
        int c1,c2,c3,c4;
        h = new double[tam];
        int posAnt = 0;
        double num,costeMejorPrimero,costeMejorSegundo;
        double[] mejorPrimero, mejorSegundo;

        for (int i = 0; i < tampoblacion; i++) {
            c1 = aleatorio.nextInt((tampoblacion - 1 - 0) + 0);
            while (c1 == (c2 = aleatorio.nextInt(tampoblacion - 1 - 0) + 0)) ;

            if (costes[c1] < costes[c2]) {
                mejorPrimero = nuevaGeneracion.get(c1);
                costeMejorPrimero = costes[c1];
            } else {
                mejorPrimero = nuevaGeneracion.get(c2);
                costeMejorPrimero = costes[c2];
            }

            while (posAnt == (c3 = aleatorio.nextInt(tampoblacion - 1 - 0) + 0)) ;
            while (posAnt == (c4 = aleatorio.nextInt(tampoblacion - 1 - 0) + 0)) ;


            if (costes[c3] < costes[c4]) {
                mejorSegundo = nuevaGeneracion.get(c3);
                costeMejorSegundo = costes[c3];
            } else {
                mejorSegundo = nuevaGeneracion.get(c4);
                costeMejorSegundo = costes[c4];
            }

            num = aleatorio.nextDouble();

            if (num < probabilidadCruce) {
                cruceBlX(tam, mejorPrimero, mejorSegundo, alfa, h, rmin, rmax);
                nuevaGeneracion.add(i, h);
                marcados[i] = true;
            } else {
                nuevaGeneracion.add(i, mejorPrimero);
                costeNuevaGeneracionSegunda[i] = costeMejorPrimero;
            }
        }
        nuevaGeneracion = nuevaGeneracionSegunda;
        costeNuevaGeneracion = costeNuevaGeneracionSegunda;
    }

    public static void mutar(int tampoblacion, int tam, double probabilidadMutacion, double rmin, List<double[]> nuevaGeneracion, boolean[] marcados){
        Random random = new Random();
        for (int i = 0; i < tampoblacion; i++) {
            boolean m = false;
            for (int j = 0; j < tam; j++) {
                double x = random.nextDouble();
                if (x < probabilidadMutacion) {
                    m = true;
                    double valor = random.nextDouble() + rmin;
                    Mutacion(nuevaGeneracion.get(i),j,valor);
                }
            }
            if (m)
                marcados[i] = true;
        }
    }

    public static void calculaMejorPeor(int tampoblacion,boolean[] marcados, double[] costeNuevaGeneracion,List<double[]> nuevaGeneracion,String funcion,int contador,double peorCosteHijo,int peor,double mejorcostehijo, int mejorCruceHijo){
        for (int i = 0; i < tampoblacion; i++) {
            if (marcados[i]) {
                costeNuevaGeneracion[i] = evaluaCoste(nuevaGeneracion.get(i),funcion);
                contador++;
            }
            if (costeNuevaGeneracion[i] < mejorcostehijo) {
                mejorcostehijo = costeNuevaGeneracion[i];
                mejorCruceHijo = i;
            }
        }
    }

    public static void actualizarMejorCromosoma(double mejorcostehijo,double mejorCosteGlobal,double[] mejorCroGlobal,List<double[]> nuevaGeneracion,int mejorCruceHijo){
        if (mejorcostehijo < mejorCosteGlobal) {
            mejorCosteGlobal = mejorcostehijo;
            mejorCroGlobal = nuevaGeneracion.get(mejorCruceHijo);
        }
    }

    public static void mostrarmatriz(double[] mat){
        for (int i=0; i<50; i++){ //cout << mat[i].size() << endl;
            for (int j=0; j<10; j++){
                System.out.println("," + mat);
            }
        }
    }
}