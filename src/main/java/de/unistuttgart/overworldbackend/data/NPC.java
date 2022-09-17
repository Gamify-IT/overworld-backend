package de.unistuttgart.overworldbackend.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.lang.Nullable;

/**
 * A NPC has a list of text to talk to a player in an area.
 *
 * A NPC is located in an area and a player can interact with it.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "index", "area_id", "course_id" }) })
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NPC {

  @Id
  @GeneratedValue(generator = "uuid")
  UUID id;

  int index;

  @ElementCollection
  List<String> text;

  @Nullable
  String description;

  @JsonBackReference
  @ManyToOne
  Course course;

  @JsonBackReference
  @ManyToOne
  Area area;

  public NPC(final List<String> text, final int index) {
    this.text = text;
    this.index = index;
  }

  public NPC(final List<String> text, final @org.jetbrains.annotations.Nullable String description, final int index) {
    this.text = text;
    this.index = index;
    this.description = description;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    final NPC npc = (NPC) o;
    return id != null && Objects.equals(id, npc.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
