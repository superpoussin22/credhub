package org.cloudfoundry.credhub.service;

import org.cloudfoundry.credhub.entity.EncryptedValue;

import java.security.Key;
import java.security.SecureRandom;
import java.util.UUID;

public class EncryptionKey implements RandomNumberGenerator {

  private EncryptionProvider provider;
  private UUID uuid;
  private final Key key;

  public EncryptionKey(EncryptionProvider provider, UUID uuid, Key key) {
    this.provider = provider;
    this.uuid = uuid;
    this.key = key;
  }

  public Key getKey() {
    return key;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public String decrypt(byte[] encryptedValue, byte[] nonce) throws Exception {
    return provider.decrypt(this, encryptedValue, nonce);
  }

  public EncryptedValue encrypt(String value) throws Exception {
    return provider.encrypt(this, value);
  }

  public EncryptionProvider getProvider() {
    return provider;
  }

  @Override
  public SecureRandom getSecureRandom() {
    return provider.getSecureRandom();
  }
}
