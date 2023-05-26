package com.teamolha.talantino.general.discord.event;

import com.teamolha.talantino.proof.model.response.ReportedProofDTO;
import com.teamolha.talantino.talent.model.response.ReportedTalentDTO;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@AllArgsConstructor
public class MessageSendEvent {

    private final GatewayDiscordClient discordClient;

    private final Snowflake channelId = Snowflake.of(System.getenv("DISCORD_CHANNEL"));

    public void sendReportProofMessage(ReportedProofDTO reportedProof, String refererUrl) {
        Button approve = Button.success("approve-rp", "Delete proof");
        Button reject = Button.danger("reject-rp", "Reject report");

        discordClient.getChannelById(channelId)
                .ofType(TextChannel.class)
                .flatMap(channel -> channel.createMessage(createSpecForReportedProof(reportedProof, refererUrl))
                        .withComponents(
                                ActionRow.of(approve, reject)
                        )).subscribe();
    }

    public void sendReportTalentMessage(ReportedTalentDTO reportedTalent, String refererUrl) {
        Button approve = Button.success("approve-rt", "Block talent");
        Button reject = Button.danger("reject-rt", "Reject report");

        discordClient.getChannelById(channelId)
                .ofType(TextChannel.class)
                .flatMap(channel -> channel.createMessage(createSpecForReportedTalent(reportedTalent, refererUrl))
                        .withComponents(
                                ActionRow.of(approve, reject)
                        )).subscribe();
    }

    private EmbedCreateSpec createSpecForReportedProof(ReportedProofDTO reportedProof, String refererUrl) {
        return EmbedCreateSpec.builder()
                .color(Color.VIVID_VIOLET)
                .title(reportedProof.title())
                .url(createTalentUrl(refererUrl, reportedProof.proofAuthorId()))
                .author(reportedProof.proofAuthor(), createTalentUrl(refererUrl, reportedProof.proofAuthorId()),
                        reportedProof.proofAuthorAvatar())
                .description(reportedProof.description())
                .addField("Report ID", String.valueOf(reportedProof.id()), true)
                .timestamp(Instant.now())
                .footer("Reported by " + reportedProof.reportedBy(), null)
                .build();
    }

    private EmbedCreateSpec createSpecForReportedTalent(ReportedTalentDTO reportedTalent, String refererUrl) {
        return EmbedCreateSpec.builder()
                .color(Color.CINNABAR)
                .title(reportedTalent.fullName())
                .url(createTalentUrl(refererUrl, reportedTalent.reportedTalentId()))
                .author(reportedTalent.kind(), createTalentUrl(refererUrl, reportedTalent.reportedTalentId()),
                        reportedTalent.avatar())
                .description(reportedTalent.description())
                .addField("Email ", reportedTalent.email(), true)
                .addField("Location ", reportedTalent.location(), true)
                .addField("Report ID", String.valueOf(reportedTalent.id()), true)
                .timestamp(Instant.now())
                .footer("Reported by " + reportedTalent.reportedBy(), null)
                .build();
    }

    private String createTalentUrl(String refererUrl, Long talentId) {
        return refererUrl + "talent/" + talentId;
    }
}
