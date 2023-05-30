package com.teamolha.talantino.notification.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record NotificationList(
        long totalAmount,
        long unreadAmount,
        List<KudosNotificationDTO> notifications
) {
}
