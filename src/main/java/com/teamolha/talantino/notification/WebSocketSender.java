package com.teamolha.talantino.notification;

import com.teamolha.talantino.notification.model.response.KudosNotificationDTO;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WebSocketSender {

    private SimpMessagingTemplate messagingTemplate;

    public void sendMessageToUser(KudosNotificationDTO notificationDTO) {
        messagingTemplate.convertAndSendToUser(notificationDTO.getToTalent(),
                "/queue/kudos", notificationDTO);
    }
}
