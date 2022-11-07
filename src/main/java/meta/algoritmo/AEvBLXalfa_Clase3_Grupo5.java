package meta.algoritmo;
import java.security.SecureRandom;

import static meta.funciones.Funciones.evaluaCoste;

public class AEvBLXalfa_Clase3_Grupo5 {
   public static <Poblacion> double AEVBLXALFA(int tp, int tampoblacion, int evaluaciones, double[] s, double rmin, double rmax,
                                               double kProbMuta, double kProbCruce, double alfa, String selector, double tiempo_ejec, int semilla, int individuos, double datos, int poblacion){


       //Iniciamos el tiempo de ejecucion
       tiempo_ejec = System.currentTimeMillis();

       //Inicializamos el generador de numeros aleatorios con la semilla
       SecureRandom r = new SecureRandom();
       r.setSeed(semilla);
/*
       //Creamos la poblacion inicial con numero aleatorios
       poblacion = new Poblacion(tampoblacion, individuos, datos.length());
       poblacion.inialeatoriamente(datos, r);

 */

       //Calculamos sus costes y lo ordenamos de mayor a menor
       //poblacion.evaluaCoste(datos);

       //Tiempo de ejecucion del algoritmo
       int t=0;
        double[][] cromosomas = new double[10];
        double[][] nuevag = new double[10];
       double[] costes = new double[10];
       double[] costesH = new double[10];
       double[] position = new double[10];
       double[] mejorCruce = new double[10];
       int peor,peorCoHijo;
       int mejorCrHijo;
       double mejorCo=9999999e+100;
       double mejorCoHijo;
       double mejorCosteGlobal=mejorCo;
       double[] mejorCruce1 = mejorCruce;
       double[] mejorCroGlobal= new double[] mejorCruce;

//Carga de los cromosomas iniciales
       for (int i=0;i<tp;i++){
           cargaAleatoria(tampoblacion, cromosomas[i], rmin, rmax);
           costes[i]=evaluaCoste(cromosomas[i],selector);

           if (costes[i]<mejorCo){
               mejorCo=costes[i];
               mejorCruce=cromosomas[i];
           }
       }


       //Calculo de la probabilidad de mutacion
       float probMutacion= (float) kProbMuta;

       //Contador de evaluaciones de la poblacion
       int conte=tp;

       //PRINCIPAL: Comienzan las iteraciones

       while (conte<evaluaciones){
           t++;

           //SELECCION por TORNEO: Calculo de los cromosomas mas prometedores entre cada 2 parejas aleatorias
           //durante tp enfrentamientos
           for (int k=0,i,j;k<tp;k++){
               i=Randint(0,tp-1);
               while (i==(j=Randint(0,tp-1)));
               posi[k]=(costes[i]<costes[j])?i:j;
           }

           //Nos quedamos con los cromosomas mas prometedores
           for (int i=0;i<tp;i++){
               nuevag[i]=cromosomas[position[i]];
               costesH[i]=costes[position[i]];
           }

           //CRUZAMOS los padres seleccionados con una probabilidad probCruce OPCION 1
           float x;
           int p1;
          double[] h1 = new double [];
          double[] h2 = new double [];
          boolean[] marcados(tp,false);  //marcamos los modificados
           for (int i=0;i<tp;i++){
               x=Randfloat(0,1.01);
               if (x<kProbCruce){
                   while (i==(p1=Randint(0,tp-1)));

                   cruceBLX(tam,nuevag[i],nuevag[p1],alfa,h1,h2);

                   nuevag[i]=h1;
                   nuevag[p1]=h2;
                   marcados[i]=marcados[p1]=true;

               }
           }

           //CRUZAMOS los padres seleccionados con una probabilidad probCruce OPCION 2
           //    PDTEEEEEEEEEEEEEEEEEEEE
           int c1,c2;
           double[][] nuevagg=nuevag; //la copiamos para obtener los nuevos hijos
           //std::vector<double> h1;

           for (int i=0;i<tp;i++){
               int c1=Randint(0,tp-1);

               x=Randfloat(0,1.01);
               if (x<kProbCruce){
                   while (c1==(c2=Randint(0,tp-1)));
                   cruceBLX(tampoblacion,nuevag[i],nuevag[p1],alfa,h1,h2);
                   nuevagg[c1]=h1;
                   nuevagg[c2]=h2;
                   marcados[c1]=marcados[c2]=true;

               }
           }
           nuevag=nuevagg;  //la dejamos en nuevag para proseguir

           //MUTAMOS los genes de los dos padres ya cruzados con probabilidad probMutacion
           for (int i=0;i<tp;i++){
               boolean m=false;
               for (int j=0;j<tampoblacion;j++){
                   x=Randfloat(0,1.01);
                   if (x<probMutacion){
                       m=true;
                       double valor = Randfloat(rmin, rmax);
                       Mutacion(nuevag[i],j,valor);//cout << "mutando Cromosoma.." << endl;
                   }
               }
               if (m)
                   marcados[i]=true;        //marcamos los modificados
           }

           //actualizamos el coste de los modificados
           // preparamos el REEMPLAZAMIENTO calculamos el peor de la nueva poblacion
           peorCoHijo=0;
           int peor;
           double mejorCoHijo= 99999999e+100;
           for (int i=0;i<tp;i++){
               if (marcados[i]) {
                   costesH[i]=evaluaCoste(nuevag[i],selector);
                   conte++;
               }

               if (costesH[i]>peorCoHijo){
                   peorCoHijo= (int) costesH[i];
                   peor=i;
               }

               if (costesH[i]<mejorCoHijo){
                   mejorCoHijo=costesH[i];
                   mejorCrHijo=i;
               }

           }

           //Mantenemos el elitismo del mejor de P(t) para P(t') si no sobrevive
           boolean enc=false;
           for (int i=0; i<nuevag.length() && !enc; i++){
               if (mejorCruce==nuevag[i]){
                   enc=true;
               }
           }
           if (!enc){
               nuevag[peor]=mejorCruce;
               costesH[peor]=mejorCo;
           }

           //actualizamos el mejor cromosoma para el elitismo de la siguiente generacion
           mejorCruce =nuevag[mejorCrHijo];
           mejorCo=mejorCoHijo;

           //Actualizamos el mejor global y su coste con el mejor hijo de la NUEVA POBLACION
           //si mejora
           if (mejorCoHijo<mejorCosteGlobal){
               mejorCosteGlobal=mejorCoHijo;
               mejorCroGlobal=nuevag[mejorCrHijo];
           }

           //        cout << "Mejor Coste: " << mejorCosteGlobal << endl;
           //Actualizo cromosomas con nuevag, para la siguiente generacion
           costes=costesH;
           cromosomas=nuevag;
       }
       s=mejorCroGlobal;
       System.out.println("Total Evaluaciones:"+ conte);
       System.out.println("Total Iteraciones:" +t);
       return mejorCosteGlobal;
   }



}
