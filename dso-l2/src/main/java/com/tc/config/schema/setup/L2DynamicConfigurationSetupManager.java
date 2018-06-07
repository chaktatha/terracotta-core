package com.tc.config.schema.setup;

import org.terracotta.entity.ServiceProvider;

import java.util.Map;

public interface L2DynamicConfigurationSetupManager {
  DynamicConfigurationRepository getConfigurationRepository();
  Map<String, Class<ServiceProvider>> getServiceProviderMap();
}
