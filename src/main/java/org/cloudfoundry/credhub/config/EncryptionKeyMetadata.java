package org.cloudfoundry.credhub.config;

@SuppressWarnings("unused")
public class EncryptionKeyMetadata {
  private String providerName;
  private String encryptionPassword;
  private boolean active;
  private String encryptionKeyName;

  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }

  public String getEncryptionPassword() {
    return encryptionPassword;
  }

  public void setEncryptionPassword(String encryptionPassword) {
    this.encryptionPassword = encryptionPassword;
  }

  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getEncryptionKeyName() {
    return encryptionKeyName;
  }

  public void setEncryptionKeyName(String encryptionKeyName) {
    this.encryptionKeyName = encryptionKeyName;
  }

}
