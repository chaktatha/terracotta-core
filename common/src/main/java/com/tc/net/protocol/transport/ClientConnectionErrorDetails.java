package com.tc.net.protocol.transport;

import com.tc.net.core.ConnectionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientConnectionErrorDetails implements ClientConnectionErrorListener{

  private final ConcurrentHashMap<ConnectionInfo, ConcurrentLinkedQueue<Exception>> exceptionMap
      = new ConcurrentHashMap<>();

  @Override
  public void onError(ConnectionInfo connInfo, Exception e) {
    ConcurrentLinkedQueue<Exception> exceptionList = exceptionMap.get(connInfo);
    if(exceptionList == null){
      exceptionList = new ConcurrentLinkedQueue<>();
      exceptionMap.put(connInfo, exceptionList);
    }
    exceptionList.add(e);
  }

  public Map<String, List<Exception>> getErrors() {
    Map<String, List<Exception>> errorMessagesMap = new HashMap<>();
    if (exceptionMap != null) {
      for (Map.Entry<ConnectionInfo, ConcurrentLinkedQueue<Exception>> entry : exceptionMap.entrySet()) {
        ConnectionInfo connInfo = entry.getKey();
        ConcurrentLinkedQueue<Exception> exceptionList = entry.getValue();
        Object[] errorObjects = exceptionList.toArray();
        List<Exception> errorMessages = new ArrayList<>();
        for (Object errorObj : errorObjects) {
          Exception e = (Exception) errorObj;
          errorMessages.add(e);
        }
        errorMessagesMap.put(connInfo.toString(), errorMessages);
      }
    }
    return errorMessagesMap;
  }
}
