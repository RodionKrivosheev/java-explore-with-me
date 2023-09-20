package ru.practicum.events.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.events.model.dto.EventFullDto;
import ru.practicum.events.model.dto.EventShortDto;
import ru.practicum.events.model.dto.NewEventDto;
import ru.practicum.events.model.entity.Event;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.users.mapper.UserMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class EventMapper {

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public Set<EventShortDto> toEventShortDtoSet(Iterable<Event> events) {
        Set<EventShortDto> result = new HashSet<>();

        for (Event e : events) {
            result.add(EventMapper.toEventShortDto(e));
        }

        return result;
    }

    public List<EventShortDto> toEventShortDto(Iterable<Event> events) {
        List<EventShortDto> result = new ArrayList<>();

        for (Event e : events) {
            result.add(EventMapper.toEventShortDto(e));
        }

        return result;
    }

    public Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }
}
