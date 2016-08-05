package util;

/**
 * Created by FireAwayH on 28/07/2016.
 */
public class PCPG implements Comparable {
    private String x;
    private String z;
    private double d;

    public PCPG(String x, String z, double d) {
        this.x = x;
        this.z = z;
        this.d = d;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        PCPG that = (PCPG)obj;
        boolean r = false;
        if (this.x.equals(that.x) && this.z.equals(that.z)) r = true;
        if (this.x.equals(that.z) && this.z.equals(that.x)) r = true;
        return r;
    }

    @Override
    public int compareTo(Object o) {
        PCPG that = (PCPG)o;
        int r = 0;
        if(this.d > that.d) r = 1;
        if(this.d < that.d) r = -1;
        return r;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.x + " " + this.z + " " + this.d;
    }
}
