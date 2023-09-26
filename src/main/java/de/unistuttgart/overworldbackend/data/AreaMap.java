package de.unistuttgart.overworldbackend.data;

import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AreaMap {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @ManyToOne
    Course course;

    @OneToOne
    Area area;

    boolean generatedArea;

    @Nullable
    @OneToOne
    CustomAreaMap customAreaMap;

    public AreaMap(Area area){
        this.area = area;
        course = area.getCourse();
        generatedArea = false;
    }
}
