package com.amitesh.shop.adapter.out.persistence;

import com.amitesh.shop.model.price.Price;
import com.amitesh.shop.model.product.Product;
import com.amitesh.shop.model.product.ProductId;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public final class TestProducts {

  private static final Currency EUR = Currency.getInstance("EUR");
  public static final Product PLASTIC_SHEETING =
      new Product(
          new ProductId("TTKQ8NJZ"),
          "Plastic Sheeting",
          "Clear plastic sheeting, tear-resistant, tough, and durable",
          priceOf(EUR, 42, 99),
          55);
  public static final Product COMPUTER_MONITOR =
      new Product(
          new ProductId("K3SR7PBX"),
          "27-Inch Curved Computer Monitor",
          "Enjoy big, bold and stunning panoramic views",
          priceOf(EUR, 159, 99),
          24_081);
  public static final Product MONITOR_DESK_MOUNT =
      new Product(
          new ProductId("Q3W43CNC"),
          "Dual Monitor Desk Mount",
          "Ultra wide and longer arm fits most monitors",
          priceOf(EUR, 119, 90),
          1_079);
  public static final Product LED_LIGHTS =
      new Product(
          new ProductId("WM3BPG3E"),
          "50ft Led Lights",
          "Enough lights to decorate an entire room",
          priceOf(EUR, 11, 69),
          3_299);
  public static final List<Product> TEST_PRODUCTS =
      List.of(PLASTIC_SHEETING, COMPUTER_MONITOR, MONITOR_DESK_MOUNT, LED_LIGHTS);
  private TestProducts() {
  }

  public static Price priceOf(final Currency currency, final int major, final int minor) {
    int scale = currency.getDefaultFractionDigits();
    return new Price(currency, BigDecimal.valueOf(major).add(BigDecimal.valueOf(minor, scale)));
  }

}
