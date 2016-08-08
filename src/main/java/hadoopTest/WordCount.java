package hadoopTest;

/**
 * Created by FireAwayH on 21/07/2016.
 */

import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class WordCount {
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, one);
            }
        }
    }
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
//            for (IntWritable val : values) {
//                sum += val.get();
//            }

            List<IntWritable> l = Lists.newArrayList(values);
            List<IntWritable> f = l.stream().distinct().collect(Collectors.toList());
            context.write(key, new IntWritable(sum));
        }
    }
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path("/Volumes/Macintosh HD/Volumes/Macintosh HD/Users/FireAwayH/Github/Dependency_Network/src/main/resources/input/test.txt"));
        FileOutputFormat.setOutputPath(job, new Path("/Volumes/Macintosh HD/Volumes/Macintosh HD/Users/FireAwayH/Github/Dependency_Network/src/main/resources/output/"));
        FileSystem.get(conf).delete(new Path("/Volumes/Macintosh HD/Volumes/Macintosh HD/Users/FireAwayH/Github/Dependency_Network/src/main/resources/output/"),true);
        job.waitForCompletion(true);
    }
}
