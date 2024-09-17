package br.nagualcode.userservice.repository;

import br.nagualcode.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // No additional code needed, JpaRepository provides the necessary methods
}
