package meta.algoritmo;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Random;
import static meta.funciones.Funciones.evaluaCoste;
import static meta.utils.FuncionesAux.*;

/*public class AED_Clase3_Grupo5 {
    double AED(int tp, int tam,int evaluaciones,double[] s, double rmin, double rmax, int selector){

        int t=0;
        List<double[]> cromosomas = new ArrayList<>();
        double[] costes = new double[tp];
        double mejorCoste = Double.MAX_VALUE;
        double[] mejorCruce = new double[tp];
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCruceGlobbal=mejorCruce;
        //Contador de evaluaciones de la poblacion
        int conte=tp;

        //Carga de los cromosomas iniciales
        for (int i = 0; i < tp; i++) {
            cargaAleatoria(tam, cromosomas.get(i), rmin, rmax);
            costes[i] = evaluaCoste(cromosomas.get(i), String.valueOf(selector));

            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCruce = cromosomas.get(i);
            }
        }

        //    mostrarVector(mejorCroGlobal);
        System.out.println("Coste" + mejorCosteGlobal);


        //PRINCIPAL: Comienzan las iteraciones

        while (conte<evaluaciones){
            t++;
            mejorCoste=9999999e+100;

            //CRUZAMOS con operador de recombinacion ternaria

            std::vector<double> ale1, ale2, obj, nuevo, padre;
            nuevo.resize(tam);
            int a1,a2,k1,k2,k3;

            for (int i=0;i<tp;i++){
                //un padre
                padre=cromosomas[i];

                //2 aleatorios distintos entre si y del padre
                do{
                    a1=Randint(0,tp-1);
                    while (a1==(a2=Randint(0,tp-1)));
                }while (a1!=i && a2!=i);

                if (a1>=tp)  //UUFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF cuando falla el aleatorio
                    a1=tp-1;

                ale1=cromosomas[a1];
                ale2=cromosomas[a2];

                //un objetivo elegido entre k=3(distintos) y distintos a a1, a2 y el padre
                do{
                    k1=Randint(0,tp-1);
                    while (k1==(k2=Randint(0,tp-1)));
                    // k2=Randint(0,tp-1);
                    while (k1==(k2==(k3=Randint(0,tp-1))));
                    //k3=Randint(0,tp-1);
                }while (k1!=i && k1!=a1 && k1!=a2 &&
                        k2!=i && k2!=a1 && k2!=a2 &&
                        k3!=i && k3!=a1 && k3!=a2);

                if (costes[k1]<costes[k2] && costes[k1]<costes[k3])
                    obj=cromosomas[k1];
                else
                if (costes[k2]<costes[k1] && costes[k2]<costes[k3])
                    obj=cromosomas[k2];
                else
                    obj=cromosomas[k3];

                double F=Randfloat(0,1.01);  //Factor de mutacion diferente por cada elemento de la poblacion

                for (int j=0; j<tam; j++){
                    double R=Randfloat(0,1.01);  //% de elección de dimensiones entre el nuevo y el objetivo
                    // por cada dimension(posicion) del individuo
                    if (R>0.5)
                        nuevo[j]=obj[j];
                    else{
                        nuevo[j]= padre[j]+ (F*(ale1[j]-ale2[j])); //operador de recombinacion a posteriori de la mutacion
                        //para cuando se salen los valores del rango de la funcion
                        if (nuevo[j]>rmax)
                            nuevo[j]=rmax;
                        else
                        if (nuevo[j]<rmin)
                            nuevo[j]=rmin;
                    }
                }

                //REEMPLAZAMIENTO si el hijo es mejor q el padre
                double costeNuevo=CalculaCoste(nuevo,selector);
                conte++;
                if (costeNuevo<costes[i]){
                    cromosomas[i]=nuevo;
                    costes[i]=costeNuevo;
                    if (costeNuevo<mejorCo){
                        mejorCo=costeNuevo;
                        mejorCr=nuevo;
                    }
                }
            }

            //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
            //si mejora
            if (mejorCo<mejorCosteGlobal){
                mejorCosteGlobal=mejorCo;
                mejorCroGlobal=mejorCr;
            }
        }

        s=mejorCroGlobal;
        System.out.println("Total evaluaciones" + conte);
        System.out.println("Total Iteraciones" +t);
        return mejorCosteGlobal;

    }




}
*/