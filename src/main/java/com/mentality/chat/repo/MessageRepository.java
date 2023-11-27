package com.mentality.chat.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mentality.chat.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}

