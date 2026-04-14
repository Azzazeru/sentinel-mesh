package com.sentinel.domain.service;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RabbitMqPublisher {
  private static final Logger logger = LoggerFactory.getLogger(RabbitMqPublisher.class);

  @Inject
  @Channel("pending-checks")
  Emitter<String> pendingChecksEmitter;

  public void publishTargetCheckRequest(String targetJson) {
    try {
      pendingChecksEmitter.send(targetJson);
      logger.info("Target check published to queue: {}", targetJson);
    } catch (Exception e) {
      logger.error("Failed to publish target check request", e);
      throw new RuntimeException("Message publishing failed:" + e.getMessage(), e);
    }
  }
}