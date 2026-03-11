package back.tjv.controller;

import back.tjv.business.service.TicketService;
import back.tjv.data.dto.speaker.SpeakerReadDto;
import back.tjv.data.dto.ticket.TicketCreateDto;
import back.tjv.data.dto.ticket.TicketReadDto;
import back.tjv.data.dto.ticket.TicketUpdateDto;
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

@Tag(name = "Tickets", description = "CRUD operations for tickets")
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Create a ticket")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(schema = @Schema(implementation = TicketReadDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    @ApiResponse(responseCode = "404", description = "Event or buyer not found", content = @Content)
    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    @PostMapping
    public ResponseEntity<TicketReadDto> create(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New ticket payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TicketCreateDto.class))
            )
            TicketCreateDto dto) {
        TicketReadDto created = ticketService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Get ticket by ID")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = TicketReadDto.class)))
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @GetMapping("/{id}")
    public TicketReadDto get(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable Long id) {
        return ticketService.get(id);
    }

    @Operation(summary = "List tickets")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketReadDto.class))))
    @GetMapping
    public List<TicketReadDto> list() {
        return ticketService.list();
    }


    @Operation(summary = "Update ticket")
    @ApiResponse(responseCode = "200", description = "Updated",
            content = @Content(schema = @Schema(implementation = TicketReadDto.class)))
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    @PutMapping("/{id}")
    public TicketReadDto update(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Ticket update payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TicketUpdateDto.class))
            )
            TicketUpdateDto dto) {
        return ticketService.update(id, dto);
    }

    @Operation(summary = "Delete ticket")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable Long id) {
        ticketService.delete(id);
    }

    @Operation(summary = "List speakers related to the ticket's event")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SpeakerReadDto.class))))
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @GetMapping("/{id}/speakers")
    public List<SpeakerReadDto> listSpeakers(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable Long id) {
        return ticketService.listSpeakers(id);
    }

}
