package com.amitesh.shop.model.product;

import com.amitesh.shop.model.price.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
/* If true, the getter for pepper is just pepper(), and the setter is pepper(T newValue).
    Generated setters return this instead of void.
 */
@Accessors(fluent = true)
@AllArgsConstructor
public class Product {

  private final ProductId id;
  private String name;
  private String description;
  private Price price;
  private int itemsInStock;

}
