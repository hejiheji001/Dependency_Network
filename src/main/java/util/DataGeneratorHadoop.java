package util;

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
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FireAwayH on 21/07/2016.
 */
public class DataGeneratorHadoop {

    public static class Map extends Mapper<LongWritable, Text, Text, DoubleWritable> {
//        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {



//            Object line = value.toArray();
//            String date = line[0];
//            String price = line[4];
            List<String> prices = new ArrayList<>();
            FileSplit fileSplit = (FileSplit)context.getInputSplit();

            FileSystem fs = FileSystem.get(context.getConfiguration());
            FileStatus[] status = fs.listStatus(new Path("src/main/resources/input2/"));  // you need to pass in your hdfs path


            String x = context.getConfiguration().get("map.input.file");

            String filename = fileSplit.getPath().getName();
            if(!filename.isEmpty()) {
//                context.write(new Text(filename.split(".")[0]), new Text("1"));
            }

//            StringTokenizer tokenizer = new StringTokenizer(date);
//            while (tokenizer.hasMoreTokens()) {
//                word.set(tokenizer.nextToken());
//                context.write(word, one);
//            }
        }
    }
    public static class Reduce extends Reducer<Text, Text, Text, DoubleWritable> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            double sum = 1;
//            for (IntWritable val : values) {
//                sum += val.get();
//            }
            context.write(key, new DoubleWritable(sum));
        }
    }

    public static void assigner(String path, String output) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "DataGenerator");
        job.setJarByClass(DataGeneratorHadoop.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);


//        job.setMapOutputKeyClass(VariablePairWritable.class);
//        job.setMapOutputValueClass(VariableValueWritable.class);
//
//        job.setOutputKeyClass(VariablePairWritable.class);
//        job.setOutputValueClass(DoubleWritable.class);

        job.setInputFormatClass(TextInputFormat.class);
//        job.setInputFormatClass(ArrayInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);



//        StringBuilder paths = new StringBuilder();
//        Arrays.asList(files).forEach(s -> {
//            paths.append(path);
//            paths.append(s);
//            paths.append(",");
//        });
//        int len = paths.length();
//        paths.delete(len - 1, len);

        FileInputFormat.addInputPath(job, new Path(path));
//        FileInputFormat.addInputPaths(job, paths.toString());
        FileOutputFormat.setOutputPath(job, new Path(output));
//        FileSystem.get(conf).delete(new Path("output"),true);
        job.waitForCompletion(true);
    }

    public static void main(String[] args) throws Exception {
        assigner("src/main/resources/input2/", "src/main/resources/output2/");
    }
}
