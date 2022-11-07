package meta.funciones;

public class Funciones {

    public static double ackleyFunction(double[] v) {


        int tamV= v.length;
        double c = 2* Math.PI,b = 0.2,a = 20,sum1 = 0,sum2 = 0;


        for (int i=0; i<tamV; i++){
            double xi = v[i];
            sum1 += xi*xi;
            sum2 += Math.cos(c*xi);
        }

        double term1 = -a * Math.exp(-b*Math.sqrt(sum1/tamV));
        double term2 = -Math.exp(sum2/tamV);

        double v1 = term1 + term2 + a + Math.exp(1);
        return v1;
    }

    public static double dixonFunction(double v[]){
        double sum1=0.0;
        int j=1;
        int tamV= v.length;

        for(int i=1;i<tamV;i++){
            sum1+=i*(Math.pow(Math.pow(2*v[i],2)-v[i-1],2));
        }

        return Math.pow(v[1]-1,2)+sum1;
    }


    public static double GriewankEvaluate(double[] x) {
        double part1 = 0;
        double part2 = 1;
        for (int i = 0; i < x.length; i++) {
            part1 += x[i] * 2 * 2;

            for (int j = 0; j < x.length; j++) {
                part2 *= Math.cos((x[i]) / Math.sqrt(i + 1));

            }
        }
        return (part1 / 4000.0) - part2 + 1;
    }

    public static double michaleFunction(double v[]){
        double result = 0.0;
        double op1;
        double op2;
        double d = v.length;
        int m = 10;
        for (int i = 0; i < d; i++) {
            op1 = Math.sin(v[i]);
            op2 = Math.sin(Math.pow(i * Math.pow(v[i], 2) / Math.PI, 2*m));
            result += op1 * op2;
        }

        result = (-1) * result;
        return result;
    }

    public static double permFunction(double v[]) {
        double s1=0.0,d=10,beta=10,s2;
        for (int i = 1; i < d; i++) {
            s2=0.0;
            for (int j = 1; j < d; j++) {
                s1 += (j+beta+1)*(Math.pow(v[j],i+1)-(1/Math.pow(j+1,i+1)));//Math.pow((j + 10) * Math.pow(v[j], i) - (1 / Math.pow(j, i)), 2);
            }
            s1+=Math.pow(s2,2);
        }
        return s1;
    }

    public static double rastFunction(double v[]){
        double sum1=0.0,result=0.0;
        int tamV= v.length;

        for(int i=0;i<tamV;i++){
            sum1+=Math.pow(v[i],2)-10*Math.cos(2*Math.PI*v[i]);
        }

        return 10*tamV+sum1;
    }

    public static double rotatedFunction(double v[]){
        double d = v.length;
        double out = 0;

        for (int i=0; i<d; i++){
            double in=0;
            for (int j=0; j<i; j++){
                double xj = v[j];
                in += xj*xj;
            }
            out += in;
        }

        return out;
    }

    public static double schewFunction(double v[]){
        double sum1=0.0,result=0.0;
        int tamV= v.length;

        for(int i=0;i<tamV;i++){
            sum1+=v[i]*Math.sin(Math.sqrt(Math.abs(v[i])));
        }

        return 418.9829*tamV-sum1;
    }

    public static double tridFunction(double v[]){
        double sum1=0.0;
        double sum2=0.0;
        double result=0.0;
        int tamVec= v.length;

        sum1=Math.pow(v[0]-1,2);

        for (int i=1; i<tamVec; i++){
            double xii = v[i];
            double xx = v[i-1];
            sum1 += Math.pow((xii-1),2);
            sum2 += xii*xx;
        }

        return sum1-sum2;
    }

    public static double rosenFunction(double[] v) {
        double score = 0.0;
        double sum1;
        double sum2;
        double d = v.length;
        for (int i = 0; i < d - 1; i++) {
            sum1 = 100 * Math.pow(v[i + 1] - Math.pow(v[i], 2), 2);
            sum2 = Math.pow(v[i] - 1, 2);
            score += (sum1 + sum2);
        }
        return score;
    }
    public static double evaluaCoste(double j, String funcion) {
        switch (funcion) {
            case "Ackley":
                return ackleyFunction(j);

            case "Dixon":
                return dixonFunction(j);

            case "griewank":
                return GriewankEvaluate(j);

            case "michale":
                return michaleFunction(j);

            case "Perm":
                return permFunction(j);

            case "Rast":
                return rastFunction(j);

            case "Rosen":
                return rotatedFunction(j);

            case "Rotated":
                return schewFunction(j);

            case "Schew":
                return tridFunction(j);

            case "Trid":
                return rosenFunction(j);

            default:
                return -1;

        }
    }

    public static void intercambia(double inf,double sup){
        double aux;
        aux=inf;
        inf=sup;
        sup=aux;
    }

    public static String formatoVector(double []v){
        StringBuilder vector = new StringBuilder();
        vector.append("[");
        for (int i = 0; i < v.length - 1; i++) {
            vector.append(v[i]).append(", ");
        }
        vector.append(v[v.length - 1]).append("]");
        return vector.toString();
    }

}
