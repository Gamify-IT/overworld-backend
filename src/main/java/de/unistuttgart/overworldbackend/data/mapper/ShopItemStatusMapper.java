package de.unistuttgart.overworldbackend.data.mapper;

import de.unistuttgart.overworldbackend.data.ShopItem;
import de.unistuttgart.overworldbackend.data.ShopItemDTO;
import de.unistuttgart.overworldbackend.data.ShopItemStatus;
import de.unistuttgart.overworldbackend.data.ShopItemStatusDTO;
import org.mapstruct.Mapper;
import java.util.List;


/**
 * Maps shopItems to shopItemDTOs
 */
@Mapper(componentModel = "spring")
public interface ShopItemStatusMapper {
    ShopItemStatusDTO shopItemStatusToShopItemStatusDTO(final ShopItemStatus shopItemStatus);

    ShopItemStatus shopItemStatusDTOToShopItemStatus(final ShopItemStatusDTO shopItemStatusDTODTO);

    List<ShopItemStatusDTO> shopItemStatusToShopItemStatusDTOs(final List<ShopItemStatus> shopItemStatuses);
}
