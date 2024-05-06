package com.amitesh.shop.adapter.out.persistence.inmemory;

import com.amitesh.shop.adapter.out.persistence.AbstractProductRepositoryTest;

class InMemoryProductRepositoryTest extends
    AbstractProductRepositoryTest<InMemoryProductRepository> {

  @Override
  protected InMemoryProductRepository createProductRepository() {
    return new InMemoryProductRepository();
  }

}