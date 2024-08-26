package de.unistuttgart.overworldbackend.data;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.unistuttgart.overworldbackend.data.enums.ShopItemID;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * The statistic of a player in a course.
 *
 * It contains information about unlocked areas, completed dungeons, current area,
 * statistics of minigames, owned shop items and statistics of NPCs.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerStatistic {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @ManyToMany(cascade = CascadeType.ALL)
    List<Area> unlockedAreas;

    @ManyToMany(cascade = CascadeType.ALL)
    List<Area> completedDungeons;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<Teleporter> unlockedTeleporters;

    @ManyToOne(cascade = CascadeType.ALL)
    Area currentArea;

    @ManyToOne(cascade = CascadeType.ALL)
    Course course;

    @NotNull
    String userId;

    @NotNull
    String username;

    LocalDateTime created;

    LocalDateTime lastActive;

    float logoutPositionX = 21.5f;

    float logoutPositionY = 2.5f;

    String logoutScene = "World 1";

    int currentCharacterIndex = 0;
  
    int volumeLevel;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PlayerTaskStatistic> playerTaskStatistics = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PlayerNPCStatistic> playerNPCStatistics = new HashSet<>();



    @PrePersist
    void onCreate() {
        lastActive = LocalDateTime.now();
        created = LocalDateTime.now();
    }

    long knowledge = 0;

    int rewards = 0;

    boolean showRewards = false;

    int credit;

    String pseudonym = "Traveller";

    @JsonManagedReference(value = "player-shopItem")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = ShopItem.class)
    List<ShopItem> items = new ArrayList<>();


    public void addKnowledge(final long gainedKnowledge) {
        knowledge += gainedKnowledge;
    }

    public void addPlayerTaskStatistic(final PlayerTaskStatistic playerTaskStatistic) {
        this.playerTaskStatistics.add(playerTaskStatistic);
    }

    public void addPlayerNPCStatistic(final PlayerNPCStatistic playerNPCStatistic) {
        this.playerNPCStatistics.add(playerNPCStatistic);
    }

    public void addUnlockedArea(final Area area) {
        if (!this.unlockedAreas.contains(area)) {
            this.unlockedAreas.add(area);
        }
    }

    public void addUnlockedTeleporter(final Teleporter teleporter) {
        this.unlockedTeleporters.add(teleporter);
    }

    public void addRewards(final int gainedRewards) {
        rewards += gainedRewards;
    }

    public void addCredit(int rewards) {
        credit += rewards;
    }

    public void addItem(ShopItem item) {
        this.items.add(item);
    }

    public ShopItem updateItem(ShopItemID shopItemID, boolean bought) {
        for(ShopItem item : items) {
            if(item.getShopItemID() == shopItemID) {
                item.setBought(bought);
                return item;
            }
        }
        throw new EntityNotFoundException("No item with this ID found");
    }
}
