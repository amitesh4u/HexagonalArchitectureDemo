package com.amitesh.shop.model.price;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Price(Currency currency, BigDecimal amount) {
  public Price {
    Objects.requireNonNull(currency, "'currency' must not be null");
    Objects.requireNonNull(amount, "'amount' must not be null");

    if (amount.scale() > currency.getDefaultFractionDigits()) {
      throw new IllegalArgumentException(
          "Scale of amount %s is greater than the number of fraction digits used with currency %s"
              .formatted(amount, currency));
    }
  }
}
