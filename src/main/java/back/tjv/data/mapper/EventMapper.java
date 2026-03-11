package back.tjv.data.mapper;

import back.tjv.data.dto.event.EventCreateDto;
import back.tjv.data.dto.event.EventReadDto;
import back.tjv.data.dto.event.EventUpdateDto;
import back.tjv.data.entity.Event;
import back.tjv.data.entity.Speaker;

import java.util.ArrayList;
import java.util.List;

public final class EventMapper {
    private EventMapper() {}

    public static EventReadDto toReadDto(Event e) {
        if (e == null) return null;
        List<Long> speakerIds = e.getSpeakers()
                .stream()
                .map(Speaker::getId)
                .toList();

        return new EventReadDto(
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getVenue(),
                e.getStartAt(),
                e.getEndAt(),
                e.getPrice(),
                new ArrayList<>(speakerIds)
        );
    }

    public static Event fromCreateDto(EventCreateDto d) {
        if (d == null) return null;
        Event e = new Event();
        e.setTitle(d.title());
        e.setDescription(d.description());
        e.setVenue(d.venue());
        e.setStartAt(d.startAt());
        e.setEndAt(d.endAt());
        e.setPrice(d.price());
        return e;
    }

    public static void applyUpdate(Event e, EventUpdateDto d) {
        if (e == null || d == null) return;
        e.setTitle(d.title());
        e.setDescription(d.description());
        e.setVenue(d.venue());
        e.setStartAt(d.startAt());
        e.setEndAt(d.endAt());
        e.setPrice(d.price());
    }
}