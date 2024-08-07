package de.unistuttgart.overworldbackend.data;

import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents an item that can be bought in the shop.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopItem {
    @Id
    ShopItemID shopItemID;

    /**
     * The cost of the item
     */
    int cost;
}
