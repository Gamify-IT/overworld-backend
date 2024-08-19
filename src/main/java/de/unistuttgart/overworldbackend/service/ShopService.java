package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.enums.ShopItemCategory;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import de.unistuttgart.overworldbackend.repositories.PlayerStatisticRepository;
import de.unistuttgart.overworldbackend.repositories.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

/**
 * Service for the shop, manages the retrieval and addition of items from/to a player.
 */
@Service
@Transactional
@Slf4j
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private PlayerStatisticRepository playerStatisticRepository;

    /**
     * Checks for all players the current achievements adds new created achievements to the player and removes none existing achievements.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updatePlayerShopItemStatus() {
        List<ShopItem> shopItems = Arrays.asList(
                new ShopItem(
                        ShopItemID.FLAME_HAT,
                        15,
                        "flames",
                        ShopItemCategory.ACCESSORIES
                ),
                new ShopItem(
                        ShopItemID.HEART_GLASSES,
                        15,
                        "herzi",
                        ShopItemCategory.ACCESSORIES
                ),
                new ShopItem(
                        ShopItemID.GLOBE_HAT,
                        15,
                        "globuseinzeln",
                        ShopItemCategory.ACCESSORIES
                ),
                new ShopItem(
                        ShopItemID.SUIT,
                        15,
                        "anzug",
                        ShopItemCategory.OUTFIT
                ),
                new ShopItem(
                        ShopItemID.SANTA_COSTUME,
                        15,
                        "santa",
                        ShopItemCategory.OUTFIT
                )
        );

        shopItems.forEach(
                shopItem -> {
                    shopRepository.save(shopItem);
                }
        );

        final List<ShopItem> items = shopRepository.findAll();

        for (final PlayerStatistic player : playerStatisticRepository.findAll()) {
            // add statistic for achievement if not exists
            for (final ShopItem item : items) {
                if (
                        player
                                .getShopItemStatuses()
                                .stream()
                                .noneMatch(achievementStatistic ->
                                        achievementStatistic
                                                .getItem()
                                                .getShopItemID()
                                                .equals(item.getShopItemID())
                                )
                ) {
                    player.getShopItemStatuses().add(new ShopItemStatus(item));
                }
            }

        }

    }
}
