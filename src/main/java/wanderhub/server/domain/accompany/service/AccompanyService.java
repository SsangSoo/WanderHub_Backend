package wanderhub.server.domain.accompany.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wanderhub.server.domain.accompany.entity.Accompany;

import java.util.Optional;

public interface AccompanyService {

    Accompany createAccompany(Accompany accompany, String userEmail);
    Page<Accompany> findAll(Pageable pageable);
    Optional<Accompany> findById(Long id);
    Page<Accompany> findByLocal(String accompanyLocal, Pageable pageable);
    Page<Accompany> findByDate(String accompanyDate, Pageable pageable);
    Page<Accompany> findByLocalAndDate(String accompanyLocal, String accompanyDate, Pageable pageable);

    void deleteAccompany(Long id, String userEmail);
}
