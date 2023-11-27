package com.mentality.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mentality.chat.entity.Message;
import com.mentality.chat.service.MessageService;


import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/bot")
@CrossOrigin
public class CustomBotController {

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    @Autowired
    private MessageService messageService;

    interface Assistant {
        String chat(String message);
    }

    private Assistant assistant;

    @PostConstruct
    public void init() {
        ChatMemory chatMemory = TokenWindowChatMemory
                .withMaxTokens(2000, new OpenAiTokenizer("gpt-3.5-turbo"));

        assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(OPENAI_API_KEY))
                .chatMemory(chatMemory)
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        String response = assistant.chat(prompt);

    messageService.saveChatResponse(response, "assistant");
        return response;
    }

    // @GetMapping("/chat")
    // public String chat(@RequestParam("prompt") String prompt) {

    // String response = getResponseFromChatGPT(prompt);

    // messageService.saveChatResponse(response, "assistant");

    // return response;
    // }

    @GetMapping
    public ResponseEntity<?> getAllMessages() {
        var messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        messageService.saveMessage(message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    
}