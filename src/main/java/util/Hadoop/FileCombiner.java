package util.Hadoop;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by FireAwayH on 08/08/2016.
 */
public class FileCombiner {

    private static int lineNum = 756;
    private static String input;
    private static FileStatus[] status;
    private static int index = 0;
    private static int length;
    private static double[][] matrixData;
    private static double[][] tranversedData;



    public static class Map extends Mapper<LongWritable, Text, Text, Text> {
        StringBuilder sb = new StringBuilder();
        List<String> loadedFiles = new ArrayList<>();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            FileSplit fileSplit = (FileSplit)context.getInputSplit();
            String filename = fileSplit.getPath().getName();
//            FileSystem fs = FileSystem.get(context.getConfiguration());
//            Path file = new Path(input + filename);
//
//            if(fs.getFileStatus(file).getLen() == 1){
//                fs.delete(file, true);
//            }else{
            String line = value.toString().split(",")[4];
            if(!loadedFiles.contains(filename)){
                loadedFiles.add(filename);
                sb.append(filename.split("\\.")[0] + "\t");
            }

            if(!line.contains("\t")){
                line += "\t";
            }
            sb.append(line);
//            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new Text(sb.toString()), new Text(""));
            super.cleanup(context);
        }
    }

    public static class Reduce extends Reducer<Text, Text, Text, Text> {
        StringBuilder sb = new StringBuilder();

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String[] line = key.toString().split("\\t");
            String[] data = Arrays.copyOfRange(line, 1, line.length);
            System.out.println(data);
            double[] d = Arrays.asList(data).stream().mapToDouble(Double::parseDouble).toArray();

            if(d.length == lineNum) {
                sb.append(line[0]);
                matrixData[index] = d;
                index++;
                if (index == length) {
                    sb.append("\r\n");
//                matrixData = blankToNum(matrixData, 0.0d);
                    tranversedData = new Array2DRowRealMatrix(matrixData).transpose().getData();
                    MyArrayMatrix transformedMatrix = new MyArrayMatrix(tranversedData);
                    context.write(new Text(sb.toString() + transformedMatrix.toString()), new Text(""));
                } else {
                    sb.append("\t");
                }
            }else{
                System.err.println(line[0]);
                FileSystem.get(context.getConfiguration()).delete(new Path(input + line[0] + ".txt"),true);
            }
        }

//        private double[][] blankToNum(double[][] matrixData, double v) {
//            List<double[]> l = new ArrayList<>(Arrays.asList(matrixData));
//            final int[] max = {0};
//            l.forEach(x -> {
//                if(x.length > max[0]) {
//                    max[0] = x.length;
//                }
//            });
//
//            int num = max[0];
//
//            for (int i = 0; i < l.size(); i++){
//                double[] y = l.get(i);
//                if(y.length < num){
//                    int old = y.length;
//                    y = Arrays.copyOf(y, num);
//                    for (int j = old; j < num; j++){
//                        y[j] = 0.0d;
//                    }
//                    l.set(i, y);
//                }
//            }
//            return l.toArray(new double[l.size()][]);
//        }
    }

    public static void combineFilesInPath(String input, String output) throws Exception{
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "FileCombiner");
        job.setJarByClass(FileCombiner.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(input));
//        FileInputFormat.addInputPath(job, new Path(input + "5/"));
        FileOutputFormat.setOutputPath(job, new Path(output));
        FileSystem.get(conf).delete(new Path(output),true);
        job.waitForCompletion(true);
    }

    public static void main(String[] args) throws Exception {
        long start = new Date().getTime();
        combine(args[0], args[1]);
        long end = new Date().getTime();
        System.out.println("Uses " + (end - start) + " milisecs"); // 11406 + 12105
        System.exit(0);
    }

    private static void combine(String input, String output) throws Exception{
//        input = "src/main/resources/input3/";
//        output = "src/main/resources/combined/";
        PathFilter filter = file -> file.getName().endsWith(".txt");
        FileSystem fs = FileSystem.get(new Configuration());
        status = fs.listStatus(new Path(input), filter);
        length = status.length;
        Arrays.asList(status).forEach(s -> {
            if(s.getLen() == 0){
                try {
                    fs.delete(s.getPath(), true);
                    length--;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        matrixData = new double[length][];
        combineFilesInPath(input, output);
    }
}
