package de.unistuttgart.overworldbackend.data;

import de.unistuttgart.overworldbackend.data.enums.FacingDirection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * A SceneTransitionSpot is an instance of an ObjectSpot
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SceneTransitionSpot extends ObjectSpot {

    @ManyToOne(cascade = CascadeType.ALL)
    Position size;

    @ManyToOne(cascade = CascadeType.ALL)
    AreaLocation areaToLoad;

    FacingDirection facingDirection;

    String style;

    public SceneTransitionSpot(
        final AreaLocation area,
        final Position position,
        final Position size,
        final AreaLocation areaToLoad,
        final FacingDirection facingDirection,
        final String style
    ) {
        super(area, position);
        this.size = size;
        this.areaToLoad = areaToLoad;
        this.facingDirection = facingDirection;
        this.style = style;
    }
}
