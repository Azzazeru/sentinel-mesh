package com.sentinel.api.dto;

import java.math.BigDecimal;

public class CreateTargetRequest {
  private String productName;
  private BigDecimal targetPrice;
  private String checkType;
  private String userEmail;

  public CreateTargetRequest() {
  }

  public CreateTargetRequest(
      String productName,
      BigDecimal targetPrice,
      String checkType,
      String userEmail) {
    this.productName = productName;
    this.targetPrice = targetPrice;
    this.checkType = checkType;
    this.userEmail = userEmail;
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

  public String getUserEmail() {
    return userEmail;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public void setTargetPrice(BigDecimal targetPrice) {
    this.targetPrice = targetPrice;
  }

  public void setCheckType(String checkType) {
    this.checkType = checkType;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

}