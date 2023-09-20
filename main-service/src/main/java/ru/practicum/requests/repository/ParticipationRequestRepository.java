package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.enums.RequestStatus;
import ru.practicum.events.model.entity.Event;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByEventInAndStatus(List<Event> events, RequestStatus status);

    Long countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    ParticipationRequest findByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findByEventIn(List<Event> events);
}
