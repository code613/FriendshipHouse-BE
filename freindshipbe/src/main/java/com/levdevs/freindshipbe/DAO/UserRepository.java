package com.levdevs.freindshipbe.DAO;

import com.levdevs.freindshipbe.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom queries here if needed
    boolean existsByEmail(String email); // Custom method to check if email exists
    User findByEmail(String email); // Custom method to find user by email
}
