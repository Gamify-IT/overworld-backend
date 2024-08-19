package de.unistuttgart.overworldbackend.controller;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.overworldbackend.data.AchievementStatisticDTO;
import de.unistuttgart.overworldbackend.data.ShopItemDTO;
import de.unistuttgart.overworldbackend.data.ShopItemStatus;
import de.unistuttgart.overworldbackend.data.ShopItemStatusDTO;
import de.unistuttgart.overworldbackend.data.enums.AchievementTitle;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import de.unistuttgart.overworldbackend.data.mapper.ShopItemMapper;
import de.unistuttgart.overworldbackend.data.mapper.ShopItemStatusMapper;
import de.unistuttgart.overworldbackend.service.ShopItemStatusService;
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
@Tag(name = "Shop", description = "Modify shop items")
@RestController
@Slf4j
@RequestMapping("/players/{playerId}/shop")
public class ShopController {
    @Autowired
    JWTValidatorService jwtValidatorService;

    @Autowired
    private ShopItemStatusMapper shopItemStatusMapper;

    @Autowired
    private ShopItemStatusService shopService;

    @Operation(summary = "Get all achievements")
    @GetMapping("")
    public List<ShopItemStatusDTO> getShopItemStatuses(
            @PathVariable final String playerId,
            @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        return shopItemStatusMapper.shopItemStatusToShopItemStatusDTOs(
               shopService.getShopItemStatusesFromPlayer(playerId)
        );
    }


    @Operation(summary = "Get item by its ID")
    @GetMapping("/{itemID}")
    public ShopItemStatusDTO getShopItemStatus(@PathVariable final String playerId,
            @PathVariable final ShopItemID itemID, @CookieValue("access_token") final String accessToken) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("Get item {} of player {}", itemID, playerId);
        return shopItemStatusMapper.shopItemStatusToShopItemStatusDTO(shopService.getShopItemStatusFromPlayer(playerId, itemID));
    }

    @Operation(summary = "Update the progress of an achievement")
    @PutMapping("/{title}")
    public ShopItemStatusDTO updateAchievementStatistic(
            @PathVariable final String playerId,
            @PathVariable final ShopItemID title,
            @Valid @RequestBody final ShopItemStatusDTO shopItemStatusDTO,
            @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("update achievements {} to {}", title, shopItemStatusDTO.getProgress());
        return shopItemStatusMapper.shopItemStatusToShopItemStatusDTO(
               shopService.updateShopItemStatus(playerId, title, shopItemStatusDTO)
        );
    }

}
