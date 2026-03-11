package back.tjv.data.dto.ticket;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public final class TicketSeatDtos {
    private TicketSeatDtos() {}

    @Schema(description = "Seat in the hall")
    public record SeatDto(
            @Schema(description = "Seat identifier (Row+Number)", example = "A5")
            String id,
            @Schema(description = "Seat status", example = "available", allowableValues = {"available","sold"})
            String status) {}

    @Schema(description = "Hall map with seats")
    public record SeatMapDto(
            @Schema(example = "10") int rows,
            @Schema(example = "12") int cols,
            @ArraySchema(schema = @Schema(implementation = SeatDto.class))
            List<SeatDto> seats) {}

    @Schema(description = "List of seats request")
    public record SeatsReq(
            @ArraySchema(arraySchema = @Schema(description = "Seat IDs"), schema = @Schema(example = "A5"))
            List<String> seats) {}

    @Schema(description = "Seat availability response")
    public record ValidateResp(
            @Schema(example = "true") boolean ok,
            @ArraySchema(schema = @Schema(example = "B7"))
            List<String> unavailable) {}

    @Schema(description = "Seat purchase request")
    public record PurchaseReq(
            @Schema(description = "Buyer ID", example = "1") Long buyerId,
            @ArraySchema(schema = @Schema(example = "C10"))
            List<String> seats) {}
}
