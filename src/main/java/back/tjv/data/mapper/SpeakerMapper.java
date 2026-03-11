package back.tjv.data.mapper;

import back.tjv.data.dto.speaker.SpeakerCreateDto;
import back.tjv.data.dto.speaker.SpeakerReadDto;
import back.tjv.data.dto.speaker.SpeakerUpdateDto;
import back.tjv.data.entity.Speaker;

public final class SpeakerMapper {
    private SpeakerMapper() {}

    public static SpeakerReadDto toReadDto(Speaker e) {
        if (e == null) return null;
        return new SpeakerReadDto(
                e.getId(),
                e.getName(),
                e.getPhoto(),
                e.getRole()
        );
    }


    public static Speaker fromCreateDto(SpeakerCreateDto d) {
        if (d == null) return null;
        Speaker e = new Speaker();
        e.setName(d.name());
        e.setPhoto(d.photo());
        e.setRole(d.role());
        return e;
    }


    public static void applyUpdate(Speaker e, SpeakerUpdateDto d) {
        if (e == null || d == null) return;
        e.setName(d.name());
        e.setPhoto(d.photo());
        e.setRole(d.role());
    }
}
