package ru.practicum.events.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.category.model.entity.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.enums.*;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.dto.*;
import ru.practicum.events.model.entity.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationRequestException;
import ru.practicum.location.LocationService;
import ru.practicum.location.model.entity.Location;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.ParticipationRequestDto;
import ru.practicum.requests.mapper.ParticipationRequestMapper;
import ru.practicum.requests.repository.ParticipationRequestRepository;
import ru.practicum.users.model.entity.User;
import ru.practicum.users.service.UserService;
import ru.practicum.util.pageable.OffsetPageRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor(onConstructor_ = {@Lazy})
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ParticipationRequestRepository participationRequestRepository;
    private final StatsClient statsClient;

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Event event = EventMapper.toEvent(newEventDto);

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationRequestException("Event date must not be before 2 hours from current time.");
        }

        Category category = categoryService.getCategoryModelById(newEventDto.getCategory());
        User user = userService.getUserModelById(userId);
        Location location = locationService.getLocation(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon());

        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(user);
        event.setState(EventState.PENDING);
        event.setLocation(location);

        log.info("Creating event with body={}", newEventDto);
        Event savedEvent = eventRepository.save(event);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(savedEvent);
        eventFullDto.setViews(0L);
        eventFullDto.setConfirmedRequests(0L);

        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        userService.userExists(userId);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event", eventId));

        log.info("Updating event with id={} with data={}", eventId, updateEventUserRequest);

        if (event.getState() != null && event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        if (updateEventUserRequest.getEventDate() != null
                && updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationRequestException("Event date must not be before 2 hours from current time.");
        }

        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(categoryService.getCategoryModelById(updateEventUserRequest.getCategory()));
        }

        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }

        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(locationService.getLocation(updateEventUserRequest.getLocation().getLat(),
                    updateEventUserRequest.getLocation().getLon()));
        }

        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction() == EventStateActionUser.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            } else if (updateEventUserRequest.getStateAction() == EventStateActionUser.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            }
        }

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return toEventFullDtoWithViewsAndConfirmedRequests(eventFullDto, event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Updating event with id={} with body={}", eventId, updateEventAdminRequest.toString());
        Event event = getEventModelById(eventId);

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationRequestException("The start date of the edited event must be no earlier than one hour from the date of publication");
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction() == EventStateActionAdmin.PUBLISH_EVENT) {
                if (event.getState() != EventState.PENDING) {
                    throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getState());
                }

                event.setPublishedOn(LocalDateTime.now());
                event.setState(EventState.PUBLISHED);
            } else if (updateEventAdminRequest.getStateAction() == EventStateActionAdmin.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictException("Event can only be reject if it hasn't been published yet");
                } else {
                    event.setState(EventState.CANCELED);
                }
            }
        }

        if (updateEventAdminRequest.getEventDate() != null
                && updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationRequestException("Event date must not be before 2 hours from current time.");
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }

        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryService.getCategoryModelById(updateEventAdminRequest.getCategory()));
        }

        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }

        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(locationService.getLocation(updateEventAdminRequest.getLocation().getLat(),
                    updateEventAdminRequest.getLocation().getLon()));
        }

        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }

        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }

        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }

        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        return toEventFullDtoWithViewsAndConfirmedRequests(eventFullDto, event);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userService.userExists(userId);
        Event event = getEventModelById(eventId);

        List<ParticipationRequest> requests = participationRequestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = EventRequestStatusUpdateResult.builder().build();

        List<ParticipationRequestDto> confirmedList = new ArrayList<>();
        List<ParticipationRequestDto> rejectedList = new ArrayList<>();

        if (requests.isEmpty()) {
            return eventRequestStatusUpdateResult;
        }

        if (!requests.stream()
                .map(ParticipationRequest::getStatus)
                .allMatch(RequestStatus.PENDING::equals)) {
            throw new ConflictException("Only requests that are pending can be changed.");
        }

        if (requests.size() != eventRequestStatusUpdateRequest.getRequestIds().size()) {
            throw new ConflictException("Some requests not found.");
        }

        int limitParticipants = event.getParticipantLimit();

        if (limitParticipants == 0 || !event.getRequestModeration()) {
            return eventRequestStatusUpdateResult;
        }

        Long countParticipants = participationRequestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED);

        if (countParticipants >= limitParticipants) {
            throw new ConflictException("The participant limit has been reached.");
        }

        if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
            for (ParticipationRequest participationRequest : requests) {
                participationRequest.setStatus(RequestStatus.REJECTED);
                rejectedList.add(ParticipationRequestMapper.toRequestDto(participationRequest));
            }
            eventRequestStatusUpdateResult.setRejectedRequests(rejectedList);
        } else {
            for (ParticipationRequest participationRequest : requests) {
                if (countParticipants < limitParticipants) {
                    participationRequest.setStatus(RequestStatus.CONFIRMED);
                    confirmedList.add(ParticipationRequestMapper.toRequestDto(participationRequest));
                    countParticipants++;
                } else {
                    participationRequest.setStatus(RequestStatus.REJECTED);
                    rejectedList.add(ParticipationRequestMapper.toRequestDto(participationRequest));
                }
            }
            eventRequestStatusUpdateResult.setConfirmedRequests(confirmedList);
            eventRequestStatusUpdateResult.setRejectedRequests(rejectedList);
        }

        participationRequestRepository.saveAll(requests);
        return eventRequestStatusUpdateResult;
    }

    @Override
    public Event getEventModelById(Long id) {
        return eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Event", id));
    }

    @Override
    public List<EventShortDto> getAllEventsByUserId(Long userId, Integer from, Integer size) {
        log.info("Getting all events by user with id={}", userId);
        Pageable pageable = OffsetPageRequest.of(from, size);

        userService.userExists(userId);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);
        Map<Long, Long> views = getViews(events);

        List<EventShortDto> eventShortDtoList = EventMapper.toEventShortDto(events);

        for (EventShortDto eventShortDto : eventShortDtoList) {
            eventShortDto.setViews(views.getOrDefault(eventShortDto.getId(), 0L));
            eventShortDto.setConfirmedRequests(confirmedRequests.getOrDefault(eventShortDto.getId(), 0L));
        }

        return eventShortDtoList;
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        userService.userExists(userId);

        log.info("Getting event with id={}", eventId);
        Event event = getEventModelById(eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return toEventFullDtoWithViewsAndConfirmedRequests(eventFullDto, event);
    }

    @Override
    public List<EventFullDto> getEventsByUserIds(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size) {
        checkStartIsBeforeEnd(rangeStart, rangeEnd);
        checkStates(states);

        SizeValidator.validateSize(size);
        Pageable pageable = OffsetPageRequest.of(from, size);

        log.info("Getting events by user ids={}", users);
        List<Event> events = eventRepository.findEventsByAdmin(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                pageable);

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);
        Map<Long, Long> views = getViews(events);

        List<EventFullDto> eventFullDtoList = events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());


        for (EventFullDto eventFullDto : eventFullDtoList) {
            eventFullDto.setViews(views.getOrDefault(eventFullDto.getId(), 0L));
            eventFullDto.setConfirmedRequests(confirmedRequests.getOrDefault(eventFullDto.getId(), 0L));
        }

        return eventFullDtoList;
    }

    @Override
    public List<EventShortDto> getEventsByParams(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSortOptions sort,
            Integer from,
            Integer size,
            HttpServletRequest request) {
        statsClient.saveEndpoint("ewm-main-service", request.getRequestURI(), request.getRemoteAddr());

        SizeValidator.validateSize(size);
        Pageable pageable = OffsetPageRequest.of(from, size);
        checkStartIsBeforeEnd(rangeStart, rangeEnd);

        log.info("Получаем ивент по text={}", text);
        List<Event> events = eventRepository.findPublishedEventsByUser(
                text,
                categories,
                paid,
                rangeStart != null ? rangeStart : LocalDateTime.now(),
                rangeEnd,
                pageable);

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);
        Map<Long, Long> views = getViews(events);

        List<EventShortDto> eventShortDtoList = EventMapper.toEventShortDto(events);

        Map<Long, Integer> eventsParticipantLimit = new HashMap<>();
        events.forEach(event -> eventsParticipantLimit.put(event.getId(), event.getParticipantLimit()));

        for (EventShortDto eventShortDto : eventShortDtoList) {
            eventShortDto.setViews(views.getOrDefault(eventShortDto.getId(), 0L));
            eventShortDto.setConfirmedRequests(confirmedRequests.getOrDefault(eventShortDto.getId(), 0L));
        }

        if (onlyAvailable) {
            eventShortDtoList = eventShortDtoList.stream()
                    .filter(eventShort -> (eventsParticipantLimit.get(eventShort.getId()) == 0 ||
                            eventsParticipantLimit.get(eventShort.getId()) > eventShort.getConfirmedRequests()))
                    .collect(Collectors.toList());
        }

        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    eventShortDtoList.sort(Comparator.comparing(EventShortDto::getEventDate));
                    break;
                case VIEWS:
                    eventShortDtoList.sort(Comparator.comparing(EventShortDto::getViews));
                    break;
                default:
                    throw new ValidationRequestException("Параметры сортировки не валидные");
            }
        }

        return eventShortDtoList;
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        userService.userExists(userId);

        log.info("Получаем requests по user с id={} и event с id={}", userId, eventId);
        List<Event> events = eventRepository.findByIdAndInitiatorId(eventId, userId);
        List<ParticipationRequest> requests = participationRequestRepository.findByEventIn(events);

        return ParticipationRequestMapper.toRequestDto(requests);
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        log.info("Получаем ивент с id={}", eventId);
        statsClient.saveEndpoint("ewm-main-service", request.getRequestURI(), request.getRemoteAddr());
        Event event = getEventModelById(eventId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event", eventId);
        }

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return toEventFullDtoWithViewsAndConfirmedRequests(eventFullDto, event);
    }

    private EventFullDto toEventFullDtoWithViewsAndConfirmedRequests(EventFullDto eventFullDto, Event event) {
        Map<Long, Long> confirmedRequests = getConfirmedRequests(List.of(event));
        Map<Long, Long> views = getViews(List.of(event));

        eventFullDto.setViews(views.getOrDefault(eventFullDto.getId(), 0L));
        eventFullDto.setConfirmedRequests(confirmedRequests.getOrDefault(eventFullDto.getId(), 0L));
        return eventFullDto;
    }

    private Map<Long, Long> getViews(List<Event> events) {
        Map<Long, Long> views = new HashMap<>();

        if (events.isEmpty()) {
            return views;
        }

        List<Event> publishedEvents = getPublishedEvents(events);

        Optional<LocalDateTime> minDate = publishedEvents.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);

        if (minDate.isPresent()) {
            LocalDateTime start = minDate.get();
            LocalDateTime end = LocalDateTime.now();
            List<String> uris = publishedEvents.stream()
                    .map(Event::getId)
                    .map(id -> ("/events/" + id))
                    .collect(Collectors.toList());

            List<ViewStatsDto> stats = statsClient.getStats(start, end, uris, true);
            stats.forEach(stat -> {
                Long eventId = Long.parseLong(stat.getUri()
                        .split("/", 0)[2]);
                views.put(eventId, views.getOrDefault(eventId, 0L) + stat.getHits());
            });
        }

        return views;
    }

    private Map<Long, Long> getConfirmedRequests(List<Event> events) {
        List<Event> publishedEvents = getPublishedEvents(events);

        return participationRequestRepository.findAllByEventInAndStatus(publishedEvents, RequestStatus.CONFIRMED)
                .stream()
                .collect(Collectors.groupingBy(participationRequest ->
                        participationRequest.getEvent().getId(), Collectors.counting()));
    }

    private List<Event> getPublishedEvents(List<Event> events) {
        return events.stream()
                .filter(event -> event.getPublishedOn() != null)
                .collect(Collectors.toList());
    }

    private void checkStartIsBeforeEnd(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationRequestException("Время началось позже чем время окончания");
        }
    }

    private void checkStates(List<String> states) {
        if (states != null) {
            for (String state : states) {
                try {
                    EventState.valueOf(state);
                } catch (IllegalArgumentException e) {
                    throw new ValidationRequestException("Неверный states!");
                }
            }
        }
    }
}
