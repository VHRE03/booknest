package com.vhre.booknest.modules.author.repository;

import com.vhre.booknest.modules.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for the {@link Author} entity.
 * Derived queries support the business rules (unique email per author).
 */
public interface AuthorRepository extends JpaRepository<Author, UUID> {

    /**
     * Checks whether an author with the given email already exists.
     *
     * @param email the email to look for
     * @return true if another author already uses the email
     */
    boolean existsByEmail(String email);

    /**
     * Checks whether an author other than the one identified by {@code id}
     * already uses the given email (used on updates).
     *
     * @param email the email to look for
     * @param id    the author being updated (excluded from the check)
     * @return true if some other author already uses the email
     */
    boolean existsByEmailAndIdNot(String email, UUID id);
}
