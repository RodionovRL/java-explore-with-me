package ru.practicum.main.service.request.model;

import lombok.*;
import ru.practicum.main.service.utils.RequestState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "requests")
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {
    @Column(name = "created")
    private LocalDateTime created;
    private long eventId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "requester_id")
    private long requesterId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestState status;
}
