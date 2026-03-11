package back.tjv.business.impl;


import back.tjv.business.service.EventService;
import back.tjv.business.spec.EventSpecs;
import back.tjv.data.dto.event.EventCreateDto;
import back.tjv.data.dto.event.EventReadDto;
import back.tjv.data.dto.event.EventSearchFilter;
import back.tjv.data.dto.event.EventUpdateDto;
import back.tjv.data.entity.Event;
import back.tjv.data.entity.Speaker;
import back.tjv.data.mapper.EventMapper;
import back.tjv.data.repository.EventRepository;
import back.tjv.data.repository.SpeakerRepository;
import back.tjv.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final SpeakerRepository speakerRepository;

    public EventServiceImpl(EventRepository eventRepository,
                            SpeakerRepository speakerRepository) {
        this.eventRepository = eventRepository;
        this.speakerRepository = speakerRepository;
    }

    @Override
    public EventReadDto create(EventCreateDto dto) {
        Event e = EventMapper.fromCreateDto(dto);

        if (dto.speakerIds() != null && !dto.speakerIds().isEmpty()) {
            var speakers = new ArrayList<Speaker>();
            speakerRepository.findAllById(dto.speakerIds()).forEach(speakers::add);

            if (speakers.size() != new HashSet<>(dto.speakerIds()).size()) {
                throw new NotFoundException("One or more speakers not found by ids: " + dto.speakerIds());
            }

            e.getSpeakers().clear();
            speakers.forEach(e::addSpeaker);
        }

        Event saved = eventRepository.save(e);
        return EventMapper.toReadDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EventReadDto get(Long id) {
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found: id=" + id));
        return EventMapper.toReadDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventReadDto> list() {
        var result = new ArrayList<EventReadDto>();
        eventRepository.findAll().forEach(ev -> result.add(EventMapper.toReadDto(ev)));
        return result;
    }

    @Override
    public EventReadDto update(Long id, EventUpdateDto dto) {
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found: id=" + id));

        EventMapper.applyUpdate(e, dto);

        if (dto.speakerIds() != null) {
            var speakers = new ArrayList<Speaker>();
            speakerRepository.findAllById(dto.speakerIds()).forEach(speakers::add);

            if (speakers.size() != new HashSet<>(dto.speakerIds()).size()) {
                throw new NotFoundException("One or more speakers not found by ids: " + dto.speakerIds());
            }

            e.getSpeakers().clear();
            speakers.forEach(e::addSpeaker);
        }

        Event saved = eventRepository.save(e);
        return EventMapper.toReadDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Event not found: id=" + id);
        }
        eventRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventReadDto> search(EventSearchFilter filter) {
        return eventRepository.findAll(EventSpecs.byFilter(filter))
                .stream()
                .map(EventMapper::toReadDto)
                .toList();
    }
}
