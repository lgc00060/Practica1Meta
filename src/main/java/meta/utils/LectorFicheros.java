package meta.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LectorFicheros {
    private double tsp[][];
    private double matrizDistancias[][];
    private int tamanio;

    public void LectorDatos(String ruta) {
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
            this.tamanio = Integer.parseInt(tam[2]);
            tsp = new double[this.tamanio][2];
            matrizDistancias = new double[this.tamanio][this.tamanio];
            b.readLine();
            b.readLine();
            int c = 0;

            while(c < this.tamanio) {
                linea = b.readLine();
                String[] separador = linea.split(" ");
                tsp[c][0] = Double.parseDouble(separador[1]);
                tsp[c][1] = Double.parseDouble(separador[2]);
                c++;
            }

            for(int i = 0; i <= this.tamanio; i++) {
                for(int j = i + 1; j < this.tamanio; j++) {
                    matrizDistancias[i][j] = Math.sqrt(Math.pow(tsp[i][0] - tsp[j][0], 2) + Math.pow(tsp[i][1] - tsp[j][1], 2));
                    matrizDistancias[j][i] = matrizDistancias[i][j];
                }
            }
        } catch(IOException IOE) {
            System.out.println(IOE);
        }
    }

    public void mostrarDatos() {
        for(int i = 0; i < tamanio; i++) {
            for(int j = 0; j < 2; j++) {
                System.out.print(tsp[i][j] + ", ");
            }
            System.out.println();
        }

        System.out.println();

        for(int i = 0; i < tamanio; i++) {
            for(int j = 0; j < tamanio; j++) {
                System.out.print(matrizDistancias[i][j] + ", ");
            }
            System.out.println();
        }
    }

    public double[][] getTsp() {

        return tsp;
    }

    public double[][] getMatrizDistancias() {

        return matrizDistancias;
    }

    public int getTamanio() {
        return tamanio;
    }

}
