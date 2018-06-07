package com.tc.config.schema.beanfactory;

import org.terracotta.entity.DynamicConfigurationParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tc.util.Assert.assertNotNull;

public class DynamicConfigurationParserProviderImpl implements DynamicConfigurationParserProvider {

  private final Map<Class<?>, DynamicConfigurationParser> factoryMap = new ConcurrentHashMap<>();


  @Override
  public <T> void addParser(final Class<T> clazz, final DynamicConfigurationParser<T> parser) {
    assertNotNull(clazz);
    assertNotNull(parser);
    factoryMap.put(clazz, parser);
  }

  @Override
  public <T> DynamicConfigurationParser<T> getParser(final Class<T> clazz) {
    return factoryMap.get(clazz);
  }
}
