//package com.haiemdavang.AnrealShop.controller;
//
//import com.haiemdavang.AnrealShop.dto.chat.ChatMessageRequest;
//import com.haiemdavang.AnrealShop.tech.kafka.producer.NoticeKafkaProducer;
//import com.haiemdavang.AnrealShop.utils.NoticeTemplate;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//import java.security.Principal;
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//public class ChatController {
//    private final NoticeKafkaProducer noticeKafkaProducer;
//
//    @MessageMapping("/chat.send")
//    @SendTo("/topic/public")
//    public void sendMessage(@Payload ChatMessageRequest chatMessage, Principal principal) {
//        log.warn(chatMessage.toString());
//        noticeKafkaProducer.sendNoticeSyncMessage(NoticeTemplate.toUser(principal.getName(), "hello"));
//    }
//
//}
