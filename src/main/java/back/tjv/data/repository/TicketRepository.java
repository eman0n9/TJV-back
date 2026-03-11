package back.tjv.data.repository;

import back.tjv.data.entity.Speaker;
import back.tjv.data.entity.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    List<Ticket> findByBuyer_Id(Long buyerId);
    List<Ticket> findByEvent_Id(Long eventId);

    boolean existsByEvent_IdAndRowLabelAndSeatNumber(Long eventId, String rowLabel, Integer seatNumber);

    @Query("""
           select t
           from Ticket t
           where t.event.id = :eventId and t.rowLabel is not null and t.seatNumber is not null
           """)
    List<Ticket> findSeatedByEvent(@Param("eventId") Long eventId);

    @Query("""
           select s
           from Ticket t
             join t.event e
             join e.speakers s
           where t.id = :ticketId
           """)
    List<Speaker> findSpeakersByTicketId(@Param("ticketId") Long ticketId);

    boolean existsByEvent_IdAndRowLabelAndSeatNumberAndIdNot(
            Long eventId, String rowLabel, Integer seatNumber, Long idNot);


}
