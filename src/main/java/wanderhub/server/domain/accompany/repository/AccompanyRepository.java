package wanderhub.server.domain.accompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.entity.Accompany;

@Repository
public interface AccompanyRepository extends JpaRepository<Accompany, Long> {
}
