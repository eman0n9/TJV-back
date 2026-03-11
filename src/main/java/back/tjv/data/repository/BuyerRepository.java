package back.tjv.data.repository;

import back.tjv.data.entity.Buyer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BuyerRepository extends CrudRepository<Buyer, Long> {
    Optional<Buyer> findByEmail(String email);
    boolean existsByEmail(String email);
}
