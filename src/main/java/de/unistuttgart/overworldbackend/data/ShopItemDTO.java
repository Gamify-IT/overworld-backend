package de.unistuttgart.overworldbackend.data;

import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Represents an item that can be bought in the shop.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopItemDTO {
    ShopItemID shopItemID;

    /**
     * The cost of the item
     */
    int cost;

    String imageName;

    String category;
}
