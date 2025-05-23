package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.Book;
import de.unistuttgart.overworldbackend.data.BookDTO;
import de.unistuttgart.overworldbackend.data.mapper.BookMapper;
import de.unistuttgart.overworldbackend.repositories.AchievementRepository;
import de.unistuttgart.overworldbackend.repositories.BookRepository;
import de.unistuttgart.overworldbackend.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WorldService worldService;

    @Autowired
    private DungeonService dungeonService;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AchievementService achievementService;

    /**
     * Get a book from a world by its index and a course by its id
     *
     * @param courseId   the id of the course
     * @param worldIndex the index of the world
     * @param bookIndex  the index of the book
     * @return the found book
     * @throws ResponseStatusException (404) if book not found
     */
    public Book getBookFromWorld(final int courseId, final int worldIndex, final int bookIndex) {
        return worldService
                .getWorldByIndexFromCourse(courseId, worldIndex)
                .getBooks()
                .parallelStream()
                .filter(book -> book.getIndex() == bookIndex)
                .findAny()
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("There is no book %d in world %d, course %d.", bookIndex, worldIndex, courseId)
                        )
                );
    }

    /**
     * Get a Book from a world by its index and a course by its id
     *
     * @param courseId     the id of the course
     * @param worldIndex   the index of the world
     * @param dungeonIndex the index of the dungeon
     * @param bookIndex    the index of the book
     * @return the found book
     * @throws ResponseStatusException (404) if book not found
     */
    public Book getBookFromDungeon(
            final int courseId,
            final int worldIndex,
            final int dungeonIndex,
            final int bookIndex
    ) {
        return dungeonService
                .getDungeonByIndexFromCourse(courseId, worldIndex, dungeonIndex)
                .getBooks()
                .parallelStream()
                .filter(book -> book.getIndex() == bookIndex)
                .findAny()
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format(
                                        "There is no book %d in dungeon %d, world %d, course %d.",
                                        bookIndex,
                                        dungeonIndex,
                                        worldIndex,
                                        courseId
                                )
                        )
                );
    }

    /**
     * Update a book by its id from a course and an area.
     * <p>
     * Only the text and description is updatable.
     *
     * @param courseId   the id of the course the book should be part of
     * @param worldIndex the index of the world
     * @param bookIndex  the index of the book
     * @param bookDTO    the updated parameters
     * @return the updated book as DTO
     * @throws ResponseStatusException (404) if book not found
     */
    public BookDTO updateBookFromWorld(
            final int courseId,
            final int worldIndex,
            final int bookIndex,
            final BookDTO bookDTO
    ) {
        final Book book = getBookFromWorld(courseId, worldIndex, bookIndex);
        book.setText(bookDTO.getText());
        book.setDescription(bookDTO.getDescription());
        final Book updatedBook = bookRepository.save(book);
        achievementService.updateBookAchievements(courseRepository.
                findById(courseId).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("There is no course with id %s.", courseId))
                ));
        return bookMapper.bookToBookDTO(updatedBook);
    }

    /**
     * Update a book by its id from a course and an area.
     * <p>
     * Only the text and description is updatable.
     *
     * @param courseId     the id of the course the book should be part of
     * @param worldIndex   the index of the world
     * @param dungeonIndex the index of the dungeon
     * @param bookIndex    the index of the book
     * @param bookDTO      the updated parameters
     * @return the updated book as DTO
     * @throws ResponseStatusException (404) if book not found
     */
    public BookDTO updateBookFromDungeon(
            final int courseId,
            final int worldIndex,
            final int dungeonIndex,
            final int bookIndex,
            final BookDTO bookDTO
    ) {
        final Book book = getBookFromDungeon(courseId, worldIndex, dungeonIndex, bookIndex);
        book.setText(bookDTO.getText());
        book.setDescription(bookDTO.getDescription());
        final Book updatedBook = bookRepository.save(book);
        return bookMapper.bookToBookDTO(updatedBook);
    }
}
