package back.tjv.data.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Payload to update an event")
public record EventUpdateDto(
        @Schema(description = "Event title", example = "Keynote Speakers")
        @NotBlank String title,

        @Schema(description = "Event description", example = "Main keynote of the conference")
        @NotBlank String description,

        @Schema(description = "Venue name", example = "Main Auditorium")
        @NotBlank String venue,

        @Schema(description = "Start date-time in UTC", example = "2035-01-12T10:00:00Z", format = "date-time")
        @NotNull OffsetDateTime startAt,

        @Schema(description = "End date-time in UTC", example = "2035-01-12T12:00:00Z", format = "date-time")
        @NotNull OffsetDateTime endAt,

        @Schema(description = "Ticket base price for the event", example = "120.00", minimum = "0")
        @NotNull BigDecimal price,

        @Schema(description = "Speakers assigned to the event (IDs)", example = "[1,2,3]")
        List<Long> speakerIds
) {}
