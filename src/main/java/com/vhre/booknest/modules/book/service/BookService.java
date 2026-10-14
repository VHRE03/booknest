package com.vhre.booknest.modules.book.service;

import com.vhre.booknest.modules.book.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Service contract for the Book module.
 */
public interface BookService {

    /**
     * Returns a paginated list of books.
     *
     * @param pageable pagination and sorting parameters
     * @return the requested page of books
     */
    Page<BookDTO> findAll(Pageable pageable);

    /**
     * Returns the book identified by the given id.
     *
     * @param id the book identifier
     * @return the book DTO
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the book does not exist
     */
    BookDTO findById(UUID id);

    /**
     * Returns all the books written by a specific author.
     *
     * @param authorId the author identifier
     * @return the list of books of the author
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the author does not exist
     */
    List<BookDTO> findByAuthor(UUID authorId);

    /**
     * Creates a new book from the given payload.
     *
     * @param dto the book data
     * @return the created book DTO
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the referenced author does not exist
     * @throws com.vhre.booknest.common.exception.DuplicateResourceException if the ISBN is already used
     */
    BookDTO save(BookDTO dto);

    /**
     * Updates an existing book with the given payload.
     *
     * @param id  the book identifier
     * @param dto the book data
     * @return the updated book DTO
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the book or the referenced author do not exist
     * @throws com.vhre.booknest.common.exception.DuplicateResourceException if the ISBN is already used by another book
     */
    BookDTO update(UUID id, BookDTO dto);

    /**
     * Deletes the book identified by the given id.
     *
     * @param id the book identifier
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the book does not exist
     */
    void delete(UUID id);
}
