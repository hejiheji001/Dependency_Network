package util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.Seekable;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.Decompressor;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.SplitLineReader;

import java.io.IOException;

/**
 * Created by FireAwayH on 07/08/2016.
 * Every Key should be a stock name and Value should be the prices of the stock
 */
public class StockPricesArrayRecordReader extends RecordReader<Text, ArrayWritable> {

    private Text currentKey;
    private ArrayWritable currentValue;
    private boolean finishConverting = false;
    private static final Log LOG = LogFactory.getLog(StockPricesArrayRecordReader.class);
    public static final String MAX_LINE_LENGTH = "mapreduce.input.linerecordreader.line.maxlength";

    private long start;
    private long pos;
    private long end;
    private SplitLineReader in;
    private FSDataInputStream fileIn;
    private Seekable filePosition;
    private int maxLineLength;
    private boolean isCompressedInput;
    private Decompressor decompressor;
    private byte[] recordDelimiterBytes;
    private FileSplit fileSplit;
    private JobContext jobContext;



    public StockPricesArrayRecordReader(){

    }

    @Override
    public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException, InterruptedException {
//        Configuration job = context.getConfiguration();
//        String delimiter = job.get("textinputformat.record.delimiter");
//        if (null != delimiter) {
//            this.recordDelimiterBytes = delimiter.getBytes(Charsets.UTF_8);
//        }

        this.fileSplit = (FileSplit) genericSplit;
        this.jobContext = context;

//        this.maxLineLength = job.getInt(MAX_LINE_LENGTH, Integer.MAX_VALUE);
//        start = fileSplit.getStart();
//        end = start + fileSplit.getLength();
//        final Path file = fileSplit.getPath();
//
//        // open the file and seek to the start of the split
//        final FileSystem fs = file.getFileSystem(job);
//        fileIn = fs.open(file);
//
//        CompressionCodec codec = new CompressionCodecFactory(job).getCodec(file);
//        if (null!=codec) {
//            isCompressedInput = true;
//            decompressor = CodecPool.getDecompressor(codec);
//            if (codec instanceof SplittableCompressionCodec) {
//                final SplitCompressionInputStream cIn =
//                        ((SplittableCompressionCodec)codec).createInputStream(
//                                fileIn, decompressor, start, end,
//                                SplittableCompressionCodec.READ_MODE.BYBLOCK);
//                in = new CompressedSplitLineReader(cIn, job,
//                        this.recordDelimiterBytes);
//                start = cIn.getAdjustedStart();
//                end = cIn.getAdjustedEnd();
//                filePosition = cIn;
//            } else {
//                in = new SplitLineReader(codec.createInputStream(fileIn,
//                        decompressor), job, this.recordDelimiterBytes);
//                filePosition = fileIn;
//            }
//        } else {
//            fileIn.seek(start);
//            in = new UncompressedSplitLineReader(
//                    fileIn, job, this.recordDelimiterBytes, fileSplit.getLength());
//            filePosition = fileIn;
//        }
//        if (start != 0) {
//            start += in.readLine(new Text(), 0, maxBytesToConsume(start));
//        }
//        this.pos = start;
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
//        if (currentKey == null) {
//            currentKey = new LongWritable();
//        }
//        currentKey.set(pos);
//        if (currentValue == null) {
//            currentValue = new ArrayWritable(DoubleWritable.class);
//        }
//        int newSize = 0;
//        // We always read one extra line, which lies outside the upper
//        // split limit i.e. (end - 1)
//        while (getFilePosition() <= end || in.needAdditionalRecordAfterSplit()) {
//            if (pos == 0) {
////                newSize = skipUtfByteOrderMark();
//            } else {
//                newSize = in.readLine(currentValue, maxLineLength, maxBytesToConsume(pos));
//                pos += newSize;
//            }
//
//            if ((newSize == 0) || (newSize < maxLineLength)) {
//                break;
//            }
//
//            // line too long. try again
//            LOG.info("Skipped line of size " + newSize + " at pos " + (pos - newSize));
//        }
//        if (newSize == 0) {
//            currentKey = null;
//            currentValue = null;
//            return false;
//        } else {
//            return true;
//        }
//
        if (!finishConverting) {
            currentValue = new ArrayWritable(DoubleWritable.class);
            int len = (int) fileSplit.getLength();
            byte[] content = new byte[len];
            Path file = fileSplit.getPath();
            FileSystem fs = file.getFileSystem(jobContext.getConfiguration());
            FSDataInputStream in = null;
            try {
                in = fs.open(file);
                IOUtils.readFully(in, content, 0, len);
//                currentValue.set(content, 0, len);
            } finally {
                if (in != null) {
                    IOUtils.closeStream(in);
                }
            }
            finishConverting = true;
            return true;
        }
        return false;

    }

    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return currentKey;
    }

    @Override
    public ArrayWritable getCurrentValue() throws IOException, InterruptedException {
        return currentValue;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        float progress = 0;
        if (finishConverting) {
            progress = 1;
        }
        return progress;
    }

    @Override
    public void close() throws IOException {

    }
}
