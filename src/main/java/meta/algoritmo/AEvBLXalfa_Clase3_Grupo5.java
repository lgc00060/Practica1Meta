package meta.algoritmo;

import static meta.funciones.Funciones.evaluaCoste;
import static java.util.Random.*;

public class AEvBLXalfa_Clase3_Grupo5 {
    double AEVBLXALFA(int tp, int tam, int evaluaciones, double[] s, double rmin, double rmax,
                      double kProbMuta, double kProbCruce, double alfa, int selector) {
        int t = 0;
        vector<vector<double>> cromosomas, nuevag;
        vector<double> costes, costesH;
        vector<double> posi, mejorCr;
        int peor;
        int peorCoHijo;
        int mejorCrHijo;
        double mejorCo = 9999999e+100,
        double mejorCoHijo;

        //  reservamos del tamaño del vector de cromosomas original, nueva gen., costes y posic.
        cromosomas.resize(tp, vector < double>(tam));
        nuevag.resize(tp, vector < double>(tam));
        costes.resize(tp);
        costesH.resize(tp);
        posi.resize(tp);

        //Carga de los cromosomas iniciales
        for (int i = 0; i < tp; i++) {
            cargaAleatoria(tam, cromosomas[i], rmin, rmax);
            costes[i] = evaluaCoste(cromosomas[i], selector);

            if (costes[i] < mejorCo) {
                mejorCo = costes[i];
                mejorCr = cromosomas[i];
            }
        }

        double mejorCosteGlobal = mejorCo;
        vector<double> mejorCroGlobal = mejorCr;

        //Calculo de la probabilidad de mutacion
        float probMutacion = kProbMuta;

        //Contador de evaluaciones de la poblacion
        int conte = tp;

        //PRINCIPAL: Comienzan las iteraciones

        while (conte < evaluaciones) {
            t++;

            //SELECCION por TORNEO: Calculo de los cromosomas mas prometedores entre cada 2 parejas aleatorias
            //durante tp enfrentamientos
            for (int k = 0, i, j; k < tp; k++) {
                i = Randint(0, tp - 1);
                while (i == (j = Randint(0, tp - 1))) ;
                posi[k] = (costes[i] < costes[j]) ? i : j;
            }

            //Nos quedamos con los cromosomas mas prometedores
            for (int i = 0; i < tp; i++) {
                nuevag[i] = cromosomas[posi[i]];
                costesH[i] = costes[posi[i]];
            }

            //CRUZAMOS los padres seleccionados con una probabilidad probCruce OPCION 1
            float x;
            int p1;
            vector < double>h1, h2;
            vector < boolean > marcados(tp, false);  //marcamos los modificados
            for (int i = 0; i < tp; i++) {
                x = Randfloat(0, 1.01);
                if (x < kProbCruce) {
                    while (i == (p1 = Randint(0, tp - 1))) ;

                    cruceBLX(tam, nuevag[i], nuevag[p1], alfa, h1, h2);

                    nuevag[i] = h1;
                    nuevag[p1] = h2;
                    marcados[i] = marcados[p1] = true;

                }
            }

            //CRUZAMOS los padres seleccionados con una probabilidad probCruce OPCION 2
            //    PDTEEEEEEEEEEEEEEEEEEEE
            x;
            int c1, c2;
            vector<vector<double>> nuevagg = nuevag; //la copiamos para obtener los nuevos hijos
            //std::vector<double> h1;

            for (int i = 0; i < tp; i++) {
                int c1 = Randint(0, tp - 1);

                x = Randfloat(0, 1.01);
                if (x < kProbCruce) {
                    while (c1 == (c2 = Randint(0, tp - 1))) ;
                    cruceBLX(tam, nuevag[i], nuevag[p1], alfa, h1, h2);
                    nuevagg[c1] = h1;
                    nuevagg[c2] = h2;
                    marcados[c1] = marcados[c2] = true;

                }
            }
            nuevag = nuevagg;  //la dejamos en nuevag para proseguir

            //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probMutacion
            for (int i = 0; i < tp; i++) {
                boolean m = false;
                for (int j = 0; j < tam; j++) {
                    x = Randfloat(0, 1.01);
                    if (x < probMutacion) {
                        m = true;
                        double valor = Randfloat(rmin, rmax);
                        Mutacion(nuevag[i], j, valor);//cout << "mutando Cromosoma.." << endl;
                    }
                }
                if (m)
                    marcados[i] = true;        //marcamos los modificados
            }

            //actualizamos el coste de los modificados
            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion
            peorCoHijo = 0;
            int peor;
            mejorCoHijo = 99999999e+100;
            for (int i = 0; i < tp; i++) {
                if (marcados[i]) {
                    costesH[i] = evaluaCoste(nuevag[i], selector);
                    conte++;
                }

                if (costesH[i] > peorCoHijo) {
                    peorCoHijo = costesH[i];
                    peor = i;
                }

                if (costesH[i] < mejorCoHijo) {
                    mejorCoHijo = costesH[i];
                    mejorCrHijo = i;
                }

            }

            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
            boolean enc = false;
            for (int i = 0; i < nuevag.size() && !enc; i++) {
                if (mejorCr == nuevag[i]) {
                    enc = true;
                }
            }
            if (!enc) {
                nuevag[peor] = mejorCr;
                costesH[peor] = mejorCo;
            }

            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCr = nuevag[mejorCrHijo];
            mejorCo = mejorCoHijo;

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
            //si mejora
            if (mejorCoHijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCoHijo;
                mejorCroGlobal = nuevag[mejorCrHijo];
            }

            //        cout << "Mejor Coste: " << mejorCosteGlobal << endl;
            //Actualizo cromosomas con nuevag, para la siguiente generacion
            costes = costesH;
            cromosomas = nuevag;
        }
        s = mejorCroGlobal;
        System.out.println("Total Evaluaciones:" + conte);
        System.out.println(" Total Iteraciones:" + t);
        return mejorCosteGlobal;
    }

}