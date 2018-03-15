package org.cloudfoundry.credhub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@ConfigurationProperties("encryption")
public class EncryptionKeysConfiguration {

  private List<EncryptionKeyMetadata> keys;
  private List<EncryptionKeyProvider> providers;
  private boolean keyCreationEnabled;

  public List<EncryptionKeyProvider> getProviders() { return providers;}

  public void setProviders(List<EncryptionKeyProvider> providers) { this.providers = providers; }

  public List<EncryptionKeyMetadata> getKeys() {
    return keys;
  }

  public void setKeys(List<EncryptionKeyMetadata> keys) {
    this.keys = keys;
  }

  public boolean isKeyCreationEnabled() {
    return keyCreationEnabled;
  }

  public void setKeyCreationEnabled(boolean keyCreationEnabled) {
    this.keyCreationEnabled = keyCreationEnabled;
  }
}
