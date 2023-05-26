package com.teamolha.talantino.general.discord.listener;

import com.teamolha.talantino.proof.service.ProofService;
import com.teamolha.talantino.talent.service.TalentService;
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

    private final TalentService talentService;

    @Autowired
    public ButtonEventListener(@Lazy ProofService proofService, @Lazy TalentService talentService) {
        this.proofService = proofService;
        this.talentService = talentService;
    }

    @Override
    public Class<ButtonInteractionEvent> getEventType() {
        return ButtonInteractionEvent.class;
    }

    @Override
    public Mono<Void> execute(ButtonInteractionEvent event) {
        Long reportId = Long.valueOf(event.getMessage().get().getEmbeds().get(0).getFields().get(0).getValue());
        log.error(String.valueOf(reportId));

        if ("approve-rp".equals(event.getCustomId())) {
            return approveReportProof(event, reportId);
        }
        if ("reject-rp".equals(event.getCustomId())) {
            return rejectReportProof(event, reportId);
        }

        if("approve-rt".equals(event.getCustomId())) {
            return approveReportTalent(event, reportId);
        }
        if ("reject-rt".equals(event.getCustomId())) {
            return rejectReportTalent(event, reportId);
        }
        return Mono.empty();
    }

    private Mono<Void> approveReportProof(ButtonInteractionEvent event, Long reportId) {
        Mono<Message> edit = event.editReply()
                .withContentOrNull("Proof was deleted because it contained violations")
                .flatMap(message -> message.edit(MessageEditSpec.builder()
                        .addAllComponents(Collections.emptyList()).build()));
        proofService.approveReport(reportId);
        return event.deferEdit().then(edit).then();
    }

    private Mono<Void> rejectReportProof(ButtonInteractionEvent event, Long reportId) {
        Mono<Message> edit = event.editReply()
                .withContentOrNull("No violation found, complaint dismissed")
                .flatMap(message -> message.edit(MessageEditSpec.builder()
                        .addAllComponents(Collections.emptyList()).build()));
        proofService.rejectReport(reportId);
        return event.deferEdit().then(edit).then();
    }

    private Mono<Void> approveReportTalent(ButtonInteractionEvent event, Long reportId) {
        Mono<Message> edit = event.editReply()
                .withContentOrNull("Talent was blocked for violations")
                .flatMap(message -> message.edit(MessageEditSpec.builder()
                        .addAllComponents(Collections.emptyList()).build()));
        talentService.approveReport(reportId);
        return event.deferEdit().then(edit).then();
    }

    private Mono<Void> rejectReportTalent(ButtonInteractionEvent event, Long reportId) {
        Mono<Message> edit = event.editReply()
                .withContentOrNull("No violation found, complaint dismissed")
                .flatMap(message -> message.edit(MessageEditSpec.builder()
                        .addAllComponents(Collections.emptyList()).build()));
        talentService.rejectReport(reportId);
        return event.deferEdit().then(edit).then();
    }
}
