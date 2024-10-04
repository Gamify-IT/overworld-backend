package de.unistuttgart.overworldbackend.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class IntTuple implements Serializable {

    private int worldId;
    private int dungeonId;
    private int numberId;

    @JsonCreator
    public IntTuple(
        @JsonProperty("worldId") int worldId,
        @JsonProperty("dungeonId") int dungeonId,
        @JsonProperty("numberId") int numberId
    ) {
        this.worldId = worldId;
        this.dungeonId = dungeonId;
        this.numberId = numberId;
    }

    public IntTuple() {}

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public int getDungeonId() {
        return dungeonId;
    }

    public void setDungeonId(int dungeonId) {
        this.dungeonId = dungeonId;
    }

    public int getNumberId() {
        return numberId;
    }

    public void setNumberId(int numberId) {
        this.numberId = numberId;
    }
}
