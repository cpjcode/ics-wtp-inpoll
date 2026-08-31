package oth.ics.wtp.inpollbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import oth.ics.wtp.inpollbackend.entities.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}
