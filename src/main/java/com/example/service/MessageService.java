package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

//Service layer for handling message related operations
@Service
public class MessageService {

    // Inject Repos
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    // Constructor injection
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    // Creates a new message
    public Message createMessage(Message message) {

        // Check if the user exists before creating a message
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }

        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text cannot be blank");
        }
        if (message.getMessageText().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text must be under 255 characters");
        }

        // Saves message in database
        return messageRepository.save(message);
    }

    // Retrieves all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Retrieves by ID
    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    // Deletes a message by its ID
    public Optional<Integer> deleteMessage(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);

            // Return 1 when deletion was successful
            return Optional.of(1);
        }
        // Return empty optional when no message was found
        return Optional.empty();
    }

    // Updates message for a given ID
    public int updateMessage(Integer id, String newText) {
        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message text");
        }
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.setMessageText(newText);
            messageRepository.save(message);
            return 1;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message not found");
    }

    // Retrieves all messages for a specific user by ID
    public List<Message> getMessagesByUser(Integer userId) {
        return messageRepository.findByPostedBy(userId);
    }
}
