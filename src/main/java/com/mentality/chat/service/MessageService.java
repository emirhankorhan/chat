package com.mentality.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mentality.chat.entity.Message;
import com.mentality.chat.repo.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository repository;

    public void saveChatResponse(String response, String author) {
        Message chatResponse = new Message();
        chatResponse.setMessage(response);
        chatResponse.setAuthor(author);
        repository.save(chatResponse);
    }

    public void saveMessage(Message message) {
        repository.save(message);
    }

    public List<Message> getAllMessages() {
        return repository.findAll();
        
    }    
}

