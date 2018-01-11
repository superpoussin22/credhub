package org.cloudfoundry.credhub.data;

import org.cloudfoundry.credhub.entity.EncryptedValue;

public interface IEncryptionService {

  String decrypt(EncryptedValue value);

  EncryptedValue encrypt(String decryptedValue);
}
