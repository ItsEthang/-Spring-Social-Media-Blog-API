package com.example.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@RestController
public class SocialMediaController {
    private MessageService messageService;
    private AccountService accountService;

    public SocialMediaController(MessageService messageService, AccountService accountService) {
        this.messageService = messageService;
        this.accountService = accountService;
    }

    @PostMapping("register")
    public ResponseEntity<?> accountRegister(@RequestBody Account account) {
        List<Account> allAccounts = accountService.getAllAccounts();
        for (Account a : allAccounts) {
            if (a.getUsername().equals(account.getUsername())) {
                return ResponseEntity.status(409).build();
            }
        }
        if (!account.getUsername().isEmpty() && account.getPassword().length() > 3) {
            return ResponseEntity.ok(accountService.accountRegister(account));
        }
        return ResponseEntity.status(400).build();
    }

    @PostMapping("login")
    public ResponseEntity<?> accountLogin(@RequestBody Account account) {
        Account validLogin = accountService.accountLogin(account.getUsername(), account.getPassword());
        if (validLogin == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(validLogin);
    }

    @PostMapping("messages")
    public ResponseEntity<?> createMessages(@RequestBody Message message) {
        int messageLength = message.getMessageText().length();

        // Check if account exists
        Account existingAccount = accountService.findAccountById(message.getPostedBy());
        if (existingAccount == null || messageLength < 1 || messageLength > 255) {
            return ResponseEntity.status(400).build();
        }

        return ResponseEntity.ok(messageService.messageCreate(message));
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return ResponseEntity.ok(messageService.getMessageById(messageId));
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        int affectedRows = messageService.deleteMessage(messageId);

        if (affectedRows == 0) {
            return ResponseEntity.status(200).build();
        }

        return ResponseEntity.ok(affectedRows);
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable Integer messageId, @RequestBody Message newMessage) {
        Message messageToUpdate = messageService.getMessageById(messageId);
        String text = newMessage.getMessageText();
        if (messageToUpdate == null || text.isEmpty()
                || text.length() > 255) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(messageService.updateMessage(messageId, newMessage));

    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccount(@PathVariable Integer accountId) {
        return ResponseEntity.ok(messageService.getMessagesByUser(accountId));
    }
}
