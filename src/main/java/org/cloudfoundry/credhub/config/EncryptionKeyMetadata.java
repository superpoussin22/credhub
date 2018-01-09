package org.cloudfoundry.credhub.config;

@SuppressWarnings("unused")
public class EncryptionKeyMetadata {

  private String encryptionKeyName;
  private String encryptionPassword;
  private boolean active = false;


  public String getEncryptionKeyName() {
    return encryptionKeyName;
  }

  public void setEncryptionKeyName(String encryptionKeyName) {
    this.encryptionKeyName = encryptionKeyName;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getEncryptionPassword() {
    return encryptionPassword;
  }

  public void setEncryptionPassword(String encryptionPassword) {
    this.encryptionPassword = encryptionPassword;
  }

}
