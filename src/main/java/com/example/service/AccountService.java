package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

//Service layer for handling account related operations
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    // injection of repository
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Creates a new user account
    public Account createAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be blank");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 4 characters long");
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        // Save the new account to the databas
        return accountRepository.save(account);
    }

    // Validates user login credentials
    public Optional<Account> validateLogin(String username, String password) {
        return accountRepository.findByUsername(username)
                .filter(account -> account.getPassword().equals(password));
    }
}
