package org.cloudfoundry.credhub.config;

import org.cloudfoundry.credhub.service.EncryptionService;
import org.cloudfoundry.credhub.service.InternalEncryptionService;
import org.cloudfoundry.credhub.service.LunaConnection;
import org.cloudfoundry.credhub.service.LunaEncryptionService;
import org.cloudfoundry.credhub.service.PasswordKeyProxyFactory;
import org.cloudfoundry.credhub.util.TimedRetry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Configuration
@ConfigurationProperties("encryption")
public class EncryptionKeysConfiguration {

  private List<EncryptionKeyProvider> providers;
  private boolean keyCreationEnabled;

  public List<EncryptionKeyProvider> getProviders() {
    return providers;
  }

  public void setProviders(List<EncryptionKeyProvider> providers) {
    this.providers = providers;
  }

  public boolean isKeyCreationEnabled() {
    return keyCreationEnabled;
  }

  public void setKeyCreationEnabled(boolean keyCreationEnabled) {
    this.keyCreationEnabled = keyCreationEnabled;
  }

  @Bean
  public EncryptionService encryptionService(TimedRetry timedRetry, PasswordKeyProxyFactory passwordKeyProxyFactory)
      throws Exception {
    List<EncryptionKeyProvider> providers = getProviders();
    EncryptionKeyProvider activeProvider  = null;
    EncryptionKeyMetadata activeKey  = null;

    outerloop:
    for (EncryptionKeyProvider p : providers) {
      for (EncryptionKeyMetadata k : p.getKeys()) {
        if (k.isActive()) {
          activeProvider = p;
          activeKey = k;
          break outerloop;
        }
      }
    }
    if (activeProvider.getType().equals("internal")) {
      return new InternalEncryptionService(passwordKeyProxyFactory);
    } else if (activeProvider.getType().equals("hsm")) {
      return new LunaEncryptionService(new LunaConnection(activeProvider.getLunaProviderProperties()), isKeyCreationEnabled(), timedRetry);
    } else {
      throw new NotImplementedException();
    }
  }


}
