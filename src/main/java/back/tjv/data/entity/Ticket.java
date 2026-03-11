package back.tjv.data.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ticket",
        indexes = {
                @Index(name = "idx_ticket_buyer", columnList = "buyer_id"),
                @Index(name = "idx_ticket_event", columnList = "event_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_ticket_event_seat",
                        columnNames = {"event_id","row_label","seat_number"})
        })
public class Ticket {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ticket_event"))
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buyer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ticket_buyer"))
    private Buyer buyer;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "row_label", nullable = false, length = 1)
    private String rowLabel;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "purchased_at", nullable = false, updatable = false)
    private OffsetDateTime purchasedAt;

    public Ticket() {
    }

    public Ticket(Event event, Buyer buyer, BigDecimal price) {
        this.event = event;
        this.buyer = buyer;
        this.price = price;
    }

    @PrePersist
    private void prePersist() {
        if (purchasedAt == null) {
            purchasedAt = OffsetDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OffsetDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(OffsetDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public String getRowLabel() {
        return rowLabel;
    }

    public void setRowLabel(String rowLabel) {
        this.rowLabel = rowLabel;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket other = (Ticket) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
