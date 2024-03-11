package crudapplication.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import crudapplication.crud.model.User;

/**
 * UserRepository interface to utilize the jpaRepository interface methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	/**
	 * Find User by username.
	 *
	 * @param username to retrieve User from the database
	 * @return the optional of User
	 */
	Optional<User> findByUsername(String username);
}
