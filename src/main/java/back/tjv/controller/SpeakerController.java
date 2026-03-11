package back.tjv.controller;

import back.tjv.business.service.SpeakerService;
import back.tjv.data.dto.event.EventReadDto;
import back.tjv.data.dto.speaker.SpeakerCreateDto;
import back.tjv.data.dto.speaker.SpeakerReadDto;
import back.tjv.data.dto.speaker.SpeakerUpdateDto;
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

@Tag(name = "Speakers", description = "CRUD operations for speakers")
@RestController
@RequestMapping("/api/speakers")
public class SpeakerController {

    private final SpeakerService speakerService;

    public SpeakerController(SpeakerService speakerService) {
        this.speakerService = speakerService;
    }

    @Operation(summary = "Create a speaker")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(schema = @Schema(implementation = SpeakerReadDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    @PostMapping
    public ResponseEntity<SpeakerReadDto> create(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New speaker payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SpeakerCreateDto.class))
            )
            SpeakerCreateDto dto) {
        SpeakerReadDto created = speakerService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Get speaker by ID")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = SpeakerReadDto.class)))
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @GetMapping("/{id}")
    public SpeakerReadDto get(
            @Parameter(description = "Speaker ID", required = true)
            @PathVariable Long id) {
        return speakerService.get(id);
    }

    @Operation(summary = "List speakers")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SpeakerReadDto.class))))
    @GetMapping
    public List<SpeakerReadDto> list() {
        return speakerService.list();
    }

    @Operation(summary = "Update speaker")
    @ApiResponse(responseCode = "200", description = "Updated",
            content = @Content(schema = @Schema(implementation = SpeakerReadDto.class)))
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @PutMapping("/{id}")
    public SpeakerReadDto update(
            @Parameter(description = "Speaker ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Speaker update payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SpeakerUpdateDto.class))
            )
            SpeakerUpdateDto dto) {
        return speakerService.update(id, dto);
    }

    @Operation(summary = "Delete speaker")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Speaker ID", required = true)
            @PathVariable Long id) {
        speakerService.delete(id);
    }

    @Operation(summary = "List events for a speaker")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventReadDto.class))))
    @ApiResponse(responseCode = "404", description = "Speaker not found", content = @Content)
    @GetMapping("/{id}/events")
    public List<EventReadDto> listEvents(
            @Parameter(description = "Speaker ID", required = true)
            @PathVariable Long id) {
        return speakerService.listEvents(id);
    }

}
