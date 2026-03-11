package back.tjv.business.service;

import back.tjv.data.dto.event.EventCreateDto;
import back.tjv.data.dto.event.EventReadDto;
import back.tjv.data.dto.event.EventSearchFilter;
import back.tjv.data.dto.event.EventUpdateDto;

import java.util.List;

public interface EventService {
    EventReadDto create(EventCreateDto dto);
    EventReadDto get(Long id);
    List<EventReadDto> list();
    EventReadDto update(Long id, EventUpdateDto dto);
    void delete(Long id);
    List<EventReadDto> search(EventSearchFilter filter);
}
