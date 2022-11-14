package meta.utils;

import java.util.Random;

import static java.lang.Math.*;
import static java.util.Collections.swap;
import static meta.algoritmo.AEvBLXalfa_Clase3_Grupo5.*;

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


    public static void mostrarmatriz(double[] mat){
        for (int i=0; i<50; i++){ //cout << mat[i].size() << endl;
            for (int j=0; j<10; j++){
                System.out.println("," + mat);
            }
        }

    }
}


