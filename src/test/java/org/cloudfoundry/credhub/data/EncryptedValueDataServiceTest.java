package org.cloudfoundry.credhub.data;

import org.cloudfoundry.credhub.CredentialManagerApp;
import org.cloudfoundry.credhub.entity.EncryptedValue;
import org.cloudfoundry.credhub.repository.EncryptedValueRepository;
import org.cloudfoundry.credhub.util.DatabaseProfileResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "unit-test", resolver = DatabaseProfileResolver.class)
@SpringBootTest(classes = CredentialManagerApp.class)
public class EncryptedValueDataServiceTest {
  @MockBean
  IEncryptionKeyCanaryMapper encryptionKeyCanaryMapper;

  @MockBean
  EncryptedValueRepository encryptedValueRepository;

  EncryptedValueDataService subject;

  @Before
  public void beforeEach() {
    subject = new EncryptedValueDataService(encryptionKeyCanaryMapper, encryptedValueRepository);
  }

  @Test
  public void countAllNotEncryptedByActiveKey() throws Exception {
    UUID activeKeyUUID = UUID.randomUUID();
    when(encryptionKeyCanaryMapper.getActiveUuid()).thenReturn(activeKeyUUID);

    subject.countAllNotEncryptedByActiveKey();

    verify(encryptedValueRepository).countByEncryptionKeyUuidNot(activeKeyUUID);
  }

  @Test
  public void findEncryptedWithAvailableInactiveKey() throws Exception {
    List<UUID> canaryUuids = Collections.singletonList(UUID.randomUUID());
    Slice<EncryptedValue> encryptedValues = new SliceImpl(Collections.singletonList(new EncryptedValue()));
    when(encryptionKeyCanaryMapper.getCanaryUuidsWithKnownAndInactiveKeys()).thenReturn(canaryUuids);
    when(encryptedValueRepository.findByEncryptionKeyUuidIn(eq(canaryUuids), any())).thenReturn(encryptedValues);

    assertThat(subject.findEncryptedWithAvailableInactiveKey(), equalTo(encryptedValues));
  }

  @Test
  public void rotate() throws Exception {
    EncryptedValue value = new EncryptedValue(UUID.randomUUID(), "old value".getBytes(), "nonce".getBytes());
    String decryptedValue = "some-decrypted-value";
    EncryptedValue newEncryptedValue = new EncryptedValue(UUID.randomUUID(), "new value".getBytes(), "nonce".getBytes());

    IEncryptionService oldEncryptionService = mock(IEncryptionService.class);
    IEncryptionService activeEncryptionService = mock(IEncryptionService.class);

    when(encryptionKeyCanaryMapper.getEncryptionService(value)).thenReturn(oldEncryptionService);
    when(encryptionKeyCanaryMapper.getActiveEncryptionService()).thenReturn(activeEncryptionService);

    when(oldEncryptionService.decrypt(value)).thenReturn(decryptedValue);
    when(activeEncryptionService.encrypt(decryptedValue)).thenReturn(newEncryptedValue);

    subject.rotate(value);

    verify(encryptedValueRepository).saveAndFlush(newEncryptedValue);
    assertThat(newEncryptedValue.getUuid(), equalTo(value.getUuid()));
  }
}
