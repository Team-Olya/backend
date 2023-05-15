package com.teamolha.talantino.general.discord.event;

import com.teamolha.talantino.proof.model.response.ReportedProofDTO;
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

    public void sendMessage(ReportedProofDTO reportedProof, String refererUrl) {
        Button approve = Button.success("approve", "Approve");
        Button reject = Button.danger("reject", "Reject");

        discordClient.getChannelById(channelId)
                .ofType(TextChannel.class)
                .flatMap(channel -> channel.createMessage(createSpec(reportedProof, refererUrl))
                        .withComponents(
                                ActionRow.of(approve, reject)
                        )).subscribe();
    }

    private EmbedCreateSpec createSpec(ReportedProofDTO reportedProof, String refererUrl) {
        return EmbedCreateSpec.builder()
                .color(Color.VIVID_VIOLET)
                .title(reportedProof.title())
                .url(createProofUrl(refererUrl, reportedProof.id()))
                .author(reportedProof.proofAuthor(), createTalentUrl(refererUrl, reportedProof.proofAuthorId()),
                        reportedProof.proofAuthorAvatar())
                .description(reportedProof.description())
                .addField("Report ID", String.valueOf(reportedProof.id()), true)
                .timestamp(Instant.now())
                .footer("Reported by " + reportedProof.reportedBy(), null)
                .build();
    }

    private String createProofUrl(String refererUrl, Long proofId) {
        return refererUrl + "/proofs/" + proofId;
    }

    private String createTalentUrl(String refererUrl, Long talentId) {
        return refererUrl + "/talent/" + talentId;
    }
}
