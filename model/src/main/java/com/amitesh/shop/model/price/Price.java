package com.amitesh.shop.model.price;

import java.math.BigDecimal;
import java.util.Currency;

public record Price(Currency currency, BigDecimal amount) {

  public Price {
    if(null == currency || null == amount){
      throw new IllegalArgumentException("'currency' or 'amount' must not be null");
    }
    if (amount.scale() > currency.getDefaultFractionDigits()) {
      throw new IllegalArgumentException(
          "Scale of amount %s is greater than the number of fraction digits used with currency %s"
              .formatted(amount, currency));
    }
  }

  public Price multiply(final int multiplicand) {
    return new Price(currency, amount.multiply(BigDecimal.valueOf(multiplicand)));
  }

  public Price add(final Price augend) {
    /* The currency must be set internally for the User either a default value or from User settings */
    if (!this.currency.equals(augend.currency())) {
      throw new IllegalArgumentException(
          "Currency %s of augend does not match this money's currency %s"
              .formatted(augend.currency(), this.currency));
    }

    return new Price(currency, amount.add(augend.amount()));
  }
}
