package com.terracotta.connection.api;

import org.terracotta.connection.ConnectionException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
  Should this class be going to connection-api project?
   */
public class DetailedConnectionException extends ConnectionException{

  /**
   * This type can only be created as a high-level wrapper over the underlying cause.
   *
   * @param cause The underlying throwable
   * @param errorMap is the map of a host:port combination and list of all the communication errors for this host:port combination
   */

  private final Map<String, List<Exception>> connectionErrorMap;
  public DetailedConnectionException(Throwable cause, Map<String, List<Exception>> errorMap) {
    super(cause);
    this.connectionErrorMap = errorMap;
  }

  public Map<String, List<Exception>> getConnectionErrorMap(){
    if(this.connectionErrorMap == null){
      return null;
    }
    return Collections.unmodifiableMap(connectionErrorMap);
  }
}