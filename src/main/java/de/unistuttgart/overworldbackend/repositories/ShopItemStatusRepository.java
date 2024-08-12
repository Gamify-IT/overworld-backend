
package de.unistuttgart.overworldbackend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.unistuttgart.overworldbackend.data.ShopItem;
import de.unistuttgart.overworldbackend.data.ShopItemStatus;
import de.unistuttgart.overworldbackend.data.ShopItemStatusDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopItemStatusRepository extends JpaRepository<ShopItemStatus, UUID> {
    List<ShopItemStatus> findAllByPlayerUserId(String playerId);

    Optional<ShopItemStatus> findByPlayerUserIdAndShopItem(
            final String playerId,
            final ShopItem shopItem
    );
}
