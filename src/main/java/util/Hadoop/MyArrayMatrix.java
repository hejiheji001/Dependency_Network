package util.Hadoop;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by FireAwayH on 08/08/2016.
 */
public class MyArrayMatrix extends Array2DRowRealMatrix{
    private final String rowSeparator = "\r\n";
    private final String columnSeparator = "\t";
    private final NumberFormat format = new DecimalFormat();

    public MyArrayMatrix(double[][] d) throws DimensionMismatchException, NoDataException, NullArgumentException {
        super(d);
    }

    public StringBuffer format(RealMatrix matrix, StringBuffer toAppendTo) {
        final int rows = matrix.getRowDimension();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < matrix.getColumnDimension(); ++j) {
                if (j > 0) {
                    toAppendTo.append(columnSeparator);
                }
                toAppendTo.append(matrix.getEntry(i, j));
            }
            if (i < rows - 1) {
                toAppendTo.append(rowSeparator);
            }
        }
        return toAppendTo;
    }

    @Override
    public String toString() {
        return format(this, new StringBuffer()).toString();
    }
}
