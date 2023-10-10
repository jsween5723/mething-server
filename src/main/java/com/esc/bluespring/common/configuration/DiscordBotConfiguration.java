package com.esc.bluespring.common.configuration;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.object.entity.channel.Channel;
import discord4j.rest.entity.RestChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordBotConfiguration {

  @Value("${discord-bot.token}")
  private String token;

  @Value("${discord-bot.channel-id}")
  private String channelId;

  @Bean
  public Channel channel(Snowflake snowflake) {
    return DiscordClient.create(token).login().flatMap(client -> client.getChannelById(snowflake))
        .block();
  }

  @Bean
  public RestChannel restChannel(Channel channel) {
    return channel.getRestChannel();
  }

  @Bean
  public Snowflake snowflake() {
    return Snowflake.of(channelId);
  }
}
