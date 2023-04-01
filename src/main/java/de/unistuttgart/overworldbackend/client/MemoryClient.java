package de.unistuttgart.overworldbackend.client;

import de.unistuttgart.overworldbackend.data.minigames.memory.MemoryConfiguration;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "memoryClient", url = "${memory.url}/configurations")
public interface MemoryClient {
    @GetMapping("/{id}")
    MemoryConfiguration getConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable("id") UUID id
    ); 
    @PostMapping("/")
    MemoryConfiguration postConfiguration(
        @CookieValue("access_token") final String accessToken,
        MemoryConfiguration memoryConfiguration
    );
}