package de.unistuttgart.overworldbackend.data;

import de.unistuttgart.overworldbackend.data.enums.ShopItemCategory;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents an item that can be bought in the shop.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopItem {

    @Id
    ShopItemID shopItemID;

    /**
     * The cost of the item
     */
    int cost;

    String imageName;

    ShopItemCategory category;

    boolean bought;

    public void setBought(boolean bought) {
        this.bought = bought;
    }

}
