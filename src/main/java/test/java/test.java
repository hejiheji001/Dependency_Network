package test.java;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 * Created by FireAwayH on 17/08/2016.
 */
public class test {

    public static void main(String[] args) {
        double[] t1 = {1, 4, 7};
        double[] t2 = {2, 5, 8};
        double ab = 0.9934207315819038;
        double ac = 0.6129736037519814;
        double bc = 0.5184565985783233;

        PearsonsCorrelation p = new PearsonsCorrelation();
        System.out.println(PartialCorrelation(ab, ac, bc));
    }

    public static double PartialCorrelation(double X_Y, double X_Z, double Y_Z){
        return  X_Y - (X_Y - X_Z * Y_Z) / Math.sqrt((1 - Math.pow(X_Z, 2)) * (1 - Math.pow(Y_Z, 2)));
    }
}
