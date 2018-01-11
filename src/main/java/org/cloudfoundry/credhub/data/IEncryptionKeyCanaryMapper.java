package org.cloudfoundry.credhub.data;

import org.cloudfoundry.credhub.entity.EncryptedValue;

import java.security.Key;
import java.util.List;
import java.util.UUID;

public interface IEncryptionKeyCanaryMapper {

  UUID getActiveUuid();

  List<UUID> getCanaryUuidsWithKnownAndInactiveKeys();

  IEncryptionService getEncryptionService(EncryptedValue value);

  IEncryptionService getActiveEncryptionService();

  Key getKeyForUuid(UUID encryptionKeyUuid);

  void mapUuidsToKeys();

  Key getActiveKey();

  List<UUID> getKnownCanaryUuids();
}
