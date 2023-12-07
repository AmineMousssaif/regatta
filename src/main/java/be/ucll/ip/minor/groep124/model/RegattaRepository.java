package be.ucll.ip.minor.groep124.model;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RegattaRepository extends JpaRepository<Regatta, Long> {

    List<Regatta> findAll();
    //Voorbeeld handmatige query indien JPA niet gewenste methode heeft:
    // @Query("SELECT p FROM Patient p WHERE p.age>18")
}

