package de.unistuttgart.overworldbackend.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class IntTupel implements Serializable {
    private int First;
    private int Second;
    private int Third;

    @JsonCreator
    public IntTupel(@JsonProperty("First") int First,
                    @JsonProperty("Second") int Second,
                    @JsonProperty("Third") int Third) {
        this.First = First;
        this.Second = Second;
        this.Third = Third;
    }

    public IntTupel() {}

    public int getFirst() {
        return First;
    }

    public void setFirst(int first) {
        this.First = first;
    }

    public int getSecond() {
        return Second;
    }

    public void setSecond(int second) {
        this.Second = second;
    }

    public int getThird() {
        return Third;
    }

    public void setThird(int third) {
        this.Third = third;
    }
}
