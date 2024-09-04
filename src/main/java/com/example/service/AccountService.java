package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // 1: Our API should be able to process new User registrations.
    public Account accountRegister(Account account) {
        return accountRepository.save(account);
    }

    // 2: Our API should be able to process User logins.
    // Todo: make a custom query
    public Account accountLogin(String username, String password) {
        Optional<Account> validAccount = accountRepository.findAccountByUsernameAndPassword(username, password);
        if (validAccount.isPresent()) {
            return validAccount.get();
        }
        return null;
    }
}
