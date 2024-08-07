package de.unistuttgart.overworldbackend.repositories;

import de.unistuttgart.overworldbackend.data.ShopItem;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the shop items
 */
@Repository
public interface ShopRepository extends JpaRepository<ShopItem, ShopItemID> {
}
