package com.amitesh.shop.bootstrap;

import java.lang.management.ManagementFactory;
import java.util.Properties;
import org.springframework.util.ObjectUtils;

/**
 * Set Process related properties for Logging
 */
public enum InitializationUtil {

  INSTANCE;

  public void setInitializationProperties() {
    Properties systemProperties = System.getProperties();
    /* Setting Process id for logging */
    systemProperties.put("pid",
        ManagementFactory.getRuntimeMXBean().getName().replaceAll("@.*", ""));

    /* Verifying and setting Active profile */
    if (ObjectUtils.isEmpty(systemProperties.get("spring.profiles.active"))) {
      systemProperties.put("spring.profiles.active", "inmemory");
    }
    System.setProperties(systemProperties);
  }
}
