package com.amitesh.shop.adapter.in.rest.helper;

import static jakarta.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.CartLineItem;
import com.amitesh.shop.model.product.Product;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public final class CartsControllerAssertions {

  private CartsControllerAssertions() {
  }

  public static void assertThatResponseIsCart(Response response, Cart cart) {
    assertThat(response.statusCode()).isEqualTo(OK.getStatusCode());

    JsonPath json = response.jsonPath();

    int size = cart.lineItems().size();
    for (int i = 0; i < size; i++) {
      CartLineItem lineItem = cart.lineItems().get(i);

      String lineItemPrefix = "lineItems[%d].".formatted(i);
      Product product = lineItem.product();

      assertThat(json.getString(lineItemPrefix + "productId"))
          .isEqualTo(product.id().value());
      assertThat(json.getString(lineItemPrefix + "productName"))
          .isEqualTo(product.name());
      assertThat(json.getString(lineItemPrefix + "price.currency"))
          .isEqualTo(product.price().currency().getCurrencyCode());
      assertThat(json.getDouble(lineItemPrefix + "price.amount"))
          .isEqualTo(product.price().amount().doubleValue());
      assertThat(json.getInt(lineItemPrefix + "quantity")).isEqualTo(lineItem.quantity());
    }

    assertThat(json.getInt("numberOfItems")).isEqualTo(cart.numberOfItems());

    if (cart.subTotal() != null) {
      assertThat(json.getString("subTotal.currency"))
          .isEqualTo(cart.subTotal().currency().getCurrencyCode());
      assertThat(json.getDouble("subTotal.amount"))
          .isEqualTo(cart.subTotal().amount().doubleValue());
    } else {
      assertThat(json.getString("subTotal")).isNull();
    }
  }
}
