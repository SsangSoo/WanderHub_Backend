package wanderhub.server.auth.jwt.refreshtoken.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wanderhub.server.auth.jwt.refreshtoken.entity.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshAccessTokenRepository extends CrudRepository<RefreshToken,String> {

    // @Indexed 사용한 필드만 가능
    Optional<RefreshToken> findByUsername(String username);
}
