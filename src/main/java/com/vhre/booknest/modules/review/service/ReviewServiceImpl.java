package com.vhre.booknest.modules.review.service;

import com.vhre.booknest.common.exception.ResourceNotFoundException;
import com.vhre.booknest.modules.book.entity.Book;
import com.vhre.booknest.modules.book.repository.BookRepository;
import com.vhre.booknest.modules.review.dto.ReviewDTO;
import com.vhre.booknest.modules.review.entity.Review;
import com.vhre.booknest.modules.review.mapper.ReviewMapper;
import com.vhre.booknest.modules.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the {@link ReviewService} contract.
 * Resolves the Book - Review relation manually: the {@code bookId} of the
 * payload is validated and converted into a managed JPA reference.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final BookRepository bookRepository;
    private final ReviewMapper mapper;

    public ReviewServiceImpl(ReviewRepository repository, BookRepository bookRepository, ReviewMapper mapper) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> findByBook(UUID bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with ID: " + bookId);
        }
        return mapper.toDtos(repository.findByBookId(bookId));
    }

    @Override
    @Transactional
    public ReviewDTO save(ReviewDTO dto) {
        Review review = mapper.toEntity(dto);
        review.setBook(resolveBook(dto.getBookId()));
        return mapper.toDto(repository.save(review));
    }

    @Override
    @Transactional
    public ReviewDTO update(UUID id, ReviewDTO dto) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found for update with ID: " + id));
        mapper.updateEntityFromDto(dto, review);
        review.setBook(resolveBook(dto.getBookId()));
        return mapper.toDto(repository.save(review));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found for deletion with ID: " + id));
        repository.delete(review);
    }

    /**
     * Resolves the {@code bookId} of the payload into a managed {@link Book}.
     *
     * @param bookId the book identifier sent by the client
     * @return the managed book entity
     * @throws ResourceNotFoundException if no book exists with the given id
     */
    private Book resolveBook(UUID bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));
    }
}
