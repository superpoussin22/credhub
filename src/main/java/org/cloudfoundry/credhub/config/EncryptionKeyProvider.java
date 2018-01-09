package org.cloudfoundry.credhub.config;

import java.util.List;


public class EncryptionKeyProvider {

  private  String type;
  private  List<EncryptionKeyMetadata> keys;
  private LunaProviderProperties lunaProviderProperties;
  private String partition;
  private String partitionPassword;

  public EncryptionKeyProvider() {
    this.lunaProviderProperties = new LunaProviderProperties();
  }

  public List<EncryptionKeyMetadata> getKeys() {
    return keys;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setKeys(List<EncryptionKeyMetadata> keys) {
    this.keys = keys;
  }


  public LunaProviderProperties getLunaProviderProperties() {
    return lunaProviderProperties;
  }

  public void setPartition(String partition) {
    this.lunaProviderProperties.setPartitionName(partition);
  }

  public void setPartitionPassword(String partitionPassword) {
    this.lunaProviderProperties.setPartitionPassword(partitionPassword);
  }
}
