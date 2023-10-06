package ru.practicum.main.service.friendship.model;

import lombok.*;
import ru.practicum.main.service.user.model.User;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@Table(name = "friends")
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    @Override
    public String toString() {
        return "friendship{" +
                "id=" + id + ", " +
                "user=" + user.toString() + ", " +
                "friend=" + friend.toString() + "}";
    }
}
