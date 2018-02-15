package com.tc.net.protocol.transport;

import com.tc.net.core.ConnectionInfo;

/**
 * Any class implementing this interface can take appropriate action
 * when a connection is not successfully established between client and server
 *
 * An instance of this interface can be passed to transport (lower) layer and on getting
 * an exception, onError can be invoked to store the exception against the
 * connection information. Stored exceptions can be passed to upper layer for inspection
 * and analysis.
 */
public interface ClientConnectionErrorListener {
  void onError(ConnectionInfo connInfo, Exception e);
}
