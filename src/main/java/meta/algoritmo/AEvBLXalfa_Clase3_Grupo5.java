package meta.algoritmo;

import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.lang.Boolean;

public class AEvBLXalfa_Clase3_Grupo5 {
    double AEVBLXALFA(int tp, int tamPob, int evaluaciones, vector<double> s, double rmin, double rmax,
                      double kProbMuta, double kProbCruce, double alfa, int selector) {
        int t = 0;
        List<double[]> cromosomas=new ArrayList<>();
        List<double[]> nuevag=new ArrayList<>();
        double[] costes;
        double[] costesH;
        int posi;
        double[] mejorCr;
        int peor;
        int peorcostehijo;
        int mejorCruceHijo;
        double mejorCoste =Integer.MAX_VALUE;
        double mejorCosteHijo;
        Random random = new Random();
        //  reservamos del tamaño del vector de cromosomas original, nueva gen., costes y posic.




        //Carga de los cromosomas iniciales
        for (int i = 0; i < tp; i++) {
            cargaAleatoria(tamPob, cromosomas.get(i), rmin, rmax);
            costes[i] = evaluaCoste(cromosomas.get(i), String.valueOf(selector));

            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCr = cromosomas.get(i);
            }
        }

        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = mejorCr;

        //Calculo de la probabilidad de mutacion
        float probMutacion;

        //Contador de evaluaciones de la poblacion
        int conta = tp;

        //PRINCIPAL: Comienzan las iteraciones

        while (conta < evaluaciones) {
            t++;

            //SELECCION por TORNEO: Calculo de los cromosomas mas prometedores entre cada 2 parejas aleatorias
            //durante tp enfrentamiento¡'
            for (int k = 0, i, j; k < tp; k++) {
               int i =  random.nextInt();
                while (i == (j = random.nextInt()));
                 posi[k] = (costes[i] < costes[j]) ? i : j;
            }

            //Nos quedamos con los cromosomas mas prometedores
            for (int i = 0; i < tp; i++) {
                nuevag.set(i, cromosomas[posi[i]]);
                costesH[i] = costes[(int) posi[i]];
            }

            //CRUZAMOS los padres seleccionados con una probabilidad probCruce OPCION 1
            double x=new double[];
            int p1;
            double h1, h2;

            Boolean marcados=false;  //marcamos los modificados
            for (int i = 0; i < tp; i++) {
                double x = random.nextDouble();
                if (x < kProbCruce) {
                    while (i == (p1 = random.nextInt())) ;

                    cruceBLX(tamPob, nuevag[i], nuevag[p1], alfa, h1, h2);

                    nuevag.set(i, h1);
                    nuevag.set(p1, h2);
                    marcados[i] = marcados[p1] = true;

                }
            }

            //CRUZAMOS los padres seleccionados con una probabilidad probCruce OPCION 2
            //    PDTEEEEEEEEEEEEEEEEEEEE
            x;
            int c1;
            int c2;
           // vector<vector<double>> nuevagg = nuevag; //la copiamos para obtener los nuevos hijos
            //std::vector<double> h1;
            List<double[]> nuevagg=new ArrayList<>();

            for (int i = 0; i < tp; i++) {
                int c1 = random.nextInt();

               double x = random.nextDouble();
                if (x < kProbCruce) {
                    while (c1 == (c2 = random.nextInt())) ;
                    cruceBLX(tamPob, nuevag.get(i), nuevag.get(p1), alfa, h1, h2);
                    nuevagg.set(c1, h1);
                    nuevagg.set(c2, h2);
                    marcados[c1] = marcados[c2] = true;

                }
            }
            nuevag = Collections.singletonList(nuevagg);  //la dejamos en nuevag para proseguir

            //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probMutacion
            for (int i = 0; i < tp; i++) {
                boolean m = false;
                for (int j = 0; j < tamPob; j++) {
                   float x = random.nextFloat();
                    if (x < probMutacion) {
                        m = true;
                        double valor = random.nextDouble();
                        Mutacion(nuevag.get(i), j, valor);//cout << "mutando Cromosoma.." << endl;
                    }
                }
                if (m)
                    marcados[i] = true;        //marcamos los modificados
            }

            //actualizamos el coste de los modificados
            // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion
            peorcostehijo = 0;
            int peor;
            double mejorcostehijo = Integer.MAX_VALUE;
            for (int i = 0; i < tp; i++) {
                if (marcados[i]) {
                    costesH[i] = evaluaCoste(nuevag[i], selector);
                    conta++;
                }

                if (costesH[i] > peorcostehijo) {
                    peorcostehijo = costesH[i];
                    peor = i;
                }

                if (costesH[i] < mejorcostehijo) {
                    mejorcostehijo = costesH[i];
                    mejorCruceHijo = i;
                }

            }

            //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
            boolean enc = false;
            for (int i = 0; i < nuevag.lenght(); i++) {
                if (mejorCr == nuevag[i]) {
                    enc = true;
                }
            }
            if (!enc) {
                nuevag[peor] = mejorCr;
                costesH[peor] = mejorCoste;
            }

            //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
            mejorCr = nuevag[mejorCruceHijo];
            mejorCoste = mejorcostehijo;

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
            //si mejora
            if (mejorcostehijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorcostehijo;
                mejorCroGlobal = nuevag[mejorCruceHijo];
            }

            //        cout << "Mejor Coste: " << mejorCosteGlobal << endl;
            //Actualizo cromosomas con nuevag, para la siguiente generacion
            costes = costesH;
            cromosomas = nuevag;
        }
        double tiempoFinal = System.nanoTime();
        s = mejorCroGlobal;
        System.out.println("Total Evaluaciones:" + conta);
        System.out.println(" Total Iteraciones:" + t);
        return mejorCosteGlobal;
    }

}