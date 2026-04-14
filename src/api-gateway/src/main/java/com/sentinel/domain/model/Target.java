package com.sentinel.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Target {
  private final String id;
  private final String productName;
  private final BigDecimal targetPrice;
  private final String checkType;
  private final LocalDateTime createdAt;
  private final String userEmail;

  public Target(
      String id,
      String productName,
      BigDecimal targetPrice,
      String checkType,
      LocalDateTime createdAt,
      String userEmail) {
    this.id = id;
    this.productName = productName;
    this.targetPrice = targetPrice;
    this.checkType = checkType;
    this.createdAt = createdAt;
    this.userEmail = userEmail;
  }

  public static Target createNew(
      String productName,
      BigDecimal targetPrice,
      String checkType,
      String userEmail) {
    return new Target(
        UUID.randomUUID().toString(),
        productName,
        targetPrice,
        checkType,
        LocalDateTime.now(),
        userEmail);
  }

  public String getId() {
    return id;
  }

  public String getProductName() {
    return productName;
  }

  public BigDecimal getTargetPrice() {
    return targetPrice;
  }

  public String getCheckType() {
    return checkType;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getUserEmail() {
    return userEmail;
  }

  @Override
  public String toString() {
    return "Target{" +
        "id='" + id + '\'' +
        ", productName='" + productName + '\'' +
        ", targetPrice=" + targetPrice +
        ", checkType='" + checkType + '\'' +
        ", createdAt=" + createdAt +
        ", userEmail='" + userEmail + '\'' +
        '}';
  }
}