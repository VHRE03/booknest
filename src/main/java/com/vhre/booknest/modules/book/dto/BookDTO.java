package com.vhre.booknest.modules.book.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.vhre.booknest.modules.book.enums.BookGenre;
import com.vhre.booknest.modules.book.enums.BookLanguage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object representing a Book.
 * Relations travel by reference: only the {@code authorId} is exchanged.
 */
@Data
@Schema(description = "Data Transfer Object representing a Book")
@JsonPropertyOrder({
        "id",
        "title",
        "isbn",
        "synopsis",
        "genre",
        "price",
        "publishedDate",
        "pages",
        "stock",
        "available",
        "language",
        "authorId",
        "createdAt",
        "updatedAt"
})
public class BookDTO {

    @Schema(
            description = "Unique identifier of the book (UUID). Automatically generated.",
            example = "123e4567-e89b-12d3-a456-426614174000",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Null(message = "The ID must be null when creating a new resource")
    private UUID id;

    @Schema(description = "Title of the book", example = "Cien anos de soledad")
    @NotBlank(message = "The title is mandatory")
    @Size(max = 150, message = "The title must not exceed 150 characters")
    private String title;

    @Schema(description = "International Standard Book Number (unique in the system)", example = "978-3-16-148410-0")
    @NotBlank(message = "The ISBN is mandatory")
    @Size(max = 20, message = "The ISBN must not exceed 20 characters")
    private String isbn;

    @Schema(description = "Synopsis of the book", example = "The story of the Buendia family over seven generations in Macondo.")
    @Size(max = 8000, message = "The synopsis must not exceed 8000 characters")
    private String synopsis;

    @Schema(description = "Literary genre of the book (FICTION, MYSTERY, THRILLER, SCIENCE_FICTION, FANTASY, HORROR, ROMANCE, HISTORICAL_NOVEL, BIOGRAPHY, POETRY, CHILDREN, OTHER)", example = "FICTION")
    @NotNull(message = "The genre is mandatory")
    private BookGenre genre;

    @Schema(description = "Sale price of the book", example = "349.99")
    @NotNull(message = "The price is mandatory")
    @DecimalMin(value = "0.00", message = "The price must be zero or greater")
    @Digits(integer = 8, fraction = 2, message = "The price must have at most 8 integer digits and 2 decimals")
    private BigDecimal price;

    @Schema(description = "Date when the book was published", example = "1967-05-30")
    @NotNull(message = "The published date is mandatory")
    @PastOrPresent(message = "The published date must be in the past or today")
    private LocalDate publishedDate;

    @Schema(description = "Number of pages of the book", example = "417")
    @NotNull(message = "The pages count is mandatory")
    @Min(value = 1, message = "The pages count must be at least 1")
    private Integer pages;

    @Schema(description = "Units available in stock", example = "25")
    @NotNull(message = "The stock is mandatory")
    @Min(value = 0, message = "The stock must be zero or greater")
    private Integer stock;

    @Schema(description = "Flag indicating if the book is available for sale", example = "true")
    @NotNull(message = "The available status is mandatory")
    private Boolean available;

    @Schema(description = "Language the book is written in (SPANISH, ENGLISH, FRENCH, GERMAN, ITALIAN, PORTUGUESE, OTHER)", example = "SPANISH")
    @NotNull(message = "The language is mandatory")
    private BookLanguage language;

    @Schema(description = "ID of the author who wrote the book", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull(message = "The author ID is mandatory")
    private UUID authorId;

    @Schema(
            description = "Timestamp indicating when the book was created.",
            example = "2026-10-14T10:00:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Timestamp indicating the last time the book was updated.",
            example = "2026-10-14T14:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime updatedAt;
}
