package org.skillbox.repository.jpa;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.skillbox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(@NotBlank @Size(min = 3, max = 50) String username, Long id);

    boolean existsByEmailAndIdNot(@NotBlank @Email String email, Long id);
}