package back.tjv.data.dto.ticket;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(description = "Ticket resource")
public record TicketReadDto(
        @Schema(description = "Ticket ID", example = "1001") Long id,
        @Schema(description = "Event ID", example = "10") Long eventId,
        @Schema(description = "Buyer ID", example = "1") Long buyerId,
        @Schema(description = "Final ticket price", example = "120.00") BigDecimal price,
        @Schema(description = "Purchase timestamp", example = "2035-01-12T09:45:00Z", format = "date-time") OffsetDateTime purchasedAt,
        @Schema(description = "Seat ID (Row+Number)", example = "A5") String seatId
) {}
