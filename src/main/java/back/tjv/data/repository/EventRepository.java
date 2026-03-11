package back.tjv.data.repository;

import back.tjv.data.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findBySpeakers_Id(Long speakerId);

    List<Event> findByTitleContainingIgnoreCase(String titlePart);

    List<Event> findByVenueContainingIgnoreCase(String venuePart);

    @Query("""
           select distinct e
           from Event e
             join e.tickets t
           where t.buyer.id = :buyerId
           """)
    List<Event> findDistinctByBuyerId(@Param("buyerId") Long buyerId);
}
