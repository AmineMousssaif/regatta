package be.ucll.ip.minor.groep124.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StorageRepository extends PagingAndSortingRepository<Storage, Long>, JpaRepository<Storage, Long> {

    List<Storage> findAll();
}
