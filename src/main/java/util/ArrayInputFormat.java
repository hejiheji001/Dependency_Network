package util;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * Created by FireAwayH on 07/08/2016.
 */
public class ArrayInputFormat extends FileInputFormat<Text, ArrayWritable> {

    @Override
    public RecordReader<Text, ArrayWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        RecordReader<Text, ArrayWritable> recordReader = new StockPricesArrayRecordReader();
        recordReader.initialize(split, context);
        return recordReader;
    }
}
