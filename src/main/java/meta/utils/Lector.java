package meta.utils;

import java.util.ArrayList;
import java.io.*;
import java.io.FileReader;


public class Lector {

    int tamSem = 5;
    private Long[] semilla = new Long[tamSem];

    private int tam;

    private float q0;

    public int greedy;

    private float p;

    private float fi;

    private ArrayList<String> algoritmos = new ArrayList<>();

    private int k;

    private int poblacion;

    private int beta;

    private float alfa;

    private int evaluaciones;

    private ArrayList<String> funciones = new ArrayList<>();

    private double[] rangoInf = new double[12];

    private double[] rangoSup = new double[12];

    //Simbolo de separacion
    private String simbolo = "=";

    //Parametros de cada algoritmo
    //Integer parametroextra;
    //Constructor
    public Lector(String ruta) {
        //Parametros necesarios para la lectura de los archivos
        String linea;
        FileReader f = null;

        //Leemos el fichero
        try {
            //Inicializamos los parametros necesarios
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);

            //Comenzamos a leer
            while ((linea = b.readLine()) != null) {
                //Separamos los nombres de los atributos de sus asignaciones
                String[] split = linea.split("=");

                //Evaluamos el nombre del atributo y entra en su correspondiente caso
                switch (split[0]) {
                    case "semilla":
                        //Volvemos a separar la asginacion de semillas, porque puede haber mas de una semilla
                        String[] splitsemi = split[1].split(" ");
                        for (int q = 0; q < splitsemi.length; q++) {
                            //Los añadimos al ArrayList
                            semilla[q] = Long.parseLong(splitsemi[q]);
                            // System.out.println("semilla:" + semilla[q]);
                        }
                        break;

                    case "algoritmos":
                        String[] splittalgg = split[1].split(" ");
                        for (int q = 0; q < splittalgg.length; q++) {
                            algoritmos.add(splittalgg[q]);
                        }
                        break;

                    case "tam":
                        //Los añadimos al atributo
                        tam = Integer.parseInt(split[1]);
                        break;

                    case "evaluaciones":
                        evaluaciones = Integer.parseInt(split[1]);
                        break;

                    case "poblacion":
                        poblacion = Integer.parseInt(split[1]);
                        // System.out.println("k:" + k);
                        break;

                    case "greedy":
                        greedy = Integer.parseInt(split[1]);
                        break;

                    case "alfa":
                        alfa = Float.parseFloat(split[1]);
                        break;

                    case "beta":
                        beta = Integer.parseInt(split[1]);
                        // System.out.println("k:" + k);
                        break;

                    case "q0":
                        q0 = Float.parseFloat(split[1]);
                        break;

                    case "p":
                        p = Float.parseFloat(split[1]);
                        break;

                    case "fi":
                        fi = Float.parseFloat(split[1]);
                        break;

                }//cierra switch
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }


    /**
     * @return Arraylist de las semillas que deberán utilizarse en la ejecución
     * de los algoritmos que necesiten esas semillas
     */
    public Long[] getSemilla() {
        return semilla;
    }

    public ArrayList<String> getAlgoritmos() {
        return algoritmos;
    }
    public int getTam() {
        return tam;
    }

    public int getEvaluaciones() {
        return evaluaciones;
    }

    public int getPoblacion() {return poblacion;}

    public int getGreedy() {
        return greedy;
    }

    public float getAlfa() {return alfa;}

    public int getBeta(){
        return beta;
    }

    public float getQ0(){
        return q0;
    }

    public float getP() {
        return p;
    }

    public float getFi() {
        return fi;
    }
}