package com.teamolha.talantino.notification.mapper;

import com.teamolha.talantino.notification.model.entity.KudosNotification;
import com.teamolha.talantino.notification.model.response.KudosNotificationDTO;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface KudosNotificationMapper {

    default KudosNotificationDTO toKudosNotificationDTO(KudosNotification notification) {
        return KudosNotificationDTO.builder()
                .fromSponsor(notification.getFromSponsor().getName() + " " + notification.getFromSponsor().getSurname())
                .sponsorAvatar(notification.getFromSponsor().getAvatar())
                .amount(notification.getAmount())
                .proofId(notification.getProof().getId())
                .proofTitle(notification.getProof().getTitle())
                .toTalent(notification.getToTalent().getEmail())
                .receivingDate(notification.getReceivingDate())
                .build();
    }
}
