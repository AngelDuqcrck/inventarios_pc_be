package com.inventarios.pc.inventarios_pc_be.controllers;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.entities.StatusUpdateMessage;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SolicitudDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TicketDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudesResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketsResponse;

@RestController
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyStatusUpdate(String type, Integer id, String newStatus, Date date){
        StatusUpdateMessage message = new StatusUpdateMessage();
        message.setId(id);
        message.setTimestamp(LocalDateTime.now());
        message.setType(type);
        message.setDate(date);
        message.setStatus(newStatus);

        messagingTemplate.convertAndSend("/topic/status-updates", message);
    }

    public void notifyNewDate(String type, Integer id, Date newDate){
        StatusUpdateMessage message = new StatusUpdateMessage();
        message.setId(id);
        message.setTimestamp(LocalDateTime.now());
        message.setType(type);
        message.setDate(newDate);

        messagingTemplate.convertAndSend("/topic/status-updates", message);
    }

    public void notifyNewRequest(String type, Integer id, SolicitudesResponse newRequest){
        StatusUpdateMessage message = new StatusUpdateMessage();
        message.setId(id);
        message.setTimestamp(LocalDateTime.now());
        message.setType(type);
        message.setSolicitudes(newRequest);

        messagingTemplate.convertAndSend("/topic/status-updates", message);
    }

    public void notifyNewTicket(String type, Integer id, TicketsResponse newTicket){
        StatusUpdateMessage message = new StatusUpdateMessage();
        message.setId(id);
        message.setTimestamp(LocalDateTime.now());
        message.setType(type);
        message.setTickets(newTicket);

        messagingTemplate.convertAndSend("/topic/status-updates", message);
    }
}
