package com.vhre.booknest.modules.review.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object representing a Review.
 * Relations travel by reference: only the {@code bookId} is exchanged.
 */
@Data
@Schema(description = "Data Transfer Object representing a Review")
@JsonPropertyOrder({
        "id",
        "reviewerName",
        "rating",
        "comment",
        "wouldRecommend",
        "helpfulVotes",
        "bookId",
        "createdAt",
        "updatedAt"
})
public class ReviewDTO {

    @Schema(
            description = "Unique identifier of the review (UUID). Automatically generated.",
            example = "123e4567-e89b-12d3-a456-426614174000",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Null(message = "The ID must be null when creating a new resource")
    private UUID id;

    @Schema(description = "Name of the person who wrote the review", example = "Ana Lopez")
    @NotBlank(message = "The reviewer name is mandatory")
    @Size(max = 100, message = "The reviewer name must not exceed 100 characters")
    private String reviewerName;

    @Schema(description = "Rating given to the book, from 1 to 5", example = "5", minimum = "1", maximum = "5")
    @NotNull(message = "The rating is mandatory")
    @Min(value = 1, message = "The rating must be at least 1")
    @Max(value = 5, message = "The rating must be at most 5")
    private Integer rating;

    @Schema(description = "Text of the review", example = "A masterpiece of Latin American literature.")
    @NotBlank(message = "The comment is mandatory")
    @Size(max = 2000, message = "The comment must not exceed 2000 characters")
    private String comment;

    @Schema(description = "Flag indicating if the reviewer would recommend the book", example = "true")
    @NotNull(message = "The wouldRecommend status is mandatory")
    private Boolean wouldRecommend;

    @Schema(description = "Number of users who found the review helpful", example = "42")
    @NotNull(message = "The helpful votes count is mandatory")
    @Min(value = 0, message = "The helpful votes count must be zero or greater")
    private Integer helpfulVotes;

    @Schema(description = "ID of the book being reviewed", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull(message = "The book ID is mandatory")
    private UUID bookId;

    @Schema(
            description = "Timestamp indicating when the review was created.",
            example = "2026-10-14T10:00:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Timestamp indicating the last time the review was updated.",
            example = "2026-10-14T14:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime updatedAt;
}
