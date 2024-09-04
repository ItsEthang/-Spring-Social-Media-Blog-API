package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Our API should be able to process the creation of new messages.
    public Message messageCreate(Message message) {
        return messageRepository.save(message);
    }

    // Our API should be able to retrieve all messages.
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Our API should be able to retrieve a message by its ID.
    public Message getMessageById(Integer id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        }
        return null;

    }

    // Our API should be able to delete a message identified by a message ID.
    public void deleteMessage(Integer id) {
        messageRepository.deleteById(id);
    }

    // Our API should be able to update a message text identified by a message ID.
    public void updateMessage(Integer id, Message replacement) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(replacement.getMessageText());
            messageRepository.save(message);
        }

    }

    // Our API should be able to retrieve all messages written by a particular user.
    // Todo: make custom query
    public List<Message> getMessagesByUser(Integer postedBy) {
        return messageRepository.findMessagesByPostedBy(postedBy);
    }

}
