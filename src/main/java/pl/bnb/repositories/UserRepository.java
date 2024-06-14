package pl.bnb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bnb.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

}
