package back.tjv.data.dto.speaker;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Speaker resource")
public record SpeakerReadDto(
        @Schema(description = "Speaker ID", example = "5") Long id,
        @Schema(description = "Speaker full name", example = "Dr. Jane Smith") String name,
        @Schema(description = "Photo URL", example = "https://cdn.example.com/img/jane.jpg") String photo,
        @Schema(description = "Role or title", example = "Keynote Speaker") String role
) {}
