package Entities;

import javax.persistence.*;

/**
 * Created by FireAwayH on 07/07/2016.
 */
@Entity
@Table(name = "bp")
public class Entity_Bp {
    private String date;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;

    @Id
    @Column(name = "Date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Basic
    @Column(name = "Open")
    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    @Basic
    @Column(name = "High")
    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    @Basic
    @Column(name = "Low")
    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    @Basic
    @Column(name = "Close")
    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    @Basic
    @Column(name = "Volume")
    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity_Bp entity_bp = (Entity_Bp) o;

        if (date != null ? !date.equals(entity_bp.date) : entity_bp.date != null) return false;
        if (open != null ? !open.equals(entity_bp.open) : entity_bp.open != null) return false;
        if (high != null ? !high.equals(entity_bp.high) : entity_bp.high != null) return false;
        if (low != null ? !low.equals(entity_bp.low) : entity_bp.low != null) return false;
        if (close != null ? !close.equals(entity_bp.close) : entity_bp.close != null) return false;
        if (volume != null ? !volume.equals(entity_bp.volume) : entity_bp.volume != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (open != null ? open.hashCode() : 0);
        result = 31 * result + (high != null ? high.hashCode() : 0);
        result = 31 * result + (low != null ? low.hashCode() : 0);
        result = 31 * result + (close != null ? close.hashCode() : 0);
        result = 31 * result + (volume != null ? volume.hashCode() : 0);
        return result;
    }
}
