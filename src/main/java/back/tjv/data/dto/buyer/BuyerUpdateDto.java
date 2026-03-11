package back.tjv.data.dto.buyer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to update a buyer")
public record BuyerUpdateDto(
        @Schema(description = "Buyer full name", example = "Alice Johnson")
        @NotBlank String name,

        @Schema(description = "Buyer email address", example = "alice@example.com", format = "email")
        @Email @NotBlank String email
) {}
