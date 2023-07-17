package wanderhub.server.domain.accompany.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.global.utils.Local;

import java.util.Optional;

@Repository
public interface AccompanyRepository extends JpaRepository<Accompany, Long> {

    Page<Accompany> findAll(Pageable pageable);

    Optional<Accompany> findById(Long accompanyId);

}
