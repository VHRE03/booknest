package com.vhre.booknest.modules.book.mapper;

import com.vhre.booknest.modules.book.dto.BookDTO;
import com.vhre.booknest.modules.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * MapStruct mapper between the {@link Book} entity and its {@link BookDTO}.
 *
 * <p>Relations travel by reference: the entity's {@code author} is mapped to
 * the DTO's {@code authorId} and back. The {@code author} field is ignored
 * when mapping DTOs into entities because the service resolves and sets it
 * explicitly (validating that the author exists).</p>
 */
@Mapper(componentModel = "spring")
public interface BookMapper {

    /**
     * Converts an entity into its DTO representation (the relation is
     * flattened into the {@code authorId} field).
     */
    @Mapping(target = "authorId", source = "author.id")
    BookDTO toDto(Book book);

    /**
     * Converts a list of entities into a list of DTOs.
     */
    List<BookDTO> toDtos(List<Book> books);

    /**
     * Converts a DTO into a new entity (for create operations).
     * The author must be set by the service from the {@code authorId}.
     */
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Book toEntity(BookDTO dto);

    /**
     * Updates an existing managed entity with data from a DTO (for update
     * operations), preserving the fields owned by the persistence layer.
     */
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(BookDTO dto, @MappingTarget Book book);
}
