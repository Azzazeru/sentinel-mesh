package com.sentinel.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentinel.api.dto.CreateTargetRequest;
import com.sentinel.domain.model.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TargetService {
  private static final Logger logger = LoggerFactory.getLogger(TargetService.class);

  @Inject
  RabbitMqPublisher publisher;

  @Inject
  ObjectMapper objectMapper;

  public Target createAndQueueTarget(CreateTargetRequest request) {
    logger.debug("Creating target from request: {}", request);

    validateTargetRequest(request);

    Target target = Target.createNew(
        request.getProductName(),
        request.getTargetPrice(),
        request.getCheckType(),
        request.getUserEmail());

    publishTargetToQueue(target);

    logger.info("Target created and queued: {}", target.getProductName(), target.getId());
    return target;
  }

  private void validateTargetRequest(CreateTargetRequest request) {
    if (request.getProductName() == null || request.getProductName().isEmpty()) {
      throw new IllegalArgumentException("Product name is required");
    }
    if (request.getTargetPrice() == null || request.getTargetPrice().signum() <= 0) {
      throw new IllegalArgumentException("Target price must be a positive number");
    }
    if (request.getCheckType() == null || !isValidCheckType(request.getCheckType())) {
      throw new IllegalArgumentException(
          "Invalid check type. Allowed values are: PRICE_DROP, PRICE_RISE, AVAILABILITY");
    }
    if (request.getUserEmail() == null || !isValidEmail(request.getUserEmail())) {
      throw new IllegalArgumentException("A valid user email is required");
    }
  }

  private boolean isValidCheckType(String checkType) {
    return checkType.equals("PRICE_DROP") ||
        checkType.equals("PRICE_RISE") ||
        checkType.equals("AVAILABILITY");
  }

  private boolean isValidEmail(String email) {
    return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
  }

  private void publishTargetToQueue(Target target) {
    try {
      String targetJson = objectMapper.writeValueAsString(target);
      publisher.publishTargetCheckRequest(targetJson);
    } catch (Exception e) {
      logger.error("Failed to serialize target for publishing", e);
      throw new RuntimeException("Failed to queue target: " + e.getMessage(), e);
    }
  }
}