package back.tjv.controller;

import back.tjv.business.service.EventService;
import back.tjv.business.service.TicketService;
import back.tjv.data.dto.event.EventCreateDto;
import back.tjv.data.dto.event.EventReadDto;
import back.tjv.data.dto.event.EventSearchFilter;
import back.tjv.data.dto.event.EventUpdateDto;
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


import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import java.net.URI;
import java.util.List;

@Tag(name = "Events", description = "CRUD operations for events")
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final TicketService ticketService;
    public EventController(EventService eventService, TicketService ticketService) {
        this.eventService = eventService;
        this.ticketService = ticketService;
    }

    @Operation(summary = "Create an event")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(schema = @Schema(implementation = EventReadDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    @PostMapping
    public ResponseEntity<EventReadDto> create(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New event payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EventCreateDto.class))
            )
            EventCreateDto dto) {
        EventReadDto created = eventService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Get event by ID")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = EventReadDto.class)))
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @GetMapping("/{id}")
    public EventReadDto get(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long id) {
        return eventService.get(id);
    }

    @Operation(summary = "List events")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventReadDto.class))))
    @GetMapping
    public List<EventReadDto> list(
            @Parameter(description = "Free-text search query")
            @RequestParam(required = false) String q,
            @Parameter(description = "Free-text search query (alias)")
            @RequestParam(required = false) String search,
            @Parameter(description = "Title filter")
            @RequestParam(required = false) String title,
            @Parameter(description = "Venue filter")
            @RequestParam(required = false) String venue,
            @Parameter(description = "Start time from (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startFrom,
            @Parameter(description = "Start time to (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startTo,
            @Parameter(description = "Minimum price (inclusive)")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price (inclusive)")
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        String query = (q != null && !q.isBlank()) ? q : (search != null && !search.isBlank() ? search : null);
        boolean hasFilters = query != null
                || (title != null && !title.isBlank())
                || (venue != null && !venue.isBlank())
                || startFrom != null
                || startTo != null
                || minPrice != null
                || maxPrice != null;
        if (hasFilters) {
            var filter = new EventSearchFilter(query, title, venue, startFrom, startTo, minPrice, maxPrice);
            return eventService.search(filter);
        }
        return eventService.list();
    }

    @Operation(summary = "Update event")
    @ApiResponse(responseCode = "200", description = "Updated",
            content = @Content(schema = @Schema(implementation = EventReadDto.class)))
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @PutMapping("/{id}")
    public EventReadDto update(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Event update payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EventUpdateDto.class))
            )
            EventUpdateDto dto) {
        return eventService.update(id, dto);
    }

    @Operation(summary = "Delete event")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long id) {
        eventService.delete(id);
    }

    @Operation(summary = "List tickets for an event")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketReadDto.class))))
    @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    @GetMapping("/{id}/tickets")
    public List<TicketReadDto> listTickets(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long id) {
        return ticketService.listByEvent(id);
    }
}
