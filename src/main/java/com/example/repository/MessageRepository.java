package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

//interface for managing Message entities
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // Finds all messages posted by a specific user
    List<Message> findByPostedBy(Integer postedBy);
}