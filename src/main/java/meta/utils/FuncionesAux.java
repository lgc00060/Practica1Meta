package meta.utils;

import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Collections.swap;
import static meta.algoritmo.AEvBLXalfa_Clase3_Grupo5.*;

public class FuncionesAux {
    public static void cargaAleatoria(int tam,double[] v, double rmin, double rmax ){
        for (int i=0; i<tam; i++){
            Random random = new Random();
            v[i] = random.nextDouble();
        }
    }

    public static void Mutacion(double[] v, int pos, double valor){
        v[pos]=valor;

    }
   public static void cruceMedia(int tam,double[] v,double[] w,double[] h){
//    vector<double> h1,h2;
//    h1.resize(tam);
        //     h.resize(tam);
        for (int i=0; i<tam; i++){
            h[i]= (v[i]+w[i])/2;
//        h2[i]= (v[i]+tercero[i])/2;
        }
//    v=h1;
//    w=h2;
    }

   public static void cruceBLX(int tam, double[] v, double[] w, double alfaBLX, double[] h1, double[] h2){
        double Cmax,Cmin,I;
        h1.resize(tam);
        h2.resize(tam);
        for (int i=0; i<tam; i++){
            Cmax=max(v[i],w[i]);
            Cmin=min(v[i],w[i]);
            if (Cmin<0 && Cmax<0)
                swap(Cmin, Cmax);
            I=Cmax-Cmin;
            h1[i]= Randfloat(Cmin-(I*alfaBLX),Cmax+(I*alfaBLX));
            h2[i]= Randfloat(Cmin-(I*alfaBLX),Cmax+(I*alfaBLX));
        }
    }

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

    public static void mostrarmatriz(double[] mat){
        for (int i=0; i<50; i++){ //cout << mat[i].size() << endl;
            for (int j=0; j<10; j++){
                System.out.println("," + mat);
            }
        }

    }
}
