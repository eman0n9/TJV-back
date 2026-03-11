package back.tjv.data.dto.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

@Schema(description = "Payload to update a ticket")
public record TicketUpdateDto(
        @Schema(description = "Seat ID (Row+Number)", example = "B7")
        String seatId,

        @Schema(description = "New price (> 0.00)", example = "150.00", minimum = "0.01")
        @DecimalMin(value = "0.00", inclusive = false) BigDecimal price
) {}
