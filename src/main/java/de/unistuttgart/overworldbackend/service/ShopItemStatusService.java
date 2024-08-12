package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.enums.AchievementTitle;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import de.unistuttgart.overworldbackend.repositories.PlayerRepository;
import de.unistuttgart.overworldbackend.repositories.ShopItemStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ShopItemStatusService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShopItemStatusRepository shopItemStatusRepository;

    /**
     * Returns all achievement statistics for a given player.
     * @param playerId the id of the player
     * @throws ResponseStatusException (404) if the player does not exist
     * @return a list of achievement statistics for the given player
     */
    public List<ShopItemStatus> getShopItemStatusesFromPlayer(final String playerId) {
        return playerRepository
                .findById(playerId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Player with id " + playerId + " does not exist")
                )
                .getShopItemStatuses();
    }

    /**
     * Returns the achievement statistic for a given player and achievement.
     * @param playerId the id of the player
     * @param shopItemID the title of the achievement
     * @throws ResponseStatusException (404) if the player or the achievement does not exist
     * @return the achievement statistic for the given player and achievement
     */
    public ShopItemStatus getShopItemStatusFromPlayer(
            final String playerId,
            final ShopItemID shopItemID
    ) {
        return getShopItemStatusesFromPlayer(playerId)
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
    }

    /**
     * Updates the progress of the given achievement statistic
     * @param playerId the if of the player
     * @param shopItemID the title of the achievement
     * @param shopItemStatusDTO the updated parameters
     * @throws ResponseStatusException (400) if the new progress is smaller than the current one
     * @throws ResponseStatusException (404) if the player or the achievement does not exist
     * @return the updated achievement statistic
     */
    public ShopItemStatus updateShopItemStatus(
            final String playerId,
            final ShopItemID shopItemID,
            final ShopItemStatusDTO shopItemStatusDTO
            ) {
        final ShopItemStatus shopItemStatus = getShopItemStatusFromPlayer(playerId, shopItemID);
        try {
            shopItemStatus.setProgress(shopItemStatusDTO.getProgress());
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The new progress cannot be smaller than the current one"
            );
        }
        return shopItemStatusRepository.save(shopItemStatus);
    }
}
