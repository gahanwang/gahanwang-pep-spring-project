package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.IncorrectPasswordException;
import com.example.exception.UserDoesNotExistException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accService;
    @Autowired
    private MessageService msgService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/register")
    public Account register(@RequestBody Account acc) {
        return accService.register(acc);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login")
    public Account login(@RequestBody Account acc) throws UserDoesNotExistException, IncorrectPasswordException  {
        return accService.login(acc);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/messages")
    public Message createMessage(@RequestBody Message msg) {
        if (!accService.exists(msg.getPosted_by())) {
            throw new IllegalArgumentException("User does not exist");
        }
        return msgService.addMessage(msg);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/messages")
    public List<Message> getAllMessages() {
        return msgService.getMessages();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/messages/{message_id}")
    public Message getMessageById(@PathVariable(value = "message_id") int message_id) {
        return msgService.getMessageById(message_id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/messages/{message_id}")
    public Integer deleteMessageById(@PathVariable(value= "message_id") int message_id) {
        return msgService.deleteMessageById(message_id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/messages/{message_id}")
    public Integer updateMessageById(@RequestBody Message message, @PathVariable(value= "message_id") int message_id) {
        return msgService.updateMessageById(message, message_id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/accounts/{account_id}/messages")
    public List<Message> getAllMessagesByAccountId(@PathVariable(value = "account_id") int account_id) {
        return msgService.getAllMessagesByAccountId(account_id);
        
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArguments(IllegalArgumentException e) {
        return e.getMessage();
    }
    
    @ExceptionHandler({DuplicateKeyException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserAlreadyExists(DuplicateKeyException e) {
        return e.getMessage();
    }

    @ExceptionHandler({IncorrectPasswordException.class, UserDoesNotExistException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedUser(Exception e) {
        return e.getMessage();
    }
}
