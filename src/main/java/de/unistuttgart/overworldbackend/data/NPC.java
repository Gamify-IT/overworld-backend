package de.unistuttgart.overworldbackend.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "index", "area_id", "course_id" }) })
@Data
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

  public NPC(final List<String> text, String description, final int index) {
    this.text = text;
    this.index = index;
    this.description = description;
  }
}
