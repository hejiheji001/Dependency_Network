package util;

import Entities.Entity_Test;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by FireAwayH on 07/07/2016.
 */
public class DataGenerator {

    // 1272792# P(Alm, Blt : Lmp)
    
    public static void main(String[] args){
        String X;
        String Y;
        String Z;
        String[] s = StockList.someStocks;
        int index = 0;
        double X_Y = 0;
        double X_Z = 0;
        double Y_Z = 0;
        double P = 0;

        for(int i = 0; i < s.length; i++){
            X = s[i];
            for (int k = 0; k < s.length; k++){
                Y = s[k];
                if(!X.equals(Y)){
                    X_Y = PearsonsR(X, Y);
                    for(int j = 0; j < s.length; j++) {
                        Z = s[j];
                        ++index;
                        if(!Z.equals(Y) && !Z.equals(X) && index > 0){
                            X_Z = PearsonsR(X, Z);
                            Y_Z = PearsonsR(Y, Z);
                            P = PartialCorrelation(X_Y, X_Z, Y_Z);
                            System.out.print(index + "# P(" + X + ", " + Y + " : " + Z + ") = ");
                            System.out.println(P);
                            saveToDB(X, Y, Z, P);
                        }
                    }
                }
            }
        }
    }

    private static void saveToDB(String x, String y, String z, double p) {
//        Entity_PartialCorrelation ep = new Entity_PartialCorrelation();
        Entity_Test ep = new Entity_Test();
        ep.setX(x);
        ep.setY(y);
        ep.setZ(z);
        ep.setXyz(p);

        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(ep);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    private static double PartialCorrelation(double X_Y, double X_Z, double Y_Z){
        return  X_Y - (X_Y - X_Z * Y_Z) / Math.sqrt((1 - Math.pow(X_Z, 2)) * (1 - Math.pow(Y_Z, 2)));
    }

    private static double PearsonsR(String arg1, String arg2) {
        List Xs = new ArrayList();
        List Ys = new ArrayList();
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Xs = session.createQuery("select close from Entity_" + arg1).list();
            Ys = session.createQuery("select close from Entity_" + arg2).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }

        double[] X = Xs.stream().mapToDouble(d -> Double.parseDouble(d.toString())).toArray();
        double[] Y = Ys.stream().mapToDouble(d -> Double.parseDouble(d.toString())).toArray();

        if(X.length > Y.length){
            X = Arrays.copyOfRange(X, 0, Y.length);
        }

        if(Y.length > X.length){
            Y = Arrays.copyOfRange(Y, 0, X.length);
        }

        if(X.length <= 1){
            return Double.MAX_VALUE;
        }

        PearsonsCorrelation p = new PearsonsCorrelation();

        return p.correlation(X, Y);
    }
}


