package com.terracotta.connection;

public class ClientConnectionException extends RuntimeException{
  private static final long serialVersionUID = 1L;

  ClientConnectionException(String message){
    super(message);
  }
}
