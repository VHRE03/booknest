package com.vhre.booknest.modules.author.service;

import com.vhre.booknest.modules.author.dto.AuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service contract for the Author module.
 */
public interface AuthorService {

    /**
     * Returns a paginated list of authors.
     *
     * @param pageable pagination and sorting parameters
     * @return the requested page of authors
     */
    Page<AuthorDTO> findAll(Pageable pageable);

    /**
     * Returns the author identified by the given id.
     *
     * @param id the author identifier
     * @return the author DTO
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the author does not exist
     */
    AuthorDTO findById(UUID id);

    /**
     * Creates a new author from the given payload.
     *
     * @param dto the author data
     * @return the created author DTO
     * @throws com.vhre.booknest.common.exception.DuplicateResourceException if the email is already used
     */
    AuthorDTO save(AuthorDTO dto);

    /**
     * Updates an existing author with the given payload.
     *
     * @param id  the author identifier
     * @param dto the author data
     * @return the updated author DTO
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the author does not exist
     * @throws com.vhre.booknest.common.exception.DuplicateResourceException if the email is already used by another author
     */
    AuthorDTO update(UUID id, AuthorDTO dto);

    /**
     * Deletes the author identified by the given id.
     *
     * @param id the author identifier
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the author does not exist
     */
    void delete(UUID id);
}
