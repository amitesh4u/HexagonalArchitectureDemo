package com.amitesh.shop.bootstrap.integration;

import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.TEST_PORT;

import com.amitesh.shop.bootstrap.ApplicationLauncher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

abstract class EndToEndTest {

  private static ApplicationLauncher launcher;

  @BeforeAll
  static void init() {
    launcher = new ApplicationLauncher();
    launcher.startOnPort(TEST_PORT);
  }

  @AfterAll
  static void stop() {
    launcher.stop();
  }
}
