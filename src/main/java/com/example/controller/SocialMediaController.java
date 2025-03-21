package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling social media operations.
 */
@RestController
@RequestMapping("")
public class SocialMediaController {
    
    private final AccountService accountService;
    private final MessageService messageService;

    //  Constructor dependency injection for services
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    //  User Registration
    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account) {
        try {
            Account createdAccount = accountService.createAccount(account);
            // 200 OK response with account data
            return ResponseEntity.ok(createdAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    //  User Login
    @PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account account) {
        Optional<Account> loggedInAccount = accountService.validateLogin(account.getUsername(), account.getPassword());

        return loggedInAccount
                // 200 OK if login is successful
                .map(ResponseEntity::ok)
                // 401 Unauthorized if login fails
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
    }

    //  Create a new message
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.createMessage(message);
            // 200 OK
            return ResponseEntity.ok(createdMessage);
        } catch (IllegalArgumentException e) {
            // 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //  Retrieve all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    // Retrieve message by ID
    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer id) {
        return messageService.getMessageById(id)
                .map(ResponseEntity::ok) // 200 OK
                .orElse(ResponseEntity.ok(null)); //  if not found
    }

    //  Delete a message
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Integer id) {
        Optional<Integer> deletedRows = messageService.deleteMessage(id);

        if (deletedRows.isPresent()) {
            //  1 when a message WAS deleted
            return ResponseEntity.ok("1");
        }
        //  empty string when message was NOT found
        return ResponseEntity.ok("");
    }

    // Update message text
    @PatchMapping("/messages/{id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer id, @RequestBody Message message) {
        int updatedRows = messageService.updateMessage(id, message.getMessageText());
        if (updatedRows > 0) {
            return ResponseEntity.ok(updatedRows);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    // Retrieve all messages by a specific user
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        return ResponseEntity.ok(messageService.getMessagesByUser(accountId));
    }
}
