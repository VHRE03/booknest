package com.vhre.booknest.modules.author.service;

import com.vhre.booknest.common.exception.DuplicateResourceException;
import com.vhre.booknest.common.exception.ResourceNotFoundException;
import com.vhre.booknest.modules.author.dto.AuthorDTO;
import com.vhre.booknest.modules.author.entity.Author;
import com.vhre.booknest.modules.author.mapper.AuthorMapper;
import com.vhre.booknest.modules.author.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of the {@link AuthorService} contract.
 * Contains the plain CRUD logic that the base library used to provide,
 * written explicitly for this module.
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    public AuthorServiceImpl(AuthorRepository repository, AuthorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));
    }

    @Override
    @Transactional
    public AuthorDTO save(AuthorDTO dto) {
        validateUniqueEmail(dto.getEmail(), null);
        Author author = mapper.toEntity(dto);
        return mapper.toDto(repository.save(author));
    }

    @Override
    @Transactional
    public AuthorDTO update(UUID id, AuthorDTO dto) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found for update with ID: " + id));
        validateUniqueEmail(dto.getEmail(), id);
        mapper.updateEntityFromDto(dto, author);
        return mapper.toDto(repository.save(author));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found for deletion with ID: " + id));
        repository.delete(author);
    }

    /**
     * Business rule: the email must be unique among authors.
     *
     * @param email     the email to validate
     * @param currentId the author being updated (null on create), excluded from the check
     */
    private void validateUniqueEmail(String email, UUID currentId) {
        boolean duplicated = currentId == null
                ? repository.existsByEmail(email)
                : repository.existsByEmailAndIdNot(email, currentId);
        if (duplicated) {
            throw new DuplicateResourceException("An author with the email '" + email + "' already exists");
        }
    }
}
