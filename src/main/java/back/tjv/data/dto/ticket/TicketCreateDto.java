package back.tjv.data.dto.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload to create a ticket")
public record TicketCreateDto(
        @Schema(description = "Event ID", example = "10")
        @NotNull Long eventId,

        @Schema(description = "Buyer ID", example = "1")
        @NotNull Long buyerId,

        @Schema(description = "Seat ID (Row+Number)", example = "A5")
        @NotBlank String seatId
) {}
