package com.adamlentz.pantryuser.repositories;

import com.adamlentz.pantryuser.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(Long id);

    Boolean existsByEmail(String email);
}
