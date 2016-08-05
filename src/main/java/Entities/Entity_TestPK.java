package Entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by FireAwayH on 28/07/2016.
 */
public class Entity_TestPK implements Serializable {
    private String x;
    private String y;
    private String z;

    @Column(name = "x")
    @Id
    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    @Column(name = "y")
    @Id
    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Column(name = "z")
    @Id
    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity_TestPK that = (Entity_TestPK) o;

        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        if (z != null ? !z.equals(that.z) : that.z != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }
}
