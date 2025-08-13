package routine.log.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import routine.log.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query("select u.id from User u where u.username = :username")
    Long findIdByUsername(@Param("username") String username);



}
