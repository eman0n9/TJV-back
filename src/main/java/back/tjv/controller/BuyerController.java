package back.tjv.controller;

import back.tjv.business.service.BuyerService;
import back.tjv.business.service.TicketService;
import back.tjv.data.dto.buyer.BuyerCreateDto;
import back.tjv.data.dto.buyer.BuyerReadDto;
import back.tjv.data.dto.buyer.BuyerUpdateDto;
import back.tjv.data.dto.event.EventReadDto;
import back.tjv.data.dto.ticket.TicketReadDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Buyers", description = "CRUD operations for buyers")
@RestController
@RequestMapping("/api/buyers")
public class BuyerController {

    private final BuyerService buyerService;
    private final TicketService ticketService;

    public BuyerController(BuyerService buyerService, TicketService ticketService) {
        this.buyerService = buyerService;
        this.ticketService = ticketService;
    }

    @Operation(summary = "Create a buyer")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(schema = @Schema(implementation = BuyerReadDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    @PostMapping
    public ResponseEntity<BuyerReadDto> create(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New buyer payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BuyerCreateDto.class))
            )
            BuyerCreateDto dto) {
        BuyerReadDto created = buyerService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Get buyer by ID")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = BuyerReadDto.class)))
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @GetMapping("/{id}")
    public BuyerReadDto get(
            @Parameter(description = "Buyer ID", required = true)
            @PathVariable Long id) {
        return buyerService.get(id);
    }

    @Operation(summary = "List buyers")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BuyerReadDto.class))))
    @GetMapping
    public List<BuyerReadDto> list() {
        return buyerService.list();
    }

    @Operation(summary = "Update buyer")
    @ApiResponse(responseCode = "200", description = "Updated",
            content = @Content(schema = @Schema(implementation = BuyerReadDto.class)))
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    @PutMapping("/{id}")
    public BuyerReadDto update(
            @Parameter(description = "Buyer ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Buyer update payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BuyerUpdateDto.class))
            )
            BuyerUpdateDto dto) {
        return buyerService.update(id, dto);
    }

    @Operation(summary = "Delete buyer")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Buyer ID", required = true)
            @PathVariable Long id) {
        buyerService.delete(id);
    }

    @Operation(summary = "List events for a buyer")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventReadDto.class))))
    @ApiResponse(responseCode = "404", description = "Buyer not found", content = @Content)
    @GetMapping("/{id}/events")
    public List<EventReadDto> listEvents(
            @Parameter(description = "Buyer ID", required = true)
            @PathVariable Long id) {
        return buyerService.listEvents(id);
    }

    @Operation(summary = "List tickets for a buyer")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketReadDto.class))))
    @ApiResponse(responseCode = "404", description = "Buyer not found", content = @Content)
    @GetMapping("/{id}/tickets")
    public List<TicketReadDto> listTickets(
            @Parameter(description = "Buyer ID", required = true)
            @PathVariable Long id) {
        return ticketService.listByBuyer(id);
    }
}
