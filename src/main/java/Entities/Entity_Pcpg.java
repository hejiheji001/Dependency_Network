package Entities;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by FireAwayH on 28/07/2016.
 */
@Entity
@Table(name = "pcpg")
@IdClass(Entity_PcpgPK.class)
public class Entity_Pcpg {
    private String source;
    private String targetX;
    private String targetY;
    private BigInteger dependency;

    public Entity_Pcpg() {
    }

    public Entity_Pcpg(Entity_PcpgPK pk) {
        this.source = pk.getSource();
        this.targetX = pk.getTargetX();
        this.targetY = pk.getTargetY();
    }

    @Id
    @Column(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Id
    @Column(name = "target_x")
    public String getTargetX() {
        return targetX;
    }

    public void setTargetX(String targetX) {
        this.targetX = targetX;
    }

    @Id
    @Column(name = "target_y")
    public String getTargetY() {
        return targetY;
    }

    public void setTargetY(String targetY) {
        this.targetY = targetY;
    }

    @Basic
    @Column(name = "dependency")
    public BigInteger getDependency() {
        return dependency;
    }

    public void setDependency(BigInteger dependency) {
        this.dependency = dependency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity_Pcpg that = (Entity_Pcpg) o;

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (targetX != null ? !targetX.equals(that.targetX) : that.targetX != null) return false;
        if (targetY != null ? !targetY.equals(that.targetY) : that.targetY != null) return false;
        if (dependency != null ? !dependency.equals(that.dependency) : that.dependency != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (targetX != null ? targetX.hashCode() : 0);
        result = 31 * result + (targetY != null ? targetY.hashCode() : 0);
        result = 31 * result + (dependency != null ? dependency.hashCode() : 0);
        return result;
    }
}
