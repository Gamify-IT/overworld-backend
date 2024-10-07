package de.unistuttgart.overworldbackend.data.mapper;

import de.unistuttgart.overworldbackend.data.ShopItem;
import de.unistuttgart.overworldbackend.data.ShopItemDTO;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Maps shopItems to shopItemDTOs
 */
@Mapper(componentModel = "spring")
public interface ShopItemMapper {
    ShopItemDTO shopItemToShopItemDTO(final ShopItem shopItem);

    ShopItem shopItemDTOToShopItem(final ShopItemDTO shopItemDTO);

    List<ShopItemDTO> shopItemsToShopItemDTOs(final List<ShopItem> shopItems);
}
