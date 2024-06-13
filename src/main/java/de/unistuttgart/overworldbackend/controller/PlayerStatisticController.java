package de.unistuttgart.overworldbackend.controller;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.overworldbackend.data.PlayerRegistrationDTO;
import de.unistuttgart.overworldbackend.data.PlayerStatistic;
import de.unistuttgart.overworldbackend.data.PlayerStatisticDTO;
import de.unistuttgart.overworldbackend.data.mapper.PlayerStatisticMapper;
import de.unistuttgart.overworldbackend.service.PlayerStatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Player statistic", description = "Statistics of a player")
@RestController
@Slf4j
@RequestMapping("/courses/{courseId}/playerstatistics")
public class PlayerStatisticController {

    @Autowired
    JWTValidatorService jwtValidatorService;

    @Autowired
    private PlayerStatisticService playerStatisticService;

    @Autowired
    private PlayerStatisticMapper playerStatisticMapper;

    @Operation(summary = "Get a playerStatistic from a player in a course by playerId and courseId")
    @GetMapping("/{playerId}")
    public PlayerStatisticDTO getPlayerstatistic(
        @PathVariable final int courseId,
        @PathVariable final String playerId,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get statistics from player {} in course {}", playerId, courseId);
        return playerStatisticMapper.playerStatisticToPlayerstatisticDTO(
            playerStatisticService.getPlayerStatisticFromCourse(courseId, playerId)
        );
    }

    @Operation(summary = "Get own playerStatistic in a course of a player and courseId, player id is read from cookie")
    @GetMapping("")
    public PlayerStatisticDTO getOwnPlayerstatistic(
        @PathVariable final int courseId,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        final String playerId = jwtValidatorService.extractUserId(accessToken);
        log.debug("get statistics from player by cookie {} in course {}", playerId, courseId);
        return playerStatisticMapper.playerStatisticToPlayerstatisticDTO(
            playerStatisticService.getPlayerStatisticFromCourse(courseId, playerId)
        );
    }

    @Operation(summary = "Get all playerStatistics in a course of all players by courseId")
    @GetMapping("allPlayerStatistics")
    public List<PlayerStatisticDTO> getAllPlayerStatisticsFromCourse(
        @PathVariable final int courseId,
        @CookieValue("access_token") final String accessToken

    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get statistics from all players in course {}", courseId);
        return playerStatisticService.getAllPlayerStatisticsFromCourse(courseId);
    }

    @Operation(summary = "Create a playerStatistic in a course by playerId")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public PlayerStatisticDTO createPlayerstatistic(
        @PathVariable final int courseId,
        @Valid @RequestBody final PlayerRegistrationDTO playerRegistrationDTO,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("create playerstatistic for userId {} in course {}", playerRegistrationDTO, courseId);
        return playerStatisticService.createPlayerStatisticInCourse(courseId, playerRegistrationDTO);
    }

    @Operation(summary = "Update a playerStatistic in a course by playerId")
    @PutMapping("/{playerId}")
    public PlayerStatisticDTO updatePlayerStatistic(
        @PathVariable final int courseId,
        @PathVariable final String playerId,
        @RequestBody final PlayerStatisticDTO playerstatisticDTO,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("update playerStatistic for userId {} in course {} with {}", playerId, courseId, playerstatisticDTO);
        return playerStatisticService.updatePlayerStatisticInCourse(courseId, playerId, playerstatisticDTO);
    }

    @Operation(summary = "Set playerStatistic.active to current time")
    @PostMapping("/active")
    public PlayerStatisticDTO setActive(
        @PathVariable final int courseId,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        final String playerId = jwtValidatorService.extractUserId(accessToken);
        log.debug("set playerStatistic.active to current time for userId {} in course {}", playerId, courseId);
        return playerStatisticService.setActive(courseId, playerId);
    }
}
