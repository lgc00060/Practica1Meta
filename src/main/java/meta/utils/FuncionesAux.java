package meta.utils;

import java.util.Random;
import static meta.algoritmo.AEvBLXalfa_Clase3_Grupo5.*;

public class FuncionesAux {
    void cargaAleatoria(int tam,double[] v, double rmin, double rmax ){
        for (int i=0; i<tam; i++){
            Random random = new Random();
            v[i] = random.nextDouble();
        }
    }

    void Mutacion(double[] v, int pos, double valor){
        v[pos]=valor;

    }
    void cruceMedia(int tam, const vector<double> &v,const vector<double> &w, vector<double> &h){
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

    void cruceBLX(int tam, vector<double> v, vector<double> w, double alfaBLX, vector<double> &h1, vector<double> &h2){
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

    bool negativos(vector<double> v){
        for (long k=0; k<v.size(); k++){
            if (v[k]<-500 || v[k]>500)
                return true;
        }
        return false;
    }

    void mostrarVector(vector<double> v){
        for (long k=0; k<v.size(); k++){
            cout << "," << v[k];
        }
        cout << endl;
    }

    void mostrarmatriz(vector<vector<double> > mat){
        for (int i=0; i<50; i++){ //cout << mat[i].size() << endl;
            for (int j=0; j<10; j++){
                cout << "," << mat[i][j];
            }
            cout << endl;
        }

    }
}
