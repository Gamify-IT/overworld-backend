package de.unistuttgart.overworldbackend.data;

import de.unistuttgart.overworldbackend.data.enums.ShopItemCategory;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Represents an item that can be bought in the shop.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "player_statistic_id", "item_id", "course_id" }) })
@Getter
@Setter
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

    String imageName;

    ShopItemCategory category;


}
