package org.cloudfoundry.credhub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("encryption")
public class EncryptionKeysConfiguration {

  private List<EncryptionKeyMetadata> keys;
  private boolean keyCreationEnabled;
  private List<EncryptionKeyProvider> providers;

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
