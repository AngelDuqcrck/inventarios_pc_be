package com.inventarios.pc.inventarios_pc_be.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.entities.StatusUpdateMessage;

@RestController
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyStatusUpdate(String type, Integer id, String newStatus){
        StatusUpdateMessage message = new StatusUpdateMessage();
        message.setId(id);
        message.setTimestamp(LocalDateTime.now());
        message.setType(type);
        message.setStatus(newStatus);

        messagingTemplate.convertAndSend("/topic/status-updates", message);
    }
}
