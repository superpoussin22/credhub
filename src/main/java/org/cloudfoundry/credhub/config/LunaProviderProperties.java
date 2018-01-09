package org.cloudfoundry.credhub.config;

public class LunaProviderProperties {

  String partitionName;
  String partitionPassword;


  public String getPartitionName() {
    return partitionName;
  }

  public String getPartitionPassword() {
    return partitionPassword;
  }

  public void setPartitionName(String partitionName) {
    this.partitionName = partitionName;
  }

  public void setPartitionPassword(String partitionPassword) {
    this.partitionPassword = partitionPassword;
  }
}
