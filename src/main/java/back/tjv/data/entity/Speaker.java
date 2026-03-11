package back.tjv.data.entity;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "speaker")
public class Speaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String photo;

    @Column
    private String role;


    @ManyToMany(mappedBy = "speakers", fetch = FetchType.LAZY)
    private Set<Event> events = new HashSet<>();

    public Set<Event> getEvents() { return events; }

    public void addEvent(Event e) {
        events.add(e);
        e.getSpeakers().add(this);
    }

    public void removeEvent(Event e) {
        events.remove(e);
        e.getSpeakers().remove(this);
    }

    public Speaker() {
    }
    public Speaker(String name) {
        this.name = name;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Speaker other = (Speaker) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
