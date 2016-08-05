package Entities;

import javax.persistence.*;

/**
 * Created by FireAwayH on 07/07/2016.
 */
@Entity
@Table(name = "aa")
public class Entity_Aa {
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

        Entity_Aa entity_aa = (Entity_Aa) o;

        if (date != null ? !date.equals(entity_aa.date) : entity_aa.date != null) return false;
        if (open != null ? !open.equals(entity_aa.open) : entity_aa.open != null) return false;
        if (high != null ? !high.equals(entity_aa.high) : entity_aa.high != null) return false;
        if (low != null ? !low.equals(entity_aa.low) : entity_aa.low != null) return false;
        if (close != null ? !close.equals(entity_aa.close) : entity_aa.close != null) return false;
        if (volume != null ? !volume.equals(entity_aa.volume) : entity_aa.volume != null) return false;

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
