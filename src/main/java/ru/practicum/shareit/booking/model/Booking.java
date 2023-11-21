package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@NamedEntityGraph(
        name = "booking-entity-graph-with-item-and-booker",
        attributeNodes = {
                @NamedAttributeNode("booker"),
                @NamedAttributeNode(value = "item", subgraph = "owner-subgraph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "owner-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("owner")
                        }
                )
        }
)

@Entity
@Table(name = "bookings", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    private User booker;

    @Column(name = "status_current")
    // private Status statusCurrent;
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        if (getId() == null) {
            return false;
        } else if (!getId().equals(booking.getId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", status='" + status + '\'' +
                '}';
    }
}
