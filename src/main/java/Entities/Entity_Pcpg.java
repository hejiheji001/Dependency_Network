package Entities;

import javax.persistence.*;

/**
 * Created by FireAwayH on 07/08/2016.
 */
@Entity
@Table(name = "pcpg")
@IdClass(Entity_PcpgPK.class)
public class Entity_Pcpg {
    private String source;
    private String target;
    private Double dependency;

    @Id
    @Column(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Id
    @Column(name = "target")
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Basic
    @Column(name = "dependency")
    public Double getDependency() {
        return dependency;
    }

    public void setDependency(Double dependency) {
        this.dependency = dependency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity_Pcpg that = (Entity_Pcpg) o;

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (target != null ? !target.equals(that.target) : that.target != null) return false;
        if (dependency != null ? !dependency.equals(that.dependency) : that.dependency != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (dependency != null ? dependency.hashCode() : 0);
        return result;
    }
}
