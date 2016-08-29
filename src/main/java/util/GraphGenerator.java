package util;

import Entities.*;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.PCPG;
import util.StockList;

import java.util.*;

import static util.PlanarTesting.planarityTester.planarityTesting;

/**
 * Created by FireAwayH on 12/07/2016.
 */
public class GraphGenerator {

    static String[] s = StockList.someStocks;

    public static void main(String[] args) {
        Date d = new Date();
        String X;
        String Y;
//        String Z;
        int index = 0;

        PCPG_Extend(Arrays.asList(s));

//        for(int i = 0; i < s.length; i++){
//            X = StockList.stocks[i];
//            for (int k = 0; k < s.length; k++){
//                Y = StockList.stocks[k];
//                if(!X.equals(Y)){
//                    ++index;
//                    if(index > 0) {
//                        System.out.print(index + " #" + "X = " + X + " Y = " + Y + " ");
//                        PCTN_Extend(X, Y);
//                    }
//                }
//            }
//        }

        Date dd = new Date();
//        System.out.print(dd.getTime() - d.getTime());
        System.exit(0);
    }

    private static void PCPG_Extend(List<String> stks) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List ds = session.createQuery("select avg(xyz) as average, x, z from Entity_Test where x in (:STKs) and z in (:STKs) group by x, z order by average desc").setParameterList("STKs", stks).list();
            Map<PCPG, Double> pcpgs = new TreeMap<>();
            ds.forEach(i -> {
                Object[] t = (Object[]) i;
                double d = Double.parseDouble(t[0].toString());
                PCPG p = new PCPG(t[1].toString(), t[2].toString(), d);
                PCPG inverse = new PCPG(t[2].toString(), t[1].toString(), d);
                if(!pcpgs.containsKey(inverse)){
                    pcpgs.put(p, d);
                }else{
                    System.out.println(p);
                }
            });

            NavigableMap prePCPG = ((TreeMap)pcpgs).descendingMap();
            List<String[]> finalPCPG = MapNodeToIndex(stks, prePCPG);

            for(int i = 0; i < finalPCPG.size(); i++){
                Entity_Pcpg p = new Entity_Pcpg();
                String[] tem = finalPCPG.get(i);
                p.setDependency(Double.parseDouble(tem[2].toString()));
                p.setSource(tem[1]);
                p.setTarget(tem[0]);
                session.saveOrUpdate(p);
            }

//            finalPCPG.forEach(x -> {
////                PCPG tem = (PCPG) k;
//                Entity_Pcpg p = new Entity_Pcpg();
////                p.setSource(tem.getX());
//                p.setSource("tem.getZ()");
//                p.setTarget("x");
////                p.setDependency(Double.parseDouble(v.toString()));
//                session.saveOrUpdate(p);
//            });

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

    private static List MapNodeToIndex(List<String> stks, NavigableMap prePCPG) {
        List<Integer> i = new ArrayList<>();
        List<Integer> j = new ArrayList<>();

        List<String> index = new ArrayList<>();

        List finalPCPG = new ArrayList<>();
        i.add(0);
        j.add(0);
//        int nodes = stks.size();
//        int edges = finalPCPG.size();
        prePCPG.forEach((k, v) -> {
            PCPG tem = (PCPG)k;
            String x = tem.getX();
            String z = tem.getZ();
            if(!index.contains(x)) {
                index.add(x);
            }
            if(!index.contains(z)) {
                index.add(z);
            }
            i.add(index.indexOf(x) + 1);
            j.add(index.indexOf(z) + 1);

            int edges = i.size() - 1;
            Integer[] t1 = i.toArray(new Integer[edges]);
            Integer[] t2 = j.toArray(new Integer[edges]);
            Integer[] t = (Integer[])(ArrayUtils.addAll(t1, t2));

            int nodes = (int) Arrays.asList(t).stream().distinct().count() - 1;

            if (edges <= 3 * nodes - 6){
                try {
                    if (planarityTesting(nodes, edges, i, j)) {
                        finalPCPG.add(new String[]{x, z, v.toString()});
                    }
                }catch (Exception e){
//                    e.printStackTrace();
                }
            }
        });
        return finalPCPG;
    }

    private static void PCTN_Extend(String X, String Y) {
        List rs;
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            double d = (Double)session.createQuery("select avg(xyz) + 2 * stddev(xyz) from Entity_PartialCorrelation where x=:X and y=:Y").setParameter("X", X).setParameter("Y", Y).uniqueResult();
            rs = session.createQuery("select z, xyz from Entity_PartialCorrelation where x=:X and y=:Y and xyz>:innerSelect").setParameter("X", X).setParameter("Y", Y).setParameter("innerSelect", d).list();
//            System.out.println(d);

            final List<String> finalStk = new ArrayList<>();
            rs.forEach(r -> {
                Entity_Pctn pk = new Entity_Pctn();
                Object[] t = (Object[])r;
                pk.setSource(t[0].toString());
                pk.setTargetX(X);
                pk.setTargetY(Y);
                pk.setDependency(Double.parseDouble(t[1].toString()));
                finalStk.add(X);
                finalStk.add(Y);
                finalStk.add(t[0].toString());
                session.saveOrUpdate(pk);
            });


            finalStk.stream().distinct().forEach(s -> {
                Entity_Stocksinpctn sp = new Entity_Stocksinpctn();
                sp.setName(s);
                session.saveOrUpdate(sp);
            });

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



    @Deprecated()
    private static boolean PCTN(String X, String Y, String Z) {
        boolean result = false;

        Entity_PartialCorrelationPK key = new Entity_PartialCorrelationPK();
        key.setX(X);
        key.setY(Y);
        key.setZ(Z);

        Object rs;
        double d;
        double meanplus2sd;
//        double mean;
//        double sd;

        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Entity_PartialCorrelation entity = (Entity_PartialCorrelation) session.get(Entity_PartialCorrelation.class, key);
            d = entity.getXyz();

            rs = session.createQuery("select avg(xyz) + 2 * stddev(xyz) from Entity_PartialCorrelation where x=:X and y=:Y").setParameter("X", X).setParameter("Y", Y).uniqueResult();
            meanplus2sd = (Double)rs;
//            mean = Double.parseDouble(rs[0].toString());
//            sd = Double.parseDouble(rs[1].toString());

            if (d > meanplus2sd) {
                result = true;

                Entity_PctnPK pk = new Entity_PctnPK();
                pk.setSource(Z);
                pk.setTargetX(X);
                pk.setTargetY(Y);

                try {
                    session.load(Entity_Pctn.class, pk);
                    System.err.println("Exists: Z -> X & Z -> Y: " + Z + " -> " + X + " & " + Z + " -> " + Y);
                }catch (Exception e){
                    Entity_Pctn p = new Entity_Pctn();
                    p.setSource(Z);
                    p.setTargetX(X);
                    p.setTargetY(Y);
                    session.save(p);
                    System.out.println("Z -> X & Z -> Y: " + Z + " -> " + X + " & " + Z + " -> " + Y);
                }
            }

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }

        return result;
    }


    public static List getDependencyNetwork(String graphEntityName) {
        List l;
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            l = session.createQuery("from " + graphEntityName).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return l;
    }

    public static List getStock(String stockTable) {
        List l;
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            l = session.createQuery("select name from " + stockTable + " order by name").list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return l;
    }
}
