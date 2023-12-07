package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.IncorrectPasswordException;
import com.example.exception.UserDoesNotExistException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accRepo;
    
    @Autowired
    public AccountService(AccountRepository accRepo) {
        this.accRepo = accRepo;
    }

    public Account register(Account acc) {
        if (acc.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        } else if (acc.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        } else if (accRepo.exists(Example.of(acc))) {
            throw new DuplicateKeyException("User already exists");
        }
        return accRepo.save(acc);
    }

    public Account login(Account acc) throws UserDoesNotExistException, IncorrectPasswordException {
        Account dbAcc = accRepo.findByUsername(acc.getUsername());
        if (dbAcc == null) {
            throw new UserDoesNotExistException("User does not exist");
        } else if (dbAcc != null && !dbAcc.getPassword().equals(acc.getPassword())) {
            throw new IncorrectPasswordException("Password is incorrect");
        }
        return dbAcc;
    }

    public boolean exists(Integer id) {
        return accRepo.findById(id).isPresent();
    }
}
