package back.tjv.data.dto.buyer;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Buyer resource")
public record BuyerReadDto(
        @Schema(description = "Buyer ID", example = "1") Long id,
        @Schema(description = "Buyer full name", example = "Alice Johnson") String name,
        @Schema(description = "Buyer email address", example = "alice@example.com", format = "email") String email,
        @Schema(description = "Creation timestamp", example = "2035-01-12T09:30:00Z", format = "date-time") OffsetDateTime createdAt
) {}
