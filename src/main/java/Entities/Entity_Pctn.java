package Entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by FireAwayH on 12/07/2016.
 */
@Entity
@Table(name = "pctn")
@IdClass(Entity_PctnPK.class)
public class Entity_Pctn {
    private String source;
    private String targetX;
    private String targetY;
    private double dependency = 0;

    public Entity_Pctn() {
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

    @Override
    public boolean equals(Object o) {

        // See if this entity's every property is inside the List o
        if(o instanceof java.util.List){
            List<String> t = ((List) o);
            boolean r = (t.indexOf(this.source) != -1) && (t.indexOf(this.targetX) != -1) && (t.indexOf(this.targetY) != -1);
//            if(r){
//                System.out.println(this.source);
//                System.out.println(this.targetX);
//                System.out.println(this.targetY);
//            }
            return r;
        }




        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity_Pctn that = (Entity_Pctn) o;

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (targetX != null ? !targetX.equals(that.targetX) : that.targetX != null) return false;
        if (targetY != null ? !targetY.equals(that.targetY) : that.targetY != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (targetX != null ? targetX.hashCode() : 0);
        result = 31 * result + (targetY != null ? targetY.hashCode() : 0);
        return result;
    }

    @Column(name = "dependency")
    public double getDependency() {
        return dependency;
    }

    public void setDependency(double dependency) {
        this.dependency = dependency;
    }
}
