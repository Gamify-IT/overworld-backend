package de.unistuttgart.overworldbackend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(value = "finitequizClient", url = "${finitequiz.url}/configurations")
public interface FinitequizClient {

    @PostMapping("/{id]/clone")
    UUID postClone(
            @CookieValue("access_token") final String accessToken,
            UUID configurationId
    );
}
