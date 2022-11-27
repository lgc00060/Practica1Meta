package meta.utils;

import java.util.ArrayList;
import java.io.*;
import java.io.FileReader;


public class Lector {

    int tamSem = 5;
    private Long[] semilla = new Long[tamSem];

    private int d;

    private ArrayList<String> algoritmos = new ArrayList<>();

    private int k;

    private int poblacion;

    private float prob_cambio;

    private float cruce;

    private float mutacion;

    private float alfa;

    private long iteraciones;

    private ArrayList<String> funciones = new ArrayList<>();

    private double[] rangoInf = new double[10];

    private double[] rangoSup = new double[10];

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

                    case "d":
                        //Los añadimos al atributo
                        d = Integer.parseInt(split[1]);
                        break;

                    case "algoritmos":
                        String[] splittalgg = split[1].split(" ");
                        for (int q = 0; q < splittalgg.length; q++) {
                            algoritmos.add(splittalgg[q]);
                        }
                        break;

                    case "k":
                        k = Integer.parseInt(split[1]);
                        break;


                    case "iteraciones":
                        iteraciones = Long.parseLong(split[1]);
                        break;

                    case "prob_cambio":
                        prob_cambio = Float.parseFloat(split[1]);
                        break;

                    case "funciones":
                        String[] splitfun = split[1].split(", ");
                        for (int q = 0; q < splitfun.length; q++) {
                            funciones.add(splitfun[q]);
                        }
                        break;

                    case "rangoInf":
                        String[] splitrangI = split[1].split(" ");
                        for (int q = 0; q < splitrangI.length; q++) {
                            rangoInf[q] = Double.parseDouble(splitrangI[q]);
                        }
                        break;

                    case "rangoSup":
                        String[] splitrangS = split[1].split(" ");
                        for (int q = 0; q < splitrangS.length; q++) {
                            if (splitrangS[q].equals("PI")) {
                                rangoSup[q] = Math.PI;
                            } else {
                                rangoSup[q] = Double.parseDouble(splitrangS[q]);
                                ;
                            }
                        }
                        break;

                    case "poblacion":
                        poblacion = Integer.parseInt(split[1]);
                        // System.out.println("k:" + k);
                        break;

                    case "cruce":
                        cruce = Float.parseFloat(split[1]);
                        break;

                    case "mutacion":
                        mutacion = Float.parseFloat(split[1]);
                        break;

                    case "alfa":
                        alfa = Float.parseFloat(split[1]);
                        break;
                }
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

    public int getD() {
        return d;
    }

    /**
     * @return ArrayList con el nombre de los algoritmos que se deberán ejecutar
     */

    public ArrayList<String> getAlgoritmos() {
        return algoritmos;
    }

    public int getK() {
        return k;
    }

    public float getProb() {
        return prob_cambio;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public long getIteraciones() {
        return iteraciones;
    }

    public ArrayList<String> getFunciones() {
        return funciones;
    }

    public double[] getRangoInf() {
        return rangoInf;
    }

    public double[] getRangoSup() {
        return rangoSup;
    }

    public int getPoblacion() {return poblacion;}

    public float getCruce() {return cruce;}

    public float getAlfa() {return alfa;}

    public float getMutacion() {return mutacion;}
}