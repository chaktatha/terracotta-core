package com.tc.config.schema.setup;

import java.util.Set;

public interface DynamicConfigurationRepository {

  void loadConfiguration();

  <T> T getConfiguration(Class<T> type);

  <T> void saveConfiguration(T entity);

  Set<String> getAllConfigurationNames();

}
