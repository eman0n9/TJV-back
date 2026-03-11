package back.tjv.business.spec;


import back.tjv.data.dto.event.EventSearchFilter;
import back.tjv.data.entity.Event;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public final class EventSpecs {
    private EventSpecs() {}

    public static Specification<Event> byFilter(EventSearchFilter eventSearchFilter) {
        return (root, q, cb) -> {
            List<Predicate> ps = new ArrayList<>();

            if (eventSearchFilter == null) return cb.conjunction();

            if (eventSearchFilter.q() != null && !eventSearchFilter.q().isBlank()) {
                String like = "%" + eventSearchFilter.q().trim().toLowerCase() + "%";
                ps.add(cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("venue")), like),
                        cb.like(cb.lower(root.get("description")), like)
                ));
            }
            if (eventSearchFilter.title() != null && !eventSearchFilter.title().isBlank()) {
                ps.add(cb.like(cb.lower(root.get("title")), "%" + eventSearchFilter.title().trim().toLowerCase() + "%"));
            }
            if (eventSearchFilter.venue() != null && !eventSearchFilter.venue().isBlank()) {
                ps.add(cb.like(cb.lower(root.get("venue")), "%" + eventSearchFilter.venue().trim().toLowerCase() + "%"));
            }
            if (eventSearchFilter.startFrom() != null) {
                ps.add(cb.greaterThanOrEqualTo(root.get("startAt"), eventSearchFilter.startFrom()));
            }

            if (eventSearchFilter.startTo() != null) {
                ps.add(cb.lessThanOrEqualTo(root.get("startAt"), eventSearchFilter.startTo()));
            }

            if (eventSearchFilter.minPrice() != null) {
                ps.add(cb.greaterThanOrEqualTo(root.get("price"), eventSearchFilter.minPrice()));
            }

            if (eventSearchFilter.maxPrice() != null) {
                ps.add(cb.lessThanOrEqualTo(root.get("price"), eventSearchFilter.maxPrice()));
            }


            return ps.isEmpty() ? cb.conjunction() : cb.and(ps.toArray(new Predicate[0]));
        };
    }
}
