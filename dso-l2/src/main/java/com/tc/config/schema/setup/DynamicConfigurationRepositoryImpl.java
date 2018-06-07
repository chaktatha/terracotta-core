package com.tc.config.schema.setup;

import org.terracotta.entity.DynamicConfigurationParser;

import com.tc.config.schema.beanfactory.DynamicConfigurationParserProvider;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.tc.util.Assert.assertNotNull;

/**
 * Equivalent of StandardXMLFileConfigurationCreator
 */

public class DynamicConfigurationRepositoryImpl implements DynamicConfigurationRepository {

  private final Path repositoryPath;
  private final DynamicConfigurationParserProvider configurationFactoryProvider;

  private final AtomicReference<ConcurrentMap<String, byte[]>> configurationCache;

  public DynamicConfigurationRepositoryImpl(DynamicConfigurationParserProvider configurationFactoryProvider, Path repositoryPath){
    assertNotNull(configurationFactoryProvider);
    assertNotNull(repositoryPath);
    this.configurationCache = new AtomicReference<>();
    this.configurationFactoryProvider = configurationFactoryProvider;
    this.repositoryPath = repositoryPath;
    loadConfiguration();
  }

  @Override
  public void loadConfiguration(){
    List<Path> configFilesPath;
    try {
      configFilesPath = Files.walk(repositoryPath).filter(Files::isRegularFile).collect(Collectors.toList());
    } catch (IOException e) {
     throw new UncheckedIOException(e);
    }

    ConcurrentMap<String, byte[]> cache = configFilesPath.stream()
        .filter(path -> !path.toString().contains("_")).collect(Collectors.toConcurrentMap(this::getFileName,this::readConfigFile)
    );

    ConcurrentMap existingMap = configurationCache.get();
    while(!configurationCache.compareAndSet(existingMap, cache)){}

  }

  public <T> T getConfiguration(Class<T> type) {
    assertNotNull(type);
    DynamicConfigurationParser<T> factory = configurationFactoryProvider.getParser(type);
    byte[] configBytes = configurationCache.get().get(normalize(type.getSimpleName()));
    if (configBytes == null) {
      return null;
    }
    T returnObject = factory.unmarshallDynamicConfigurationEntity(configBytes);
    return returnObject;
  }

  @Override
  public <T> void saveConfiguration(final T entity) {
    assertNotNull(entity);
    Class<T> type = (Class<T>)entity.getClass();
    String configurationName = entity.getClass().getSimpleName();
    String configFileName = configurationName.toLowerCase();
    DynamicConfigurationParser<T> factory = configurationFactoryProvider.getParser(type);
    String completeFileName;
    String backupFileName;
    if (repositoryPath.endsWith(File.separator)){
      completeFileName = repositoryPath.toString()+configFileName;
      backupFileName = repositoryPath.toString()+completeFileName+"_backup_"+System.currentTimeMillis();
    }else {
      completeFileName = repositoryPath.toString()+File.separator+configFileName;
      backupFileName = repositoryPath.toString()+File.separator+configFileName+"_backup_"+System.currentTimeMillis();
    }
    byte[] bytes = factory.marshallDynamicConfigurationEntity(entity);
    try {
      Files.copy(Paths.get(completeFileName), Paths.get(backupFileName));
      Files.write(Paths.get(completeFileName), bytes);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  String getFileName(Path path){
    return normalize(path.getFileName().toString());
  }

  private String normalize(String name){
    return name.toLowerCase();
  }

  byte[] readConfigFile(Path path){
    byte[] configBytes;
    try {
      configBytes = Files.readAllBytes(path);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return configBytes;
  }

  @Override
  public Set<String> getAllConfigurationNames() {
    return configurationCache.get().keySet();
  }
}