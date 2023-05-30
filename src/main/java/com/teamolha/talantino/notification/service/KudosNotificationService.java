package com.teamolha.talantino.notification.service;

import com.teamolha.talantino.notification.WebSocketSender;
import com.teamolha.talantino.notification.mapper.KudosNotificationMapper;
import com.teamolha.talantino.notification.model.entity.KudosNotification;
import com.teamolha.talantino.notification.model.response.KudosNotificationDTO;
import com.teamolha.talantino.notification.model.response.NotificationList;
import com.teamolha.talantino.notification.repository.KudosNotificationRepository;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.repository.TalentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class KudosNotificationService {

    @Value("${expire.deletion.days}")
    private Integer expireDays;

    private final TalentRepository talentRepository;

    private final KudosNotificationMapper notificationMapper;

    private final KudosNotificationRepository notificationRepository;

    private final WebSocketSender webSocketSender;

    public void saveNotification(Sponsor sponsor, Proof proof, int amount, Talent talent) {
        Date currentDate = new Date(Calendar.getInstance().getTime().getTime());

        KudosNotification notification = KudosNotification.builder()
                .fromSponsor(sponsor)
                .toTalent(talent)
                .proof(proof)
                .amount(amount)
                .receivingDate(currentDate)
                .expirationDate(calculateExpiredDate(currentDate))
                .build();
        addNotificationToTalent(talent, notification);

        KudosNotificationDTO notificationDTO = notificationMapper.toKudosNotificationDTO(notification);
        webSocketSender.sendMessageToUser(notificationDTO);
    }

    private Date calculateExpiredDate(Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentDate.getTime());
        cal.add(Calendar.DAY_OF_WEEK, expireDays);
        return new Date(cal.getTime().getTime());
    }

    private void addNotificationToTalent(Talent talent, KudosNotification notification) {
        var talentNotifications = talent.getNotifications();
        if (!talentNotifications.contains(notification)) {
            talentNotifications.add(notification);
            talentRepository.save(talent);
        }
    }

    public NotificationList getAllNotifications(Authentication auth, Integer size, Integer page) {
        if (auth != null) {
            String email = auth.getName();
            Pageable pageable = PageRequest.of(page, size);

            return NotificationList.builder()
                    .totalAmount(notificationRepository.countByToTalentEmailIgnoreCase(email))
                    .notifications(notificationRepository.findByToTalentEmailIgnoreCase(auth.getName(), pageable)
                    .stream().map(notificationMapper::toKudosNotificationDTO).collect(Collectors.toList()))
                    .build();
        }

        return null;
    }
}
