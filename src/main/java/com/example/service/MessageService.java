package com.example.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository msgRepo;

    @Autowired
    public MessageService(MessageRepository msgRepo) {
        this.msgRepo = msgRepo;
    }

    public Message addMessage(Message msg) {
        if (msg.getMessage_text().isBlank()) {
            throw new IllegalArgumentException("Message cannot be blank");
        } else if (msg.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message exceeds 255 characters");
        } 
        return msgRepo.save(msg);
    }

    public List<Message> getMessages() {
        return msgRepo.findAll();
    }

    public Message getMessageById(int id) {
        Optional<Message> msg = msgRepo.findById(id);
        if (msg.isPresent()) {
            return msg.get();
        } else {
            return null;
        }
    }

    public Integer deleteMessageById(int id) {
        Optional<Message> msg = msgRepo.findById(id);
        if (msg.isPresent()) {
            msgRepo.deleteById(id);
            return 1;
        } else {
            return null;
        }
    }

    public Integer updateMessageById(Message message, int id) {
        String msg = message.getMessage_text();
        if (msg.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be blank");
        } else if (msg.length() > 255) {
            throw new IllegalArgumentException("Message has too many characters");
        }
        Optional<Message> dbMsg = msgRepo.findById(id);
        if (dbMsg.isEmpty()) {
            throw new IllegalArgumentException("Message does not exist");
        }
        Message dbMessage = dbMsg.get();
        dbMessage.setMessage_text(msg);
        return 1;
    }
    
    public List<Message> getAllMessagesByAccountId(int id) {
        Iterable<Integer> idIterable = Arrays.asList(id);
        return msgRepo.findAllById(idIterable);
    }
}
