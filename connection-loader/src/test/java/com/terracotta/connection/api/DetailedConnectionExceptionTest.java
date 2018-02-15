package com.terracotta.connection.api;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DetailedConnectionExceptionTest {

  @Test
  public void testGetConnectionErrorMapWithNullMap(){
    Exception exc = mock(Exception.class);
    DetailedConnectionException connException = new DetailedConnectionException(exc, null);
    Map<String, List<Exception>> exceptionMap = connException.getConnectionErrorMap();
    assertNull(exceptionMap);
  }

  @Test
  public void testGetConnectionErrorMap(){
    Exception thrownException = mock(Exception.class);
    String endoint = "localhost:9510";
    Exception e1 = new IOException("first exception");
    Exception e2 = new IOException("second exception");
    List<Exception> errorList = new ArrayList<>();
    errorList.add(e1);
    errorList.add(e2);
    Map<String, List<Exception>> errorMap = new HashMap<>();
    errorMap.put(endoint, errorList);
    DetailedConnectionException connException = new DetailedConnectionException(thrownException, errorMap);
    Map<String, List<Exception>> exceptionMap = connException.getConnectionErrorMap();
    assertNotNull(exceptionMap);
    assertEquals(1, exceptionMap.size());
    List<Exception> retExceptions = exceptionMap.get(endoint);
    assertNotNull(retExceptions);
    assertEquals(2,retExceptions.size());
    assertTrue(retExceptions.contains(e1));
    assertTrue(retExceptions.contains(e2));
  }
}
