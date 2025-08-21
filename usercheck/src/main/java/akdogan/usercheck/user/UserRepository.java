package akdogan.usercheck.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findTopByOrderByIdAsc();
    
    @Query("SELECT u FROM User u")
    Stream<User> streamAll();

}
