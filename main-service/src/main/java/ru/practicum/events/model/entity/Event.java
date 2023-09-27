package ru.practicum.events.model.entity;

import lombok.*;
import ru.practicum.category.model.entity.Category;
import ru.practicum.enums.EventState;
import ru.practicum.location.model.entity.Location;
import ru.practicum.users.model.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = false)
    private Integer participantLimit;
    private LocalDateTime publishedOn;

    @Column(nullable = false)
    private Boolean requestModeration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(length = 120, nullable = false)
    private String title;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
}
