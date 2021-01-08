package com.example.monzun_admin.repository;

import com.example.monzun_admin.entities.Role;
import com.example.monzun_admin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Optional<User> findByIdAndRole(Long id, Role role);

    List<User> findByRole(Role role);
}
