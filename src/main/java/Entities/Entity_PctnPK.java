package Entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by FireAwayH on 12/07/2016.
 */
public class Entity_PctnPK implements Serializable {
    private String source;
    private String targetX;
    private String targetY;

    @Column(name = "source")
    @Id
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Column(name = "target_x")
    @Id
    public String getTargetX() {
        return targetX;
    }

    public void setTargetX(String targetX) {
        this.targetX = targetX;
    }

    @Column(name = "target_y")
    @Id
    public String getTargetY() {
        return targetY;
    }

    public void setTargetY(String targetY) {
        this.targetY = targetY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity_PctnPK that = (Entity_PctnPK) o;

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
}
