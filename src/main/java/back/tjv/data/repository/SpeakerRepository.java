package back.tjv.data.repository;

import back.tjv.data.entity.Speaker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpeakerRepository extends CrudRepository<Speaker, Long> {
    List<Speaker> findByNameContainingIgnoreCase(String namePart);
    List<Speaker> findByRoleContainingIgnoreCase(String rolePart);
    List<Speaker> findAllByIdIn(Collection<Long> ids);
}
