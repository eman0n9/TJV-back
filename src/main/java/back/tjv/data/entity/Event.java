package back.tjv.data.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "event",
        indexes = {
                @Index(name = "idx_event_start_at", columnList = "start_at"),
                @Index(name = "idx_event_venue", columnList = "venue")
        }
)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private OffsetDateTime endAt;


    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private java.util.List<Ticket> tickets = new java.util.ArrayList<>();


    public java.util.List<Ticket> getTickets() {
        return tickets;
    }

    public void addTicket(Ticket t) {
        tickets.add(t);
        t.setEvent(this);
    }

    public void removeTicket(Ticket t) {
        tickets.remove(t);
        if (t.getEvent() == this) t.setEvent(null);
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_speakers",
            joinColumns = @JoinColumn(
                    name = "event_id",
                    foreignKey = @ForeignKey(name = "fk_event_speakers_event")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "speaker_id",
                    foreignKey = @ForeignKey(name = "fk_event_speakers_speaker")
            ),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_event_speaker",
                    columnNames = {"event_id", "speaker_id"}
            )
    )
    private Set<Speaker> speakers = new HashSet<>();

    public Set<Speaker> getSpeakers() { return speakers; }

    public void addSpeaker(Speaker s) {
        speakers.add(s);
        s.getEvents().add(this);
    }

    public void removeSpeaker(Speaker s) {
        speakers.remove(s);
        s.getEvents().remove(this);
    }

    public Event() {
    }

    public Event(String title, OffsetDateTime startAt, OffsetDateTime endAt) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Event(String title, OffsetDateTime startAt, OffsetDateTime endAt, String description) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.description = description;
    }

    public Event(String title, OffsetDateTime startAt, OffsetDateTime endAt, String venue, String description, BigDecimal price) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.venue = venue;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(OffsetDateTime startAt) {
        this.startAt = startAt;
    }

    public OffsetDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(OffsetDateTime endAt) {
        this.endAt = endAt;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event other = (Event) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
