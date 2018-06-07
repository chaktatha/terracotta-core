package com.tc.config.schema.setup;

import org.terracotta.entity.DynamicConfigurationParser;
import org.terracotta.entity.ServiceProvider;

import com.tc.config.schema.beanfactory.DynamicConfigurationParserProviderImpl;
import com.tc.util.ManagedServiceLoader;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

public class L2DynamicConfigurationSetupManagerImpl implements L2DynamicConfigurationSetupManager{

  private final String[] args;
  private final DynamicConfigurationRepository configurationRepository;

  public L2DynamicConfigurationSetupManagerImpl(String[] args, ClassLoader classLoader){
    this.args = args;
    DynamicConfigurationParserProviderImpl parserProvider = new DynamicConfigurationParserProviderImpl();
    Collection<DynamicConfigurationParser> configParsers = ManagedServiceLoader.loadServices(DynamicConfigurationParser.class, classLoader);
    for(DynamicConfigurationParser parser : configParsers){
      parserProvider.addParser(parser.getType(), parser);
    }
    String configurationRepositoryPath = System.getProperty("configRepository", "../conf/dynamic");
    DynamicConfigurationRepository configurationRepository = new DynamicConfigurationRepositoryImpl(parserProvider, Paths
        .get(configurationRepositoryPath));
    this.configurationRepository = configurationRepository;
  }

  @Override
  public DynamicConfigurationRepository getConfigurationRepository() {
    return configurationRepository;
  }

  @Override
  public Map<String, Class<ServiceProvider>> getServiceProviderMap() {
    return null;
  }
}
