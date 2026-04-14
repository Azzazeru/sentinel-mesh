package com.sentinel.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentinel.api.dto.CreateTargetRequest;
import com.sentinel.domain.model.Target;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@QuarkusTest
@DisplayName("TargetService Unit Tests")
class TargetServiceTest {

  @Mock
  private RabbitMqPublisher publisher;

  @InjectMocks
  private TargetService targetService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    targetService = new TargetService();
    targetService.publisher = publisher;
    targetService.objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Should create and queue target successfully")
  void testCreateAndQueueTargetSuccess() {
    CreateTargetRequest request = new CreateTargetRequest(
        "iPhone 15 Pro",
        BigDecimal.valueOf(999.99),
        "PRICE_DROP",
        "user@example.com");

    Target result = targetService.createAndQueueTarget(request);

    assertNotNull(result);
    assertEquals("iPhone 15 Pro", result.getProductName());
    assertEquals(BigDecimal.valueOf(999.99), result.getTargetPrice());

    verify(publisher, times(1)).publishTargetCheckRequest(anyString());
  }

  @Test
  @DisplayName("Should reject empty product name")
  void testValidationFailsOnEmptyProductName() {
    CreateTargetRequest request = new CreateTargetRequest(
        "",
        BigDecimal.valueOf(100),
        "PRICE_DROP",
        "user@example.com");

    assertThrows(IllegalArgumentException.class,
        () -> targetService.createAndQueueTarget(request));
  }

  @Test
  @DisplayName("Should reject negative target price")
  void testValidationFailsOnNegativePrice() {
    CreateTargetRequest request = new CreateTargetRequest(
        "Product",
        BigDecimal.valueOf(-50),
        "PRICE_DROP",
        "user@example.com");

    assertThrows(IllegalArgumentException.class,
        () -> targetService.createAndQueueTarget(request));
  }

  @Test
  @DisplayName("Should reject invalid check type")
  void testValidationFailsOnInvalidCheckType() {
    CreateTargetRequest request = new CreateTargetRequest(
        "Product",
        BigDecimal.valueOf(100),
        "INVALID_TYPE",
        "user@example.com");

    assertThrows(IllegalArgumentException.class,
        () -> targetService.createAndQueueTarget(request));
  }

  @Test
  @DisplayName("Should reject invalid email format")
  void testValidationFailsOnInvalidEmail() {
    CreateTargetRequest request = new CreateTargetRequest(
        "Product",
        BigDecimal.valueOf(100),
        "PRICE_DROP",
        "invalid-email");

    assertThrows(IllegalArgumentException.class,
        () -> targetService.createAndQueueTarget(request));
  }
}