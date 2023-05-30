package com.teamolha.talantino.notification.controller;

import com.teamolha.talantino.notification.model.response.NotificationList;
import com.teamolha.talantino.notification.service.KudosNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NotificationController {

    private final KudosNotificationService notificationService;

    @GetMapping("/notifications")
    public NotificationList getAllNotifications(Authentication auth,
                                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                                @RequestParam(required = false, defaultValue = "0") Integer page) {
        return notificationService.getAllNotifications(auth, size, page);
    }
}
