package com.vhre.booknest.modules.book.repository;

import com.vhre.booknest.modules.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for the {@link Book} entity.
 * Derived queries support the business rules (unique ISBN) and the
 * Author - Book relation.
 */
public interface BookRepository extends JpaRepository<Book, UUID> {

    /**
     * Checks whether a book with the given ISBN already exists.
     *
     * @param isbn the ISBN to look for
     * @return true if another book already uses the ISBN
     */
    boolean existsByIsbn(String isbn);

    /**
     * Checks whether a book other than the one identified by {@code id}
     * already uses the given ISBN (used on updates).
     *
     * @param isbn the ISBN to look for
     * @param id   the book being updated (excluded from the check)
     * @return true if some other book already uses the ISBN
     */
    boolean existsByIsbnAndIdNot(String isbn, UUID id);

    /**
     * Returns all books written by the given author.
     *
     * @param authorId the author identifier
     * @return the list of books owned by the author
     */
    List<Book> findByAuthorId(UUID authorId);
}
