package com.vhre.booknest.modules.review.mapper;

import com.vhre.booknest.modules.review.dto.ReviewDTO;
import com.vhre.booknest.modules.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * MapStruct mapper between the {@link Review} entity and its {@link ReviewDTO}.
 *
 * <p>Relations travel by reference: the entity's {@code book} is mapped to
 * the DTO's {@code bookId} and back. The {@code book} field is ignored
 * when mapping DTOs into entities because the service resolves and sets it
 * explicitly (validating that the book exists).</p>
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper {

    /**
     * Converts an entity into its DTO representation (the relation is
     * flattened into the {@code bookId} field).
     */
    @Mapping(target = "bookId", source = "book.id")
    ReviewDTO toDto(Review review);

    /**
     * Converts a list of entities into a list of DTOs.
     */
    List<ReviewDTO> toDtos(List<Review> reviews);

    /**
     * Converts a DTO into a new entity (for create operations).
     * The book must be set by the service from the {@code bookId}.
     */
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Review toEntity(ReviewDTO dto);

    /**
     * Updates an existing managed entity with data from a DTO (for update
     * operations), preserving the fields owned by the persistence layer.
     */
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(ReviewDTO dto, @MappingTarget Review review);
}
