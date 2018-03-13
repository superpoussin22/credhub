package org.cloudfoundry.credhub.service;

import org.cloudfoundry.credhub.entity.EncryptedValue;

import java.security.Key;
import java.util.UUID;

public interface EncryptionProvider {
  EncryptedValue encrypt(EncryptionKey key, String value) throws Exception;
  EncryptedValue encrypt(UUID canaryUuid, Key key, String value) throws Exception;
  String decrypt(EncryptionKey key, byte[] encryptedValue, byte[] nonce) throws Exception;
  String decrypt(Key key, byte[] encryptedValue, byte[] nonce) throws Exception;
}
