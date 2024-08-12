package de.unistuttgart.overworldbackend.data;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "player_user_id", "achievement_achievement_title" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopItemStatus {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @JsonBackReference(value = "shopItem-player")
    @ManyToOne
    PlayerStatistic player;

    @OneToOne
    ShopItem item;

    boolean bought;

    public ShopItemStatus(PlayerStatistic player, ShopItem item) {
        this.player = player;
        this.item = item;
        this.bought = false;

    }

    /**
     * Sets the progress to the given value, if valid, and updates the completed flag accordingly
     * @param newStatus the new progress
     * @throws IllegalArgumentException if the new progress is smaller than the current one
     */
    public void setProgress(boolean newStatus) {
        if (!newStatus) {
            throw new IllegalArgumentException("The new progress cannot be not bought");
        }
       bought = newStatus;

    }

}
