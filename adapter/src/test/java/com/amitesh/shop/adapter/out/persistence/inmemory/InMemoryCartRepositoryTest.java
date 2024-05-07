package com.amitesh.shop.adapter.out.persistence.inmemory;

import com.amitesh.shop.adapter.out.persistence.AbstractCartRepositoryTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class InMemoryCartRepositoryTest extends AbstractCartRepositoryTest {
}
