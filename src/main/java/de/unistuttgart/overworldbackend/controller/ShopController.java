package de.unistuttgart.overworldbackend.controller;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.overworldbackend.data.ShopItem;
import de.unistuttgart.overworldbackend.data.ShopItemDTO;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import de.unistuttgart.overworldbackend.data.mapper.ShopItemMapper;
import de.unistuttgart.overworldbackend.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Handles REST requests from the shop frontend
 */
@Tag(name = "Shop", description = "Modify shop items")
@RestController
@Slf4j
@RequestMapping("/players/{playerId}/courses/{courseId}/shop")
public class ShopController {

    @Autowired
    JWTValidatorService jwtValidatorService;

    @Autowired
    private ShopItemMapper shopItemMapper;

    @Autowired
    private ShopService shopService;

    @Operation(summary = "Get all shop items")
    @GetMapping("")
    public List<ShopItemDTO> getShopItems(
        @PathVariable final String playerId,
        @PathVariable final int courseId,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        List<ShopItem> items = shopService.getShopItemsFromPlayer(playerId, courseId);
        return shopItemMapper.shopItemsToShopItemDTOs(items);
    }

    @Operation(summary = "Get item by its ID")
    @GetMapping("/{itemID}")
    public ShopItemDTO getShopItem(
        @PathVariable final String playerId,
        @PathVariable int courseId,
        @PathVariable final ShopItemID itemID,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        ShopItem item = shopService.getShopItemFromPlayer(playerId, courseId, itemID);
        return shopItemMapper.shopItemToShopItemDTO(item);
    }

    @Operation(summary = "Update the bought status of an shop item")
    @PutMapping("/{itemID}")
    public ShopItemDTO updateShopItem(
        @PathVariable final String playerId,
        @PathVariable int courseId,
        @PathVariable final ShopItemID itemID,
        @Valid @RequestBody final ShopItemDTO shopItemDTO,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        ShopItem updatedItem = shopService.updateShopItem(playerId, courseId, itemID, shopItemDTO);
        return shopItemMapper.shopItemToShopItemDTO(updatedItem);
    }
}
