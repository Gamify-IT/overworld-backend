package de.unistuttgart.overworldbackend.data;

import de.unistuttgart.overworldbackend.data.enums.ShopItemCategory;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import java.util.UUID;
import javax.persistence.*;
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
    @GeneratedValue(generator = "uuid")
    UUID id;

    ShopItemID shopItemID;

    /**
     * The cost of the item
     */
    int cost;

    String imageName;

    ShopItemCategory category;

    boolean bought;

    public ShopItem(ShopItemID shopItemID, int cost, String imageName, ShopItemCategory category, boolean bought) {
        this.shopItemID = shopItemID;
        this.cost = cost;
        this.imageName = imageName;
        this.category = category;
        this.bought = bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
}
