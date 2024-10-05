package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.enums.ShopItemCategory;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import de.unistuttgart.overworldbackend.repositories.PlayerStatisticRepository;
import de.unistuttgart.overworldbackend.repositories.ShopRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
     * Creates the shop items and saves them in the database. The items are then used to set up the shop for each
     * player statistic (in the PlayerStatisticService.java) meaning per player per course.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void setupShopItems() {
        List<ShopItem> shopItems = Arrays.asList(
            new ShopItem(ShopItemID.FLAME_HAT, 10, "hat0", ShopItemCategory.ACCESSORIES, false),
            new ShopItem(ShopItemID.GLOBE_HAT, 2, "hat1", ShopItemCategory.ACCESSORIES, false),
            new ShopItem(ShopItemID.SAFETY_HELMET, 1, "hat2", ShopItemCategory.ACCESSORIES, false), new ShopItem(ShopItemID.BLONDE, 1, "hat3", ShopItemCategory.ACCESSORIES, false),
            new ShopItem(ShopItemID.CINEMA_GLASSES, 1, "glasses0", ShopItemCategory.ACCESSORIES, false),
            new ShopItem(ShopItemID.COOL_GLASSES, 2, "glasses1", ShopItemCategory.ACCESSORIES, false),
            new ShopItem(ShopItemID.HEART_GLASSES, 2, "glasses2", ShopItemCategory.ACCESSORIES, false),
            new ShopItem(ShopItemID.RETRO_GLASSES, 1, "glasses3", ShopItemCategory.ACCESSORIES, false),
            new ShopItem(ShopItemID.SPORTS, 1, "character3", ShopItemCategory.OUTFIT, false),
            new ShopItem(ShopItemID.SUIT, 1, "character4", ShopItemCategory.OUTFIT, false),
            new ShopItem(ShopItemID.BLUE_SHIRT, 1, "character5", ShopItemCategory.OUTFIT, false),
            new ShopItem(ShopItemID.LONGHAIR, 1, "character6", ShopItemCategory.OUTFIT, false),
            new ShopItem(ShopItemID.TITANIUM_KNIGHT, 1, "character7", ShopItemCategory.OUTFIT, false),
            new ShopItem(ShopItemID.SANTA_COSTUME, 2, "character8", ShopItemCategory.OUTFIT, false)

        );

        Set<ShopItemID> existingShopItemIDs = new HashSet<>();
        for (ShopItem item : shopRepository.findAll()) {
            existingShopItemIDs.add(item.getShopItemID());
        }

        for (ShopItem item : shopItems) {
            if (!existingShopItemIDs.contains(item.getShopItemID())) {
                shopRepository.save(item);
            } else {
                log.warn("Shop item with ID {} already exists and will not be added again.", item.getShopItemID());
            }
        }

        log.info("Shop items have been set up: {}", shopItems);
    }

    /**
     * Returns all shop items for a given player.
     * @param playerId the id of the player
     * @param courseID the course id
     * @throws ResponseStatusException (404) if the player does not exist
     * @return a list of shop items for the given player
     */
    public List<ShopItem> getShopItemsFromPlayer(final String playerId, final int courseID) {
        return playerStatisticRepository
            .findByCourseIdAndUserId(courseID, playerId)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + playerId + " does not exist")
            )
            .getItems();
    }

    /**
     * Returns the shop item for a given player and item.
     * @param playerId the id of the player
     * @param courseID the course id
     * @param shopItemID the id of the item
     * @throws ResponseStatusException (404) if the player does not exist
     * @return the item
     */
    public ShopItem getShopItemFromPlayer(final String playerId, final int courseID, final ShopItemID shopItemID) {
        return playerStatisticRepository
            .findByCourseIdAndUserId(courseID, playerId)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + playerId + " does not exist")
            )
            .getItems()
            .stream()
            .filter(item -> item.getShopItemID().equals(shopItemID))
            .toList()
            .get(0);
    }

    /**
     * Updates the given shop item based on the DTO provided.
     * @param playerId the id of the player
     * @param courseID the course id
     * @param shopItemID the id of the item
     * @param shopItemDTO the updated parameters
     * @throws ResponseStatusException (404) if the player or the item does not exist
     * @return the updated item
     */
    public ShopItem updateShopItem(final String playerId, final int courseID, final ShopItemID shopItemID, final ShopItemDTO shopItemDTO) {
        PlayerStatistic playerStatistic = playerStatisticRepository
            .findByCourseIdAndUserId(courseID, playerId)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + playerId + " does not exist"));

        try {
            return playerStatistic.updateItem(shopItemID, shopItemDTO.isBought());
        } catch (final EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item with id " + shopItemID + " not found.");
        }
    }
}
