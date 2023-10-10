package com.esc.bluespring.common.configuration;

import discord4j.core.object.entity.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscordLifeCycle implements DisposableBean {
  private final Channel channel;
  @Override
  public void destroy() {
    channel.getClient().logout().block();
  }
}
