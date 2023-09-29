package ru.practicum.events.model.dto;

import lombok.*;
import ru.practicum.enums.RequestStatus;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatus status;
}
