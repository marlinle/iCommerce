package au.com.nab.users.repo;

import au.com.nab.users.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
