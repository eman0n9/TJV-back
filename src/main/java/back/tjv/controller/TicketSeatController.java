package back.tjv.controller;

import back.tjv.business.service.TicketService;
import back.tjv.data.dto.ticket.TicketReadDto;
import back.tjv.data.dto.ticket.TicketSeatDtos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ticket Seats", description = "Hall layout, seat validation and purchase")
@RestController
@RequestMapping("/api/events")
public class TicketSeatController {

    private final TicketService ticketService;

    public TicketSeatController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Get hall seat map for an event")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = TicketSeatDtos.SeatMapDto.class)))
    @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    @GetMapping("/{eventId}/seats")
    public TicketSeatDtos.SeatMapDto seats(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long eventId) {
        return ticketService.getSeatMap(eventId);
    }

    @Operation(summary = "Check seat availability for an event")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = TicketSeatDtos.ValidateResp.class)))
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    @PostMapping("/{eventId}/seats/availability")
    public TicketSeatDtos.ValidateResp validate(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Seats to validate",
                    content = @Content(
                            schema = @Schema(implementation = TicketSeatDtos.SeatsReq.class),
                            examples = @ExampleObject(
                                    name = "Example",
                                    value = "{\"seats\":[\"A1\",\"A2\",\"B7\"]}"
                            )
                    )
            )
            @RequestBody TicketSeatDtos.SeatsReq req) {
        return ticketService.validateSeats(eventId, req);
    }

    @Operation(summary = "Purchase tickets for selected seats")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketReadDto.class))))
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Event or buyer not found", content = @Content)
    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    @PostMapping("/{eventId}/tickets")
    public ResponseEntity<List<TicketReadDto>> purchase(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Buyer and selected seats",
                    content = @Content(
                            schema = @Schema(implementation = TicketSeatDtos.PurchaseReq.class),
                            examples = @ExampleObject(
                                    name = "Purchase example",
                                    value = "{\"buyerId\":1,\"seats\":[\"A1\",\"A2\"]}"
                            )
                    )
            )
            @RequestBody TicketSeatDtos.PurchaseReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.purchaseSeats(eventId, req));
    }
}
