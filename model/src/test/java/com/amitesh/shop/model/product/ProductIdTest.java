package com.amitesh.shop.model.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(SoftAssertionsExtension.class)
class ProductIdTest {

  @InjectSoftAssertions
  private SoftAssertions softly;

  @DisplayName("Throw error if Id is null or empty or contains only whitespace")
  @ParameterizedTest(name = "#{index} - Run test with args={0}")
  @NullAndEmptySource
  @ValueSource(strings = {" ", "\n", "\t"})
  void testConstructor_idIsNullOrEmpty_throwException(String id){
    assertThatThrownBy(() -> new ProductId(id))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'id' must not be null or empty");
  }

  @DisplayName("Return ProductId for valid Id")
  @Test
  void testConstructor_idIsValid_returnProductId(){
    String validId = "valid";
    ProductId productId = new ProductId(validId);
    softly.assertThat(productId).isNotNull();
    softly.assertThat(productId.value()).isEqualTo(validId);
  }
}