package net.cabrasky.yambo.repositories;

import net.cabrasky.yambo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByIsEnabledFalse();

    List<User> findByIsEnabledTrue();

    List<User> findByRoles_id(String role);

    Optional<User> findByUsernameOrEmail(String usernameOrEmail, String usernameOrEmail2);
}
