package de.unistuttgart.overworldbackend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(value = "towercrushClient", url = "${towercrush.url}/configurations")
public interface TowercrushClient {

    @PostMapping("/{id]/clone")
    UUID postClone(
            @CookieValue("access_token") final String accessToken,
            UUID configurationId
    );
}
