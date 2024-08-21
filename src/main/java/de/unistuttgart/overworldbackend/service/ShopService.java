package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.enums.ShopItemCategory;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import de.unistuttgart.overworldbackend.repositories.PlayerStatisticRepository;
import de.unistuttgart.overworldbackend.repositories.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Arrays;

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
     * Sets up all the shop items per player statistic. Aka per player per course.
     */
    @EventListener(ApplicationStartedEvent.class)
    public void setupShopItems() {
        List<ShopItem> shopItems = Arrays.asList(
                new ShopItem(
                        ShopItemID.FLAME_HAT,
                        15,
                        "flames",
                        ShopItemCategory.ACCESSORIES,
                        false
                ),
                new ShopItem(
                        ShopItemID.HEART_GLASSES,
                        15,
                        "herzi",
                        ShopItemCategory.ACCESSORIES,
                        false
                ),
                new ShopItem(
                        ShopItemID.GLOBE_HAT,
                        15,
                        "globuseinzeln",
                        ShopItemCategory.ACCESSORIES,
                        false
                ),
                new ShopItem(
                        ShopItemID.SUIT,
                        15,
                        "anzug",
                        ShopItemCategory.OUTFIT,
                        false
                ),
                new ShopItem(
                        ShopItemID.SANTA_COSTUME,
                        15,
                        "santa",
                        ShopItemCategory.OUTFIT,
                        false
                )
        );

        shopRepository.saveAll(shopItems);
        System.out.println(Arrays.toString(shopItems.toArray()));
    }

    /**
     * Returns all shop items for a given player.
     * @param playerId the id of the player
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
     * Updates the given shop item.
     * @param playerId the id of the player
     * @param courseID the course id
     * @param shopItemID the id of the item
     * @param shopItemDTO the updated parameters
     * @throws ResponseStatusException (404) if the player or the item does not exist
     * @return the updated item
     */
    public ShopItem updateShopItem(final String playerId, final int courseID, final ShopItemID shopItemID,
                                   final ShopItemDTO shopItemDTO) {
        PlayerStatistic playerStatistic = playerStatisticRepository.findByCourseIdAndUserId(courseID, playerId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + playerId + " does not exist"));
        try {
            return playerStatistic.updateItem(shopItemID, shopItemDTO.isBought());
        } catch(final EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item with id " + shopItemID + " not found.");
        }
    }
}