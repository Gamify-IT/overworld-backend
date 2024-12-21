package de.unistuttgart.overworldbackend.controller;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.overworldbackend.data.AchievementStatisticDTO;
import de.unistuttgart.overworldbackend.data.enums.AchievementTitle;
import de.unistuttgart.overworldbackend.data.mapper.AchievementStatisticMapper;
import de.unistuttgart.overworldbackend.service.AchievementStatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AchievementStatistic", description = "Modify achievement statistic")
@RestController
@Slf4j
@RequestMapping("/players/{playerId}/courses/{courseId}/achievements")
public class AchievementStatisticController {

    @Autowired
    JWTValidatorService jwtValidatorService;

    @Autowired
    private AchievementStatisticMapper achievementStatisticMapper;

    @Autowired
    private AchievementStatisticService achievementStatisticService;

    @Operation(summary = "Get all achievements of a player's course")
    @GetMapping("")
    public List<AchievementStatisticDTO> getAchievementStatistics(
        @PathVariable final String playerId,
        @PathVariable final int courseId,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get achievements");
        return achievementStatisticMapper.achievementStatisticsToAchievementStatisticDTOs(
            achievementStatisticService.getAchievementStatisticsFromPlayerOfCourse(playerId, courseId)
        );
    }

    @Operation(summary = "Get achievement by its title of a course")
    @GetMapping("/{title}")
    public AchievementStatisticDTO getAchievementStatistic(
        @PathVariable final String playerId,
        @PathVariable final int courseId,
        @PathVariable final AchievementTitle title,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get achievements {} ", title);
        return achievementStatisticMapper.achievementStatisticToAchievementStatisticDTO(
            achievementStatisticService.getAchievementStatisticFromPlayerOfCourse(playerId, courseId, title)
        );
    }

    @Operation(summary = "Update the progress of an achievement in a course")
    @PutMapping("/{title}")
    public AchievementStatisticDTO updateAchievementStatistic(
        @PathVariable final String playerId,
        @PathVariable final int courseId,
        @PathVariable final AchievementTitle title,
        @Valid @RequestBody final AchievementStatisticDTO achievementStatisticDTO,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("update achievements {} to {}", title, achievementStatisticDTO.getProgress());
        return achievementStatisticMapper.achievementStatisticToAchievementStatisticDTO(
            achievementStatisticService.updateAchievementStatistic(playerId, courseId, title, achievementStatisticDTO)
        );
    }
}
