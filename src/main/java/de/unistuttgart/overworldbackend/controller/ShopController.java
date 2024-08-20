package de.unistuttgart.overworldbackend.controller;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.overworldbackend.data.ShopItemDTO;
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
@Tag(name = "Shop", description = "Modify shop items")
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

    @Operation(summary = "Get all shop items")
    @GetMapping("")
    public List<ShopItemDTO> getShopItemStatuses(
            @PathVariable final String playerId,
            @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        // TODO
        var x =  shopItemMapper.shopItemToShopItemDTO(shopService.getShopItemStatusesFromPlayer(playerId, 1));
        System.out.println(x);
        return x;
    }


    @Operation(summary = "Get item by its ID")
    @GetMapping("/{itemID}")
    public ShopItemDTO getShopItemStatus(@PathVariable final String playerId,
            @PathVariable final ShopItemID itemID, @CookieValue("access_token") final String accessToken) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("Get item {} of player {}", itemID, playerId); //TODO
        return shopItemMapper.shopItemToShopItemDTO(shopService.getShopItemStatusFromPlayer(playerId, itemID, 1));
    }

    @Operation(summary = "Update the status of an shop item")
    @PutMapping("/{itemID}")
    public ShopItemDTO updateShopItemStatus(
            @PathVariable final String playerId,
            @PathVariable final ShopItemID itemID,
            @Valid @RequestBody final ShopItemDTO shopItemDTO,
            @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);//TODO
        log.debug("update shopitemstatus {} to {}", itemID, shopItemDTO.getProgress());
        return shopItemMapper.shopItemToShopItemDTO(
               shopService.updateShopItemStatus(playerId, itemID, shopItemDTO, 1)
        );
    }

}
