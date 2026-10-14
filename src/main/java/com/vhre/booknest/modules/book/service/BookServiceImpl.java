package com.vhre.booknest.modules.book.service;

import com.vhre.booknest.common.exception.DuplicateResourceException;
import com.vhre.booknest.common.exception.ResourceNotFoundException;
import com.vhre.booknest.modules.author.entity.Author;
import com.vhre.booknest.modules.author.repository.AuthorRepository;
import com.vhre.booknest.modules.book.dto.BookDTO;
import com.vhre.booknest.modules.book.entity.Book;
import com.vhre.booknest.modules.book.mapper.BookMapper;
import com.vhre.booknest.modules.book.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the {@link BookService} contract.
 * Resolves the Author - Book relation manually: the {@code authorId} of the
 * payload is validated and converted into a managed JPA reference.
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final BookMapper mapper;

    public BookServiceImpl(BookRepository repository, AuthorRepository authorRepository, BookMapper mapper) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findByAuthor(UUID authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author not found with ID: " + authorId);
        }
        return mapper.toDtos(repository.findByAuthorId(authorId));
    }

    @Override
    @Transactional
    public BookDTO save(BookDTO dto) {
        validateUniqueIsbn(dto.getIsbn(), null);
        Book book = mapper.toEntity(dto);
        book.setAuthor(resolveAuthor(dto.getAuthorId()));
        return mapper.toDto(repository.save(book));
    }

    @Override
    @Transactional
    public BookDTO update(UUID id, BookDTO dto) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for update with ID: " + id));
        validateUniqueIsbn(dto.getIsbn(), id);
        mapper.updateEntityFromDto(dto, book);
        book.setAuthor(resolveAuthor(dto.getAuthorId()));
        return mapper.toDto(repository.save(book));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for deletion with ID: " + id));
        repository.delete(book);
    }

    /**
     * Business rule: the ISBN must be unique among books.
     *
     * @param isbn      the ISBN to validate
     * @param currentId the book being updated (null on create), excluded from the check
     */
    private void validateUniqueIsbn(String isbn, UUID currentId) {
        boolean duplicated = currentId == null
                ? repository.existsByIsbn(isbn)
                : repository.existsByIsbnAndIdNot(isbn, currentId);
        if (duplicated) {
            throw new DuplicateResourceException("A book with the ISBN '" + isbn + "' already exists");
        }
    }

    /**
     * Resolves the {@code authorId} of the payload into a managed {@link Author}.
     *
     * @param authorId the author identifier sent by the client
     * @return the managed author entity
     * @throws ResourceNotFoundException if no author exists with the given id
     */
    private Author resolveAuthor(UUID authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + authorId));
    }
}
