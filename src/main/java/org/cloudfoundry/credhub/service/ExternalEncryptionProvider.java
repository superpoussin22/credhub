package org.cloudfoundry.credhub.service;

import org.cloudfoundry.credhub.entity.EncryptedValue;

import java.security.Key;
import java.util.UUID;

public class ExternalEncryptionProvider implements EncryptionProvider {



  @Override
  public EncryptedValue encrypt(EncryptionKey key, String value) throws Exception {

    return null;
  }

  @Override
  public EncryptedValue encrypt(UUID canaryUuid, Key key, String value) throws Exception {
    return null;
  }

  @Override
  public String decrypt(EncryptionKey key, byte[] encryptedValue, byte[] nonce) throws Exception {
    return null;
  }

  @Override
  public String decrypt(Key key, byte[] encryptedValue, byte[] nonce) throws Exception {
    return null;
  }
}
