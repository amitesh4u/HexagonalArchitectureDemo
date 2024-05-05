package com.amitesh.shop.model.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CustomerIdTest {

  @DisplayName("Throw error if Id is not positive")
  @ParameterizedTest(name = "#{index} - Run test with args={0}")
  @ValueSource(ints = {-1, 0, -100})
  void testConstructor_idIsNotPositive_throwException(int id) {
    assertThatThrownBy(() -> new CustomerId(id))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'id' must be a positive integer");
  }

  @DisplayName("Return CustomerId for valid Id")
  @Test
  void testConstructor_idIsValid_returnCustomerId() {
    int validId = 5;
    CustomerId customerId = new CustomerId(validId);
    assertThat(customerId.value()).isEqualTo(validId);
  }
}