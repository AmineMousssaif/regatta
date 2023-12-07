package be.ucll.ip.minor.groep124.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long>{

    List<Boat> findAll();
}

