package util.Hadoop;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A pair of indices corresponding to the i-th and j-th variables/columns.
 * 
 * @author Nagamallikarjuna
 * 
 */
public class VariableValueWritable implements
		WritableComparable<VariableValueWritable> {

	private double i;
	private double j;
	private double k;

	/**
	 * Constructor.
	 */
	public VariableValueWritable() {
		this(0, 0, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param i
	 *            i-th index.
	 * @param j
	 *            j-th index.
	 */
	public VariableValueWritable(double i, double j) {
		this.i = i;
		this.j = j;
		this.k = 0;
	}

	public VariableValueWritable(double i, double j, double k) {
		this.i = i;
		this.j = j;
		this.k = k;
	}

	@Override
	public boolean equals(Object object) {
		if (null == object)
			return false;
		if (!(object instanceof VariableValueWritable))
			return false;
		VariableValueWritable indexPairs = (VariableValueWritable) object;
		double i1 = getI();
		double j1 = getJ();
		double k1 = getK();
		double i2 = indexPairs.getI();
		double j2 = indexPairs.getJ();
		double k2 = indexPairs.getK();

		return (i1 == i2 && j1 == j2 && k1 == k2);
	}

	@Override
	public int hashCode() {
		return 37 + (new Double(i)).hashCode() + (new Double(j)).hashCode() + (new Double(k)).hashCode();
	}

	@Override
	public String toString() {
		return (new StringBuilder()).append(getI()).append(',')
				.append(getJ()).append(getK() != 0 ? "," + getK() : "").toString();
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		i = in.readDouble();
		j = in.readDouble();
		k = in.readDouble();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeDouble(getI());
		out.writeDouble(getJ());
		out.writeDouble(getK());
	}

	public double getK() {
		return k;
	}

	public void setK(double k) {
		this.k = k;
	}

	/**
	 * Gets the i-th variable index.
	 * 
	 * @return Double.
	 */
	public double getI() {
		return i;
	}

	/**
	 * Gets the j-th variable index.
	 * 
	 * @return Double.
	 */
	public double getJ() {
		return j;
	}

	/**
	 * Sets the i-th variable index.
	 * 
	 * @param i
	 *            Double.
	 */
	public void setI(double i) {
		this.i = i;
	}

	/**
	 * Sets the j-th variable index.
	 * 
	 * @param j
	 */
	public void setJ(double j) {
		this.j = j;
	}

	@Override
	public int compareTo(VariableValueWritable object) {
		Double i1 = getI();
		Double j1 = getJ();
		Double k1 = getK();
		Double i2 = object.getI();
		Double j2 = object.getJ();
		Double k2 = object.getK();

		int result = i1.compareTo(i2);
		if (0 == result) {
			result = j1.compareTo(j2);
			if(0 == result){
				return k1.compareTo(k2);
			}
		}

		return result;
	}

}
