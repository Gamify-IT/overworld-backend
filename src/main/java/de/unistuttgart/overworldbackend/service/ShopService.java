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

        shopRepository.saveAll(shopItems);

        final List<ShopItem> items = shopRepository.findAll();

        //TODO redo item creation
        for (final PlayerStatistic player : playerStatisticRepository.findAll()) {
            for (final ShopItem item : items) {
                player.getShopItemStatuses().add(new ShopItemStatus(player, item));
            }
        }
    }

    /**
     * Returns all shop items for a given player.
     * @param playerId the id of the player
     * @throws ResponseStatusException (404) if the player does not exist
     * @return a list of shop items for the given player
     */
    public List<ShopItem> getShopItemStatusesFromPlayer(final String playerId, final int courseID) {
        System.out.println("playerstat= "+playerStatisticRepository.findByCourseIdAndUserId(courseID, playerId));
        return playerStatisticRepository
                .findByCourseIdAndUserId(courseID, playerId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + playerId + " does not exist")
                )
                .getShopItem();
        // TODO redo getAll
    }

    /**
     * Returns the shop item for a given player and item.
     * @param playerId the id of the player
     * @param shopItemID the id of the item
     * @throws ResponseStatusException (404) if the player or the item does not exist
     * @return the item
     */
    public ShopItem getShopItemStatusFromPlayer(final String playerId, final ShopItemID shopItemID, final int courseID) {
        return getShopItemStatusesFromPlayer(playerId, courseID)
                .stream()
                .filter(shopItemStatus ->
                        shopItemStatus.getItem().getShopItemID().equals(shopItemID)
                )
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("There is no status for the shop item %s", shopItemID)
                        )
                );
        // TODO redo get
    }

    /**
     * Updates the given shop item
     * @param playerId the id of the player
     * @param shopItemID the id of the item
     * @param shopItemDTO the updated parameters
     * @throws ResponseStatusException (404) if the player or the item does not exist
     * @return the updated item
     */
    public ShopItem updateShopItemStatus(
            final String playerId,
            final ShopItemID shopItemID,
            final ShopItemDTO shopItemDTO,
            final int courseID
    ) {
        final ShopItem shopItemStatus = getShopItemStatusFromPlayer(playerId, shopItemID, courseID);
        try {
            shopItemStatus.setProgress(shopItemStatusDTO.getProgress());
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The new progress cannot be smaller than the current one"
            );
        }
        return shopRepository.save(shopItemStatus);
        // TODO redo put
    }


}
