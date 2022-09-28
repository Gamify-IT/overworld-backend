package de.unistuttgart.overworldbackend.controller;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.overworldbackend.data.BookDTO;
import de.unistuttgart.overworldbackend.data.NPCDTO;
import de.unistuttgart.overworldbackend.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Book", description = "Get and update books from areas")
@RestController
@Slf4j
@RequestMapping("/courses/{courseId}/worlds/{worldIndex}")
public class BookController {

  @Autowired
  JWTValidatorService jwtValidatorService;

  @Autowired
  private BookService bookService;

  @Operation(summary = "Update a book by its index in a world")
  @PutMapping("/books/{bookIndex}")
  public BookDTO updateNPCFromWorld(
    @PathVariable int courseId,
    @PathVariable int worldIndex,
    @PathVariable int bookIndex,
    @RequestBody BookDTO bookDTO,
    @CookieValue("access_token") final String accessToken
  ) {
    jwtValidatorService.validateTokenOrThrow(accessToken);
    jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
    log.debug("update book {} of world {} of course {}", bookIndex, worldIndex, courseId);
    return bookService.updateBookFromWorld(courseId, worldIndex, bookIndex, bookDTO);
  }

  @Operation(summary = "Update a book by its index in a dungeon")
  @PutMapping("/dungeons/{dungeonIndex}/books/{bookIndex}")
  public BookDTO updateNPCFromDungeon(
    @PathVariable int courseId,
    @PathVariable int worldIndex,
    @PathVariable int dungeonIndex,
    @PathVariable int bookIndex,
    @RequestBody BookDTO bookDTO,
    @CookieValue("access_token") final String accessToken
  ) {
    jwtValidatorService.validateTokenOrThrow(accessToken);
    jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
    log.debug(
      "update book {} of dungeon {} from world {} of course {} with {}",
      bookIndex,
      dungeonIndex,
      worldIndex,
      courseId,
      bookDTO
    );
    return bookService.updateBookFromDungeon(courseId, worldIndex, dungeonIndex, bookIndex, bookDTO);
  }
}
