package com.vhre.booknest.modules.author.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.vhre.booknest.modules.author.enums.Nationality;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object representing an Author.
 * Used for both incoming payloads (create/update) and outgoing responses.
 */
@Data
@Schema(description = "Data Transfer Object representing an Author")
@JsonPropertyOrder({
        "id",
        "fullName",
        "pseudonym",
        "email",
        "biography",
        "birthDate",
        "nationality",
        "awards",
        "active",
        "createdAt",
        "updatedAt"
})
public class AuthorDTO {

    @Schema(
            description = "Unique identifier of the author (UUID). Automatically generated.",
            example = "123e4567-e89b-12d3-a456-426614174000",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Null(message = "The ID must be null when creating a new resource")
    private UUID id;

    @Schema(description = "Full name of the author", example = "Gabriel Garcia Marquez")
    @NotBlank(message = "The full name is mandatory")
    @Size(max = 120, message = "The full name must not exceed 120 characters")
    private String fullName;

    @Schema(description = "Literary pseudonym of the author, if any", example = "Gabo")
    @Size(max = 120, message = "The pseudonym must not exceed 120 characters")
    private String pseudonym;

    @Schema(description = "Contact email of the author (unique in the system)", example = "gabo@booknest.com")
    @NotBlank(message = "The email is mandatory")
    @Email(message = "The email must be a valid email address")
    @Size(max = 150, message = "The email must not exceed 150 characters")
    private String email;

    @Schema(description = "Biography of the author", example = "Colombian novelist, Nobel Prize in Literature 1982.")
    @Size(max = 5000, message = "The biography must not exceed 5000 characters")
    private String biography;

    @Schema(description = "Birth date of the author", example = "1927-03-06")
    @NotNull(message = "The birth date is mandatory")
    @Past(message = "The birth date must be in the past")
    private LocalDate birthDate;

    @Schema(description = "Nationality of the author (MEXICAN, SPANISH, ARGENTINE, COLOMBIAN, PERUVIAN, CHILEAN, AMERICAN, BRITISH, FRENCH, GERMAN, ITALIAN, OTHER)", example = "COLOMBIAN")
    @NotNull(message = "The nationality is mandatory")
    private Nationality nationality;

    @Schema(description = "Number of literary awards won by the author", example = "3")
    @NotNull(message = "The awards count is mandatory")
    @Min(value = 0, message = "The awards count must be zero or greater")
    private Integer awards;

    @Schema(description = "Flag indicating if the author is currently active", example = "true")
    @NotNull(message = "The active status is mandatory")
    private Boolean active;

    @Schema(
            description = "Timestamp indicating when the author was created.",
            example = "2026-10-14T10:00:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Timestamp indicating the last time the author was updated.",
            example = "2026-10-14T14:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime updatedAt;
}
