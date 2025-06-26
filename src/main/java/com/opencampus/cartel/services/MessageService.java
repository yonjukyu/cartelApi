package com.opencampus.cartel.services;


import com.opencampus.cartel.dto.request.MessageCreateRequest;
import com.opencampus.cartel.dto.response.MessageResponse;
import com.opencampus.cartel.model.entity.Message;
import com.opencampus.cartel.model.entity.User;
import com.opencampus.cartel.repository.MessageRepository;
import com.opencampus.cartel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    public MessageResponse sendMessage(MessageCreateRequest request) {
        User sender = authService.getCurrentUser();
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSubject(request.getSubject());
        message.setContent(request.getContent());
        message.setIsEncrypted(request.getIsEncrypted());
        message.setIsRead(false);
        message.setPriorityLevel(request.getPriorityLevel());
        message.setMessageType(request.getMessageType());

        return convertToResponse(messageRepository.save(message));
    }

    public MessageResponse getMessageById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        return convertToResponse(message);
    }

    public Page<MessageResponse> getReceivedMessages(Pageable pageable) {
        User currentUser = authService.getCurrentUser();
        return messageRepository.findByReceiver(currentUser, pageable).map(this::convertToResponse);
    }

    public Page<MessageResponse> getSentMessages(Pageable pageable) {
        User currentUser = authService.getCurrentUser();
        return messageRepository.findBySender(currentUser, pageable).map(this::convertToResponse);
    }

    public List<MessageResponse> getUnreadMessages() {
        User currentUser = authService.getCurrentUser();
        return messageRepository.findUnreadMessagesByUser(currentUser)
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public MessageResponse markAsRead(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        User currentUser = authService.getCurrentUser();
        if (!message.getReceiver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to mark this message as read");
        }

        message.setIsRead(true);
        message.setReadAt(LocalDateTime.now());
        return convertToResponse(messageRepository.save(message));
    }

    private MessageResponse convertToResponse(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setSender(userService.convertToResponse(message.getSender()));
        response.setReceiver(userService.convertToResponse(message.getReceiver()));
        response.setSubject(message.getSubject());
        response.setContent(message.getContent());
        response.setIsEncrypted(message.getIsEncrypted());
        response.setIsRead(message.getIsRead());
        response.setPriorityLevel(message.getPriorityLevel());
        response.setMessageType(message.getMessageType());
        response.setSentAt(message.getSentAt());
        response.setReadAt(message.getReadAt());
        return response;
    }
}