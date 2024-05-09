package com.amitesh.shop.bootstrap;

import com.amitesh.shop.SpringAppConfig;
import lombok.CustomLog;
import org.springframework.boot.SpringApplication;

@CustomLog
public class HexagonalArchitectureShoppingApplication {

  public static void main(String[] args) {
    LOGGER.info("Starting application");
    InitializationUtil.INSTANCE.setInitializationProperties();
    SpringApplication.run(SpringAppConfig.class, args);
  }
}
