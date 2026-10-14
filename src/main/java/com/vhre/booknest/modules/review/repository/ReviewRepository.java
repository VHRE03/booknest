package com.vhre.booknest.modules.review.repository;

import com.vhre.booknest.modules.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for the {@link Review} entity.
 * Derived queries support the Book - Review relation.
 */
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    /**
     * Returns all the reviews written for the given book.
     *
     * @param bookId the book identifier
     * @return the list of reviews of the book
     */
    List<Review> findByBookId(UUID bookId);
}
