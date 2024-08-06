package de.unistuttgart.overworldbackend.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class IntTupel implements Serializable {
    private int first;
    private int second;
    private int third;

    @JsonCreator
    public IntTupel(@JsonProperty("first") int first,
                    @JsonProperty("second") int second,
                    @JsonProperty("third") int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public IntTupel() {}

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getThird() {
        return third;
    }

    public void setThird(int third) {
        this.third = third;
    }
}
