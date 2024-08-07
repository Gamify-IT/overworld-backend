package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.PlayerStatistic;
import de.unistuttgart.overworldbackend.data.ShopItem;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import de.unistuttgart.overworldbackend.repositories.PlayerStatisticRepository;
import de.unistuttgart.overworldbackend.repositories.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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
     * Returns a list of all shop items in a players' possession.
     *
     * @param playerID the player to get the items from
     * @return a list of the players' items
     * @throws ResponseStatusException (404) if the player does not exist
     */
    public List<ShopItem> getShopItemsFromPlayer(final String playerID, final int courseID) {
        return playerStatisticRepository.findByCourseIdAndUserId(courseID, playerID).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + playerID + " does not exist")
        ).getAllItemsFromInventory();
    }

    /**
     * Returns a list of all shop items in a players' possession.
     *
     * @param playerID the player to get the items from
     * @param itemID the shop item to return
     * @return the item of the player
     * @throws ResponseStatusException (404) if the player does not exist
     */
    public ShopItem getShopItemFromPlayer(final String playerID, final int courseID, final ShopItemID itemID) {
        return playerStatisticRepository.findByCourseIdAndUserId(courseID ,playerID).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + playerID + " does not exist")
        ).getItemFromInventory(itemID);
    }

    /**
     * Updates the players inventory, so that they are now in possession of the item.
     *
     * @param playerID the player to add the item to
     * @param itemID the item to add
     * @return the added item
     * @throws ResponseStatusException (404) if the player or the item does not exist
     */
    public ShopItem addShopItemToPlayer(final String playerID, final int courseID, final ShopItemID itemID) {
        PlayerStatistic playerStatistics = playerStatisticRepository.findByCourseIdAndUserId(courseID ,playerID).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + playerID + " does not exist")
        );
        ShopItem item = shopRepository.getReferenceById(itemID);
        return playerStatistics.addItemToInventory(item);
    }
}
