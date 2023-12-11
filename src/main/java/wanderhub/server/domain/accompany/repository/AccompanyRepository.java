package wanderhub.server.domain.accompany.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.global.utils.Local;

import java.util.Optional;

@Repository
public interface AccompanyRepository extends JpaRepository<Accompany, Long> {


    @Modifying
    @Query("update Accompany acc set acc.recruitComplete =:recruitComplete where acc.accompanyId = :accompanyId")
    void setRecruitComplete(Long accompanyId, boolean recruitComplete);

    @Modifying
    @Query("delete from Accompany acc where acc.accompanyMaker = :memberNickname")
    void deleteAccompanyByAccompanyMaker(String memberNickname);

}
