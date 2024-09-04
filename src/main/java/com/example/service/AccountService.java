package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // 1: Our API should be able to process new User registrations.
    public Account accountRegister(Account account) {
        return accountRepository.save(account);
    }

    // 2: Our API should be able to process User logins.
    // Todo: make a custom query
    public Account accountLogin(String username, String password) {
        Optional<Account> validAccount = accountRepository.findAccountByUsernameAndPassword(username, password);
        return validAccount.orElseThrow(() -> new UnauthorizedException("The username or the password is incorrect"));
    }

    public Account findAccountById(Integer id) {
        Optional<Account> validAccount = accountRepository.findById(id);
        return validAccount
                .orElseThrow(() -> new ResourceNotFoundException("The account of id " + id + " is not found"));
    }
}
