package com.tc.net.protocol.transport;

import com.tc.net.core.ConnectionInfo;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.tc.util.Assert.assertEquals;
import static com.tc.util.Assert.assertNotNull;
import static com.tc.util.Assert.assertTrue;

public class ClientConnectionErrorDetailsTest {

  @Test
  public void testOnError(){
    ClientConnectionErrorDetails errorDetails = new ClientConnectionErrorDetails();
    ConnectionInfo connInfo = new ConnectionInfo("localhost",9510);
    Exception exception1 = new IOException("Test Exception1");
    errorDetails.onError(connInfo, exception1);
    Exception exception2 = new IOException("Test Exception2");
    errorDetails.onError(connInfo, exception2);

    Map<String, List<Exception>> retMap = errorDetails.getErrors();
    retMap.get(connInfo);
    assertNotNull(retMap);
    assertEquals(1,retMap.size());
    List<Exception> errorList = retMap.get(connInfo.toString());
    assertEquals(2,errorList.size());
    assertTrue(errorList.contains(exception1));
    assertTrue(errorList.contains(exception2));
  }

  @Test
  public void testGetErrorWithNoError(){
    ClientConnectionErrorDetails errorDetails = new ClientConnectionErrorDetails();
    Map<String, List<Exception>> retMap = errorDetails.getErrors();
    assertNotNull(retMap);
    assertTrue(retMap.isEmpty());
  }

  @Test
  public void testGetError(){
    ClientConnectionErrorDetails errorDetails = new ClientConnectionErrorDetails();
    ConnectionInfo connInfo1 = new ConnectionInfo("localhost",9510);
    Exception exception1 = new IOException("Test Exception1");
    errorDetails.onError(connInfo1, exception1);
    Exception exception2 = new IOException("Test Exception2");
    errorDetails.onError(connInfo1, exception2);

    ConnectionInfo connInfo2 = new ConnectionInfo("localhost",9710);
    Exception exception3 = new IOException("Test Exception3");
    errorDetails.onError(connInfo2, exception3);

    Map<String, List<Exception>> retMap = errorDetails.getErrors();
    assertNotNull(retMap);
    assertEquals(2, retMap.size());
    List<Exception> exceptionList1 = retMap.get(connInfo1.toString());

    assertEquals(2,exceptionList1.size());
    assertTrue(exceptionList1.contains(exception1));
    assertTrue(exceptionList1.contains(exception2));

    List<Exception> exceptionList2 = retMap.get(connInfo2.toString());

    assertEquals(1,exceptionList2.size());
    assertTrue(exceptionList2.contains(exception3));
  }
}