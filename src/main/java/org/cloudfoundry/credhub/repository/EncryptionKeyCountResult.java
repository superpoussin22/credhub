package org.cloudfoundry.credhub.repository;

import org.cloudfoundry.credhub.constants.UuidConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

public class EncryptionKeyCountResult {
  private UUID encryptionKeyUuid;
  private long count;

  public EncryptionKeyCountResult(UUID encryptionKeyUuid, long count) {
    this.encryptionKeyUuid = encryptionKeyUuid;
    this.count = count;
  }

  public UUID getEncryptionKeyUuid() {
    return encryptionKeyUuid;
  }

  public void setEncryptionKeyUuid(UUID encryptionKeyUuid) {
    this.encryptionKeyUuid = encryptionKeyUuid;
  }

  public long getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
