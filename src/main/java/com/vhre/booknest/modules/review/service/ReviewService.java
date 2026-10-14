package com.vhre.booknest.modules.review.service;

import com.vhre.booknest.modules.review.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Service contract for the Review module.
 */
public interface ReviewService {

    /**
     * Returns a paginated list of reviews.
     *
     * @param pageable pagination and sorting parameters
     * @return the requested page of reviews
     */
    Page<ReviewDTO> findAll(Pageable pageable);

    /**
     * Returns the review identified by the given id.
     *
     * @param id the review identifier
     * @return the review DTO
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the review does not exist
     */
    ReviewDTO findById(UUID id);

    /**
     * Returns all the reviews written for a specific book.
     *
     * @param bookId the book identifier
     * @return the list of reviews of the book
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the book does not exist
     */
    List<ReviewDTO> findByBook(UUID bookId);

    /**
     * Creates a new review from the given payload.
     *
     * @param dto the review data
     * @return the created review DTO
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the referenced book does not exist
     */
    ReviewDTO save(ReviewDTO dto);

    /**
     * Updates an existing review with the given payload.
     *
     * @param id  the review identifier
     * @param dto the review data
     * @return the updated review DTO
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the review or the referenced book do not exist
     */
    ReviewDTO update(UUID id, ReviewDTO dto);

    /**
     * Deletes the review identified by the given id.
     *
     * @param id the review identifier
     * @throws com.vhre.booknest.common.exception.ResourceNotFoundException if the review does not exist
     */
    void delete(UUID id);
}
