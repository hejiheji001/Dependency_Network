package util.Hadoop.PearsonAndPartialCorrelation;

import Entities.Entity_Test;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.Hadoop.VariablePairWritable;
import util.Hadoop.VariableValueWritable;
import util.HibernateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ComputePartialCorrelation{

    /**
     * @param args
     * @author FireAwayH
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     * @Usage: hadoop jar jarname mainclassname inputlocation outputlocation
     * @Example:- This is used to compute the PartialCorrelation coefficient between three variables....
     *
     * Input matrix:
     *
     * 	|c1	c2	c3  c4
     * 	------------------
     * 	|2	2	-2  4
     * 	|4	4	-4  1
     * 	|6	6	-6  4
     *
     * Correlation Matrix is:
     *
     * c1,c2:c3 xxxx
     * c1,c2:c4 yyyy
     * c1,c3:c2 qqqq
     * c1,c3:c4 wwww
     * c1,c4:c2 eeee
     * c1,c4:c3 rrrr
     * c2,c1:c3 zzzz
     * c2,c1:c4 aaaa
     * c2,c3:c1 tttt
     * c2,c3:c4 yyyy
     * .............
     *
     */

    public static void main(String[] args) throws Exception{
        long start = new Date().getTime();
        compute(args[0], args[1]);
        long end = new Date().getTime();
        System.out.println("Uses " + (end - start) + " milisecs"); // 11406 + 12105
        System.exit(0);
    }


    private static void compute(String input, String output) throws Exception{
        Configuration conf = new Configuration();

        FileSystem fs = FileSystem.get(conf);
        FileStatus[] ffs = fs.listStatus(new Path(input));
        for(FileStatus f : ffs){
            if(f.getLen() > 0){
                BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(f.getPath())));
                conf.set("stock.names", br.readLine());
            }
        }

        Job job = Job.getInstance(conf, "Computing Partial Correlation");

        job.setJarByClass(ComputePartialCorrelation.class);
        job.setMapperClass(PartialCorrelationMapper.class);
        job.setReducerClass(PartialCorrelationReducer.class);

        job.setOutputKeyClass(VariablePairWritable.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setMapOutputKeyClass(VariablePairWritable.class);
        job.setMapOutputValueClass(VariableValueWritable.class);

        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        fs.delete(new Path(output),true);
        job.waitForCompletion(true);
    }


    public static class PartialCorrelationMapper extends Mapper<LongWritable, Text, VariablePairWritable, VariableValueWritable> {
//        String[] names;
        VariablePairWritable emitKey = new VariablePairWritable();
        VariableValueWritable emitValue = new VariableValueWritable();
        int index = 0;

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            if(index == 0){
//                names = line.split("\\t");
//                context.getConfiguration().set("stock.names", line);
//                System.out.println(context.getConfiguration().get("stock.names"));
//                FileSystem fs = new
            }else{
                double[] myPairs = toDouble(line.split("\\t"));

                for (int i = 0; i < myPairs.length; i++) {
                    emitKey.setI(i);
                    emitValue.setI(myPairs[i]);
                    for (int j = i; j < myPairs.length; j++) {
                        emitKey.setJ(j);
                        emitValue.setJ(myPairs[j]);
                        context.write(emitKey, emitValue);
                    }
                }

                for (int i = 0; i < myPairs.length; i++) {
                    emitKey.setI(i);
                    emitValue.setI(myPairs[i]);
                    for (int j = i; j < myPairs.length; j++) {
                        emitKey.setK(j);
                        emitValue.setK(myPairs[j]);
                        context.write(emitKey, emitValue);
                    }
                }

                for (int i = 0; i < myPairs.length; i++) {
                    emitKey.setJ(i);
                    emitValue.setJ(myPairs[i]);
                    for (int j = i; j < myPairs.length; j++) {
                        emitKey.setK(j);
                        emitValue.setK(myPairs[j]);
                        context.write(emitKey, emitValue);
                    }
                }

//                for (int i = 0; i < myPairs.length; i++) {
//                    emitKey.setI(i);
//                    emitValue.setI(myPairs[i]);
//                    for (int j = i; j < myPairs.length; j++) {
//                        emitKey.setJ(j);
//                        emitValue.setJ(myPairs[j]);
//                        for (int k = j; k < myPairs.length; k++){
//                            emitKey.setK(k);
//                            emitValue.setK(myPairs[k]);
//                            context.write(emitKey, emitValue);
//                        }
//                    }
//                }
            }
            index++;
        }

        public double[] toDouble(String[] tokens) {
            double[] myArray = new double[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                if (i == 24) {
                    myArray[i] = Double.parseDouble("0.1");
                } else {
                    myArray[i] = Double.parseDouble(tokens[i]);
                }
            }
            return myArray;
        }
    }

    public static class PartialCorrelationReducer extends Reducer<VariablePairWritable, VariableValueWritable, VariablePairWritable, DoubleWritable> {
        Map<String, Double> records = new HashMap<>();

        public void reduce(VariablePairWritable key, Iterable<VariableValueWritable> values, Context context) throws IOException, InterruptedException {
            String[] names = context.getConfiguration().get("stock.names").split("\\t");

            double x = 0.0d;
            double y = 0.0d;
            double z = 0.0d;
            double xx = 0.0d;
            double yy = 0.0d;
            double zz = 0.0d;
            double xy = 0.0d;
            double xz = 0.0d;
            double yz = 0.0d;
            double n = 0.0d;
            for (VariableValueWritable value : values) {
                x += value.getI();
                y += value.getJ();
                z += value.getK();
                xx += Math.pow(value.getI(), 2.0d);
                yy += Math.pow(value.getJ(), 2.0d);
                zz += Math.pow(value.getK(), 2.0d);
                xy += value.getI() * value.getJ();
                xz += value.getI() * value.getK();
                yz += value.getJ() * value.getK();
                n += 1.0d;
            }
//            n /= 3;
            PearsonWritable XY = new PearsonWritable(x, y, xx, yy, xy, n);
            PearsonWritable XZ = new PearsonWritable(x, z, xx, zz, xz, n);
            PearsonWritable YZ = new PearsonWritable(y, z, yy, zz, yz, n);

            double x_y_i = XY.getCorrelation();
            double x_z_j = XZ.getCorrelation();
            double y_z_k = YZ.getCorrelation();

            double ijk = PartialCorrelation(x_y_i, x_z_j, y_z_k);
            double jki = PartialCorrelation(x_z_j, y_z_k, x_y_i);
            double kij = PartialCorrelation(y_z_k, x_y_i, x_z_j);

            if(key.getI() != key.getJ() && key.getI() != key.getK() && key.getJ() != key.getK() && !Double.isNaN(ijk) && !Double.isNaN(jki) && !Double.isNaN(kij)) {
                key.setI_name(names[(int) key.getI()]);
                key.setJ_name(names[(int) key.getJ()]);
                key.setK_name(names[(int) key.getK()]);
                records.put(key.toString(), ijk);
                context.write(key, new DoubleWritable(ijk));

                key.setI_name(names[(int) key.getJ()]);
                key.setJ_name(names[(int) key.getK()]);
                key.setK_name(names[(int) key.getI()]);
                records.put(key.toString(), jki);
                context.write(key, new DoubleWritable(jki));

                key.setI_name(names[(int) key.getK()]);
                key.setJ_name(names[(int) key.getI()]);
                key.setK_name(names[(int) key.getJ()]);
                records.put(key.toString(), kij);
                context.write(key, new DoubleWritable(kij));
            }
        }

        public double PartialCorrelation(double X_Y, double X_Z, double Y_Z){
            return  X_Y - (X_Y - X_Z * Y_Z) / Math.sqrt((1 - Math.pow(X_Z, 2)) * (1 - Math.pow(Y_Z, 2)));
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            Session session = HibernateUtil.getSession();
            Transaction tx = session.beginTransaction();
            try {
                int batchSize = 50;
                int i = 0;
//                System.out.println(records.size());
                for (String key : records.keySet()) {
                    i++;
                    Entity_Test ep = new Entity_Test();
                    String[] XY = key.split(",");
                    String[] YZ = XY[1].split(":");
                    ep.setX(XY[0]);
                    ep.setY(YZ[0]);
                    ep.setZ(YZ[1]);
                    ep.setXyz(records.get(key));
                    session.saveOrUpdate(ep);
                    if (i % batchSize == 0) {
                        session.flush();
                        session.clear();
                    }
                }
                tx.commit();
                session.close();
            } catch (RuntimeException e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            }
            super.cleanup(context);
        }
    }
}
