package meta.utils;

import java.util.ArrayList;
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

    public static void cruceMedia(int tam, double[] v, double[] w, double[] h) {
//   vector<double> h1,h2;
//    h1.resize(tam);
        //     h.resize(tam);
        for (int i = 0; i < tam; i++) {
            h[i] = (v[i] + w[i]) / 2;
//        h2[i]= (v[i]+tercero[i])/2;
        }
//    v=h1;
//    w=h2;
    }


/*
    public static boolean negativos(double[] v){
        for (long k=0; k<v.length(); k++){
            if (v[k]<-500 || v[k]>500)
                return true;
        }
        return false;
    }

    public static void mostrarVector(double[] v){
        for (long k=0; k<v.lenght(); k++){
            System.out.println("," + v);
        }
    }

 */
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

    public static void cargaCromosomasIniciales(int tp, List<double[]> cromosomas, double rmin, double rmax, String funcion, double[] costes, int tam, double mejorCoste, double[] mejorCruce){
        for (int i = 0; i < tp; i++) {
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

    public static void cruce(int tampoblacion,List<double[]> nuevaGeneracion,boolean[] marcados,double probabilidadCruce,int tam){
        Random aleatorio = new Random();
        double x=0;
        int p1;
        double[] primerosHijos = new double[tam];
        double[] segundosHijos = new double[tam];
        for (int i = 0; i < tampoblacion; i++) {
            x = aleatorio.nextDouble();
            if (x < probabilidadCruce) {
                while (i == (p1 = aleatorio.nextInt(tampoblacion - 1)));
                nuevaGeneracion.add(i, primerosHijos);
                nuevaGeneracion.add(p1, segundosHijos);
                marcados[i] = marcados[p1] = true;
            }
        }
    }

    public static void mutar(int tampoblacion,int tam,double probabilidadMutacion,double rmin,List<double[]> nuevaGeneracion,boolean[] marcados){
        Random aleatorio = new Random();
        for (int i = 0; i < tampoblacion; i++) {
            boolean m = false;
            for (int j = 0; j < tam; j++) {
                double x = aleatorio.nextDouble();
                if (x < probabilidadMutacion) {
                    m = true;
                    double valor = aleatorio.nextDouble() + rmin;
                    Mutacion(nuevaGeneracion.get(i), j, valor);//cout << "mutando Cromosoma.." << endl;
                }
            }
            if (m)
                marcados[i] = true;        //marcamos los modificados
        }
    }

    public static void calculaMejorPeor(int tampoblacion,boolean[] marcados, double[] costeNuevaGeneracion,List<double[]> nuevaGeneracion,String funcion,int contador,double peorCosteHijo,int peor,double mejorcostehijo, int mejorCruceHijo){
        for (int i = 0; i < tampoblacion; i++) {
            if (marcados[i]) {
                costeNuevaGeneracion[i] = evaluaCoste(nuevaGeneracion.get(i),funcion);
                contador++;
            }

            if (costeNuevaGeneracion[i] > peorCosteHijo) {
                peorCosteHijo = costeNuevaGeneracion[i];
                peor = i;
            }

            if (costeNuevaGeneracion[i] < mejorcostehijo) {
                mejorcostehijo = costeNuevaGeneracion[i];
                mejorCruceHijo = i;
            }
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


