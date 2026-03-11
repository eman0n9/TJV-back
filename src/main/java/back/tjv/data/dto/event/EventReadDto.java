package back.tjv.data.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Event resource")
public record EventReadDto(
        @Schema(description = "Event ID", example = "10") Long id,
        @Schema(description = "Event title", example = "Keynote Speakers") String title,
        @Schema(description = "Event description", example = "Main keynote of the conference") String description,
        @Schema(description = "Venue name", example = "Main Auditorium") String venue,
        @Schema(description = "Start date-time in UTC", example = "2035-01-12T10:00:00Z", format = "date-time") OffsetDateTime startAt,
        @Schema(description = "End date-time in UTC", example = "2035-01-12T12:00:00Z", format = "date-time") OffsetDateTime endAt,
        @Schema(description = "Ticket base price for the event", example = "120.00") BigDecimal price,
        @Schema(description = "Speakers assigned to the event (IDs)", example = "[1,2,3]") List<Long> speakerIds
) {}
