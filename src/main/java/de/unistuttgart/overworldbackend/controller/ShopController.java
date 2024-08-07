package de.unistuttgart.overworldbackend.controller;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.overworldbackend.data.AchievementStatisticDTO;
import de.unistuttgart.overworldbackend.data.ShopItemDTO;
import de.unistuttgart.overworldbackend.data.enums.AchievementTitle;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import de.unistuttgart.overworldbackend.data.mapper.ShopItemMapper;
import de.unistuttgart.overworldbackend.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Handles REST requests from the shop frontend
 */
@Tag(name = "Shop", description = "Modify achievement statistic")
@RestController
@Slf4j
@RequestMapping("/players/{playerId}/shop")
public class ShopController {
    @Autowired
    JWTValidatorService jwtValidatorService;

    @Autowired
    private ShopItemMapper shopItemMapper;

    @Autowired
    private ShopService shopService;

    @Operation(summary = "Get all items of a player")
    @GetMapping("")
    public List<ShopItemDTO> getShopItems(@PathVariable final String playerId,
                                          @CookieValue("access_token") final String accessToken) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        return shopItemMapper.shopItemsToShopItemDTOs(shopService.getShopItemsFromPlayer(playerId));
    }

    @Operation(summary = "Get item by its ID")
    @GetMapping("/{itemID}")
    public ShopItemDTO getShopItem(@PathVariable final String playerId,
            @PathVariable final ShopItemID itemID, @CookieValue("access_token") final String accessToken) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        return shopItemMapper.shopItemToShopItemDTO(shopService.getShopItemFromPlayer(playerId, itemID));
    }

    @Operation(summary = "Add an item to the players collection")
    @PostMapping("/{itemID}")
    public ShopItemDTO addShopItemToPlayer(@PathVariable final String playerId, @PathVariable final ShopItemID itemID,
                                           @Valid @RequestBody final AchievementStatisticDTO achievementStatisticDTO,
                                           @CookieValue("access_token") final String accessToken) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("update achievements {} to {}", itemID, achievementStatisticDTO.getProgress());
        return shopItemMapper.shopItemToShopItemDTO(shopService.addShopItemToPlayer(playerId, itemID, achievementStatisticDTO));
    }

}
