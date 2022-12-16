package meta.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class leerFicheros {
    private double tsp[][];
    private double distancia[][];
    private int tam;

    public void leeDatos(String ruta) {
        String linea;
        FileReader f = null;
        try {
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            b.readLine();
            b.readLine();
            b.readLine();
            linea = b.readLine();
            String tam[] = linea.split(" ");
            this.tam = Integer.parseInt(tam[2]);
            tsp = new double[this.tam][2];
            distancia = new double[this.tam][this.tam];
            b.readLine();
            b.readLine();
            int c = 0;
            while(c < this.tam) {
                linea = b.readLine();
                String[] separador = linea.split(" ");
                tsp[c][0] = Double.parseDouble(separador[1]);
                tsp[c][1] = Double.parseDouble(separador[2]);
                c++;
            }

            for(int i = 0; i <= this.tam; i++) {
                for(int j = i + 1; j < this.tam; j++) {
                    distancia[i][j] = Math.sqrt(Math.pow(tsp[i][0] - tsp[j][0], 2) + Math.pow(tsp[i][1] - tsp[j][1], 2));
                    distancia[j][i] = distancia[i][j];
                }
            }
        } catch(IOException IOE) {
            System.out.println(IOE);
        }
    }

    public void mostrarDatos() {
        for(int i = 0; i < tam; i++) {
            for(int j = 0; j < 2; j++) {
                System.out.print(tsp[i][j] + ", ");
            }
            System.out.println();
        }

        System.out.println();

        for(int i = 0; i < tam; i++) {
            for(int j = 0; j < tam; j++) {
                System.out.print(distancia[i][j] + ", ");
            }
            System.out.println();
        }
    }

    public double[][] getTsp() {
        return tsp;
    }

    public double[][] getDistancia() {
        return distancia;
    }

    public int getTam() {
        return tam;
    }

}
