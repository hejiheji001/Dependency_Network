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
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by FireAwayH on 08/08/2016.
 */
public class FileCombiner {
    public static class Map extends Mapper<LongWritable, Text, Text, Text> {
        StringBuffer sb = new StringBuffer();
        List<String> loadedFiles = new ArrayList<>();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            FileSplit fileSplit = (FileSplit)context.getInputSplit();
            String filename = fileSplit.getPath().getName();
            String line = value.toString().split(",")[4];
            if(!loadedFiles.contains(filename)){
                loadedFiles.add(filename);
                sb.append(filename.split("\\.")[0] + "\t");
            }

            if(!line.contains("\t")){
                line += "\t";
            }
            sb.append(line);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new Text(sb.toString()), new Text(""));
            super.cleanup(context);
        }
    }

    public static class Reduce extends Reducer<Text, Text, Text, Text> {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int length;
        int lineNum;
        double[][] matrixData;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            length = context.getConfiguration().getInt("matrix.length", 0);
            lineNum = context.getConfiguration().getInt("matrix.lineNum", 0);
            matrixData = new double[length][];
            super.setup(context);
        }

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String[] line = key.toString().split("\\t");
            String[] data = Arrays.copyOfRange(line, 1, line.length);
            double[] d = Arrays.asList(data).stream().mapToDouble(Double::parseDouble).toArray();

            if(d.length == lineNum) {
                sb.append(line[0]);
                matrixData[index] = d;
                index++;
                if (index == length) {
                    sb.append("\r\n");
                    MyArrayMatrix transformedMatrix = new MyArrayMatrix(new Array2DRowRealMatrix(matrixData).transpose().getData());
                    context.write(new Text(sb.toString() + transformedMatrix.toString()), new Text(""));
                } else {
                    sb.append("\t");
                }
            }else{
                System.err.println(line[0]);
//                FileSystem.get(context.getConfiguration()).delete(new Path(input + line[0] + ".txt"),true);
            }
        }
    }

    public static void combineFilesInPath(String input, String output, int length) throws Exception{
        Configuration conf = new Configuration();
        conf.setInt("matrix.length", length);
        conf.setInt("matrix.lineNum", 756);
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

    public static void combine(String input, String output) throws Exception{
        PathFilter filter = file -> file.getName().endsWith(".txt");
        FileSystem fs = FileSystem.get(new Configuration());
        FileStatus[] status = fs.listStatus(new Path(input), filter);
        int length = status.length;
        LineNumberReader lnr = null;
        for(FileStatus s : status){
            lnr = new LineNumberReader(new InputStreamReader(fs.open(s.getPath())));
            lnr.skip(Long.MAX_VALUE);
            int num = lnr.getLineNumber();
            if(s.getLen() == 0 || num != 756){
                length--;
                fs.delete(s.getPath(), true);
            }
        }
        lnr.close();
        combineFilesInPath(input, output, length);
    }
}
