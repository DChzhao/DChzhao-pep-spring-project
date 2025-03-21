package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// interface for managing Account entities
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    // Finds account by username
    Optional<Account> findByUsername(String username);
}
