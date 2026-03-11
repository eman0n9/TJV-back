package back.tjv.data.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "buyer",
        uniqueConstraints = @UniqueConstraint(name = "uk_buyer_email", columnNames = "email")
)
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private java.util.List<Ticket> tickets = new java.util.ArrayList<>();

    public java.util.List<Ticket> getTickets() {
        return tickets;
    }


    public Buyer() {
    }

    public Buyer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @PrePersist
    private void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public void addTicket(Ticket t) {
        tickets.add(t);
        t.setBuyer(this);
    }

    public void removeTicket(Ticket t) {
        tickets.remove(t);
        if (t.getBuyer() == this) t.setBuyer(null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Buyer other = (Buyer) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
