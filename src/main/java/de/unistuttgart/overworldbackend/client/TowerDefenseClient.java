package de.unistuttgart.overworldbackend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(value = "towerdefenseClient", url = "${towerdefense.url}/configurations")
public interface TowerDefenseClient {
    @PostMapping("/{id}/clone")
    UUID postClone(@CookieValue("access_token") final String accessToken, @PathVariable UUID id);
}
