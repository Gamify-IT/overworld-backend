package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.ShopItem;
import de.unistuttgart.overworldbackend.data.ShopItemDTO;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import de.unistuttgart.overworldbackend.repositories.PlayerRepository;
import de.unistuttgart.overworldbackend.repositories.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
@Transactional
@Slf4j
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private PlayerRepository playerRepository;
    @EventListener(ApplicationReadyEvent.class)
    public void updatePlayerItems() {
        // TODO
    }
    public List<ShopItem> getShopItemsFromPlayer(final String playerID) {
        // TODO
    }

    public ShopItem getShopItemFromPlayer(final String playerID, final ShopItemID itemID) {
        // TODO
    }

    public ShopItem addShopItemToPlayer(final String playerId, final ShopItemID itemID, final ShopItemDTO shopItemDTO) {
        // TODO
    }
}
