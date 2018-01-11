package org.cloudfoundry.credhub.data;

import org.cloudfoundry.credhub.entity.EncryptedValue;
import org.cloudfoundry.credhub.repository.EncryptedValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.cloudfoundry.credhub.repository.EncryptedValueRepository.BATCH_SIZE;

@Service
public class EncryptedValueDataService {

  private final IEncryptionKeyCanaryMapper encryptionKeyCanaryMapper;
  private final EncryptedValueRepository encryptedValueRepository;

  @Autowired
  protected EncryptedValueDataService(
      IEncryptionKeyCanaryMapper encryptionKeyCanaryMapper,
      EncryptedValueRepository encryptedValueRepository) {
    this.encryptionKeyCanaryMapper = encryptionKeyCanaryMapper;
    this.encryptedValueRepository = encryptedValueRepository;
  }

  public Long countAllNotEncryptedByActiveKey() {
    UUID activeUuid = encryptionKeyCanaryMapper.getActiveUuid();

    return encryptedValueRepository.countByEncryptionKeyUuidNot(activeUuid);
  }

  public Slice<EncryptedValue> findEncryptedWithAvailableInactiveKey() {
    return encryptedValueRepository
        .findByEncryptionKeyUuidIn(
            encryptionKeyCanaryMapper.getCanaryUuidsWithKnownAndInactiveKeys(),
            new PageRequest(0, BATCH_SIZE)
        );
  }

  public void rotate(EncryptedValue value) {
    String decrypted = encryptionKeyCanaryMapper.getEncryptionService(value).decrypt(value);
    EncryptedValue newValue = encryptionKeyCanaryMapper.getActiveEncryptionService().encrypt(decrypted);

    newValue.setUuid(value.getUuid());
    encryptedValueRepository.saveAndFlush(newValue);
  }
}
