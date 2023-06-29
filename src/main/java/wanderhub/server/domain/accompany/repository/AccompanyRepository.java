package wanderhub.server.domain.accompany.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.entity.Accompany;

import java.time.LocalDate;

@Repository
public interface AccompanyRepository extends JpaRepository<Accompany, Long> {

    Page<Accompany> findAll(Pageable pageable);
    Page<Accompany> findByAccompanyLocal(String local, Pageable pageable);
    Page<Accompany> findByAccompanyDate(LocalDate date, Pageable pageable);
    Page<Accompany> findByAccompanyLocalAndAccompanyDate(String local, LocalDate date, Pageable pageable);

}