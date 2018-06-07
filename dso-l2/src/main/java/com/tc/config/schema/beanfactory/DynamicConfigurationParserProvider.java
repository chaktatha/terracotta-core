package com.tc.config.schema.beanfactory;

import org.terracotta.entity.DynamicConfigurationParser;

public interface DynamicConfigurationParserProvider {

  <T> void addParser(Class<T> clazz, DynamicConfigurationParser<T> factory);

  <T> DynamicConfigurationParser<T> getParser(Class<T> clazz);
}
