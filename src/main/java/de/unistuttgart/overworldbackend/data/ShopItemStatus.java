package de.unistuttgart.overworldbackend.data;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "player_id", "item_id" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopItemStatus {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    PlayerStatistic player;

    @OneToOne
    @JoinColumn(name = "item_id")
    ShopItem shopItem;;


    boolean bought;

    public ShopItemStatus(PlayerStatistic player,ShopItem shopItem) {
        this.player = player;
        this.shopItem = shopItem;
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

    public ShopItem getItem() {
        return shopItem;
    }

}
