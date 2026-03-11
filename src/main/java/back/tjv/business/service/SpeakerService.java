package back.tjv.business.service;

import back.tjv.data.dto.speaker.SpeakerCreateDto;
import back.tjv.data.dto.speaker.SpeakerReadDto;
import back.tjv.data.dto.speaker.SpeakerUpdateDto;
import back.tjv.data.dto.event.EventReadDto;

import java.util.List;

public interface SpeakerService {
    SpeakerReadDto create(SpeakerCreateDto dto);
    SpeakerReadDto get(Long id);
    List<SpeakerReadDto> list();
    SpeakerReadDto update(Long id, SpeakerUpdateDto dto);
    void delete(Long id);


    List<EventReadDto> listEvents(Long speakerId);
}