package com.amitesh.shop.application.service.product;

import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_PRODUCT_1;
import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_PRODUCT_2;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amitesh.shop.application.port.out.persistence.ProductRepository;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
class FindProductsServiceTest {

  @InjectSoftAssertions
  private SoftAssertions softly;

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private FindProductsService findProductsService;


  @ParameterizedTest(name = "#{index} - Running test with args = {0}")
  @NullAndEmptySource
  @ValueSource(strings = {" ", "a", "\t", "\n"})
  void testFindByNameOrDescription_invalidQuery_throwsException(String query) {
    assertThatThrownBy(
        () -> findProductsService.findByNameOrDescription(query))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'query' must be at least two characters long");

    verify(productRepository, never()).findByNameOrDescription(any());
  }

  @Test
  void testFindByNameOrDescription_invalidQuery_returnMatchingProducts() {
    when(productRepository.findByNameOrDescription("one")).thenReturn(List.of(TEST_PRODUCT_1));
    when(productRepository.findByNameOrDescription("two")).thenReturn(List.of(TEST_PRODUCT_2));
    when(productRepository.findByNameOrDescription("one-two"))
        .thenReturn(List.of(TEST_PRODUCT_1, TEST_PRODUCT_2));
    when(productRepository.findByNameOrDescription("empty")).thenReturn(List.of());

    softly.assertThat(findProductsService.findByNameOrDescription("one"))
        .containsExactly(TEST_PRODUCT_1);
    softly.assertThat(findProductsService.findByNameOrDescription("two"))
        .containsExactly(TEST_PRODUCT_2);
    softly.assertThat(findProductsService.findByNameOrDescription("one-two"))
        .containsExactly(TEST_PRODUCT_1, TEST_PRODUCT_2);
    softly.assertThat(findProductsService.findByNameOrDescription("empty")).isEmpty();

    verify(productRepository, times(4)).findByNameOrDescription(any());
  }
}