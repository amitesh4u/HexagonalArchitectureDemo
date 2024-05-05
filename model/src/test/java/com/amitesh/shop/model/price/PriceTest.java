package com.amitesh.shop.model.price;

import static com.amitesh.shop.model.ModelTestHelper.EUR;
import static com.amitesh.shop.model.ModelTestHelper.euros;
import static com.amitesh.shop.model.ModelTestHelper.usDollars;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
class PriceTest {

  @InjectSoftAssertions
  private SoftAssertions softly;

  @DisplayName("Throw error if amount is null")
  @Test
  void testConstructor_amountIsNull_throwException() {
    assertThatThrownBy(() -> new Price(EUR, null))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'currency' or 'amount' must not be null");
  }

  @DisplayName("Throw error if amount is null")
  @Test
  void testConstructor_currencyIsNull_throwException() {
    assertThatThrownBy(() -> new Price(null, BigDecimal.ONE))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'currency' or 'amount' must not be null");
  }

  @DisplayName("Throw error if scale doesn't match with currency default scale")
  @Test
  void testConstructor_amountWithAnInvalidScale_throwsIllegalArgumentException() {
    BigDecimal amountWithScale3 = new BigDecimal(BigInteger.valueOf(12999), 3);

    ThrowingCallable invocation = () -> new Price(EUR, amountWithScale3);

    assertThatIllegalArgumentException().isThrownBy(invocation);
  }

  @DisplayName("Throw error if currency doesn't match while adding amount")
  @Test
  void testAdd_givenEuroAmount_addDollarAmount_throwsIllegalArgumentException() {
    Price euros = euros(11, 99);
    Price dollars = usDollars(11, 99);

    ThrowingCallable invocation = () -> euros.add(dollars);

    assertThatIllegalArgumentException().isThrownBy(invocation);
  }

  @DisplayName("Test multiply method with valid values")
  @Test
  void testMultiply_validAmount_returnValidResponse() {
    Price dollars = usDollars(11, 99);

    Price newPrice = dollars.multiply(3);

    softly.assertThat(dollars.currency()).isEqualTo(newPrice.currency());
    softly.assertThat(dollars.amount().multiply(new BigDecimal(3))).isEqualTo(newPrice.amount());
  }

  @DisplayName("Test add method with valid values")
  @Test
  void testAdd_validAmount_returnValidResponse() {
    Price dollars = usDollars(11, 99);

    Price augend = usDollars(12, 34);
    Price newPrice = dollars.add(augend);

    softly.assertThat(dollars.currency()).isEqualTo(newPrice.currency());
    softly.assertThat(dollars.amount().add(augend.amount())).isEqualTo(newPrice.amount());
  }
}