package back.tjv.business.impl;


import back.tjv.business.service.SpeakerService;
import back.tjv.data.dto.event.EventReadDto;
import back.tjv.data.dto.speaker.SpeakerCreateDto;
import back.tjv.data.dto.speaker.SpeakerReadDto;
import back.tjv.data.dto.speaker.SpeakerUpdateDto;
import back.tjv.data.entity.Speaker;
import back.tjv.data.mapper.EventMapper;
import back.tjv.data.mapper.SpeakerMapper;
import back.tjv.data.repository.EventRepository;
import back.tjv.data.repository.SpeakerRepository;
import back.tjv.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SpeakerServiceImpl implements SpeakerService {

    private final SpeakerRepository speakerRepository;
    private final EventRepository eventRepository;

    public SpeakerServiceImpl(SpeakerRepository speakerRepository,
                              EventRepository eventRepository) {
        this.speakerRepository = speakerRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public SpeakerReadDto create(SpeakerCreateDto dto) {
        Speaker entity = SpeakerMapper.fromCreateDto(dto);
        Speaker saved = speakerRepository.save(entity);
        return SpeakerMapper.toReadDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SpeakerReadDto get(Long id) {
        Speaker e = speakerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Speaker not found: id=" + id));
        return SpeakerMapper.toReadDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpeakerReadDto> list() {
        var out = new ArrayList<SpeakerReadDto>();
        speakerRepository.findAll().forEach(s -> out.add(SpeakerMapper.toReadDto(s)));
        return out;
    }

    @Override
    public SpeakerReadDto update(Long id, SpeakerUpdateDto dto) {
        Speaker e = speakerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Speaker not found: id=" + id));

        SpeakerMapper.applyUpdate(e, dto);
        Speaker saved = speakerRepository.save(e);
        return SpeakerMapper.toReadDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!speakerRepository.existsById(id)) {
            throw new NotFoundException("Speaker not found: id=" + id);
        }
        speakerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventReadDto> listEvents(Long speakerId) {
        if (!speakerRepository.existsById(speakerId)) {
            throw new NotFoundException("Speaker not found: id=" + speakerId);
        }
        return eventRepository.findBySpeakers_Id(speakerId)
                .stream()
                .map(EventMapper::toReadDto)
                .toList();
    }
}
