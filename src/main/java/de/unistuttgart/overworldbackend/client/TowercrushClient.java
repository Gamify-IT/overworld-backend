package de.unistuttgart.overworldbackend.client;

import de.unistuttgart.overworldbackend.data.minigames.towercrush.TowercrushConfiguration;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "towercrushClient", url = "${towercrush.url}/configurations")
public interface TowercrushClient {
    @PostMapping("/{id}/clone")
    UUID postClone(@CookieValue("access_token") final String accessToken, @PathVariable UUID id);
}
