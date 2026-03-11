package back.tjv.data.dto.speaker;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to create a speaker")
public record SpeakerCreateDto(
        @Schema(description = "Speaker full name", example = "Dr. Jane Smith")
        @NotBlank String name,

        @Schema(description = "Photo URL (optional)", example = "https://cdn.example.com/img/jane.jpg")
        String photo,

        @Schema(description = "Role or title", example = "Keynote Speaker")
        String role
) {}
