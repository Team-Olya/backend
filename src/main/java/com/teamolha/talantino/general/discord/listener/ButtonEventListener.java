package com.teamolha.talantino.general.discord.listener;

import com.teamolha.talantino.admin.service.AdminService;
import com.teamolha.talantino.proof.service.ProofService;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageEditSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Service
public class ButtonEventListener implements EventListener<ButtonInteractionEvent> {

    private final ProofService proofService;

    @Autowired
    public ButtonEventListener(@Lazy ProofService proofService) {
        this.proofService = proofService;
    }

    @Override
    public Class<ButtonInteractionEvent> getEventType() {
        return ButtonInteractionEvent.class;
    }

    @Override
    public Mono<Void> execute(ButtonInteractionEvent event) {
        Long reportId = Long.valueOf(event.getMessage().get().getEmbeds().get(0).getFields().get(0).getValue());
        log.error(String.valueOf(reportId));

        if ("approve".equals(event.getCustomId())) {
            Mono<Message> edit = event.editReply()
                    .withContentOrNull("Proof was deleted because it contained violations")
                    .flatMap(message -> message.edit(MessageEditSpec.builder()
                            .addAllComponents(Collections.emptyList()).build()));
            proofService.approveReport(reportId);
            return event.deferEdit().then(edit).then();

        } else if ("reject".equals(event.getCustomId())) {
            Mono<Message> edit = event.editReply()
                    .withContentOrNull("No violation found, complaint dismissed")
                    .flatMap(message -> message.edit(MessageEditSpec.builder()
                            .addAllComponents(Collections.emptyList()).build()));
            proofService.rejectReport(reportId);
            return event.deferEdit().then(edit).then();

        }
        return Mono.empty();
    }
}
