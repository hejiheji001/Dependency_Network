package Entities;

import javax.persistence.*;

/**
 * Created by FireAwayH on 07/07/2016.
 */
@Entity
@Table(name = "partial_correlation")
@IdClass(Entity_PartialCorrelationPK.class)
public class Entity_PartialCorrelation {
    private String x;
    private String y;
    private String z;
    private Double xyz;

    @Id
    @Column(name = "X")
    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    @Id
    @Column(name = "Y")
    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Id
    @Column(name = "Z")
    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    @Basic
    @Column(name = "XYZ")
    public Double getXyz() {
        return xyz;
    }

    public void setXyz(Double xyz) {
        this.xyz = xyz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity_PartialCorrelation that = (Entity_PartialCorrelation) o;

        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        if (z != null ? !z.equals(that.z) : that.z != null) return false;
        if (xyz != null ? !xyz.equals(that.xyz) : that.xyz != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        result = 31 * result + (xyz != null ? xyz.hashCode() : 0);
        return result;
    }
}
