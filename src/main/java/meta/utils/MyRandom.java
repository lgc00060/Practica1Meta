package meta.utils;

import java.util.Random;

public class MyRandom {
    public static double randDoubleWithRange(double min, double max){
        Random random = new Random();
        return random.nextDouble() * (max - min) + min;
    }
}
