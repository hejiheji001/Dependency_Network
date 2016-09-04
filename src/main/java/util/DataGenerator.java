package util;

import Entities.Entity_PartialCorrelation;
import Entities.Entity_Test;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by FireAwayH on 07/07/2016.
 */
public class DataGenerator {

    // 1272792# P(Alm, Blt : Lmp)
    
    public static void main(String[] args){
        long start = new Date().getTime();
        String X;
        String Y;
        String Z;
        String[] s = StockList.someStocks;
//        String[] s = StockList.someStocks2;/
//        String[] s = StockList.someStocks3;/
//        String[] s = StockList.someStocks4;/
//        String[] s = StockList.someStocks5;/
//        String[] s = StockList.someStocks6;/
//        String[] s = StockList.someStocks7;/
//        String[] s = StockList.someStocks8;/
//        String[] s = StockList.someStocks9;/
//        String[] s = StockList.someStocks10;/
        int index = 0;
        double X_Y = 0;
        double X_Z = 0;
        double Y_Z = 0;
        double P = 0;

        List<Entity_PartialCorrelation> l = new ArrayList<>();

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
//                            saveToDB(X, Y, Z, P);
                            Entity_PartialCorrelation ep = new Entity_PartialCorrelation();
                            ep.setX(X);
                            ep.setY(Y);
                            ep.setZ(Z);
                            ep.setXyz(P);
                            l.add(ep);

                            if(l.size() == 1000){
                                saveToDB(l);
                                l = new ArrayList<>();
                            }
                        }
                    }
                }
            }
        }

        long end = new Date().getTime();
        System.out.println("Uses " + (end - start) + " milisecs");
    }

    private static void saveToDB(List<Entity_PartialCorrelation> l) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            int batchSize = 50;
            final int[] i = {0};
            l.forEach(e -> {
                session.saveOrUpdate(e);
                i[0]++;
                if (i[0] % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
            });
            tx.commit();
            session.close();
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
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


