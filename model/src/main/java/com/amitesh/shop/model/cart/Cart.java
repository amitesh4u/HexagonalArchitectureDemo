package com.amitesh.shop.model.cart;

import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.price.Price;
import com.amitesh.shop.model.product.Product;
import com.amitesh.shop.model.product.ProductId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Cart {

  @Getter
  private final CustomerId id; // cart ID = customer ID

  private final Map<ProductId, CartLineItem> lineItems = new LinkedHashMap<>();

  public void addProduct(final Product product, final int quantity) throws InsufficientStockException {
    lineItems
        .computeIfAbsent(product.id(), absent -> new CartLineItem(product))
        .increaseQuantityBy(quantity, product.itemsInStock());
  }

  public List<CartLineItem> lineItems() {
    return List.copyOf(lineItems.values());
  }

  public int numberOfItems() {
    return lineItems.values().stream().mapToInt(CartLineItem::quantity).sum();
  }

  public Price subTotal() {
    return lineItems.values().stream().map(CartLineItem::subTotal).reduce(Price::add).orElse(null);
  }

  /**
   * Should be used to Recreate cart without verifying the stock.
   * That can be done while updating the Cart or during Checkout verification
   * @param product Product to add in the cart
   * @param quantity Quantity of product to add
   */
  public void recreateCart(final Product product, final int quantity) {
    CartLineItem cartLineItem = new CartLineItem(product, quantity);
    lineItems.put(product.id(), cartLineItem);
  }
}
