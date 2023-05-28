package com.teamolha.talantino.general.notification;

import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WebSocketSender {

    private SimpMessagingTemplate messagingTemplate;

    public void sendMessageToUser(Long proofId, int amount, Sponsor sponsor, Proof proof) {
        KudosNotification notification = KudosNotification.builder()
                .fromSponsor(sponsor.getName() + " " + sponsor.getSurname())
                .sponsorAvatar(sponsor.getAvatar())
                .amount(amount)
                .proofId(proofId)
                .proofTitle(proof.getTitle())
                .toTalent(proof.getTalent().getEmail())
                .build();
        messagingTemplate.convertAndSendToUser(notification.getToTalent(), "/queue/kudos", notification);
    }
}
