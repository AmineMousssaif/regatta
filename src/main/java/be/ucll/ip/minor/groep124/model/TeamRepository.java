package be.ucll.ip.minor.groep124.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, PagingAndSortingRepository<Team, Long> {

    List<Team> findAll();
}
