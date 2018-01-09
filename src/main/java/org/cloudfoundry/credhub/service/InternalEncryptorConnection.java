package org.cloudfoundry.credhub.service;

public class InternalEncryptorConnection implements RemoteEncryptionConnectable {

  @Override
  public void reconnect(Exception reasonForReconnect) throws Exception {
    if (reasonForReconnect == null) {
      return;
    }
    throw reasonForReconnect;
  }
}
