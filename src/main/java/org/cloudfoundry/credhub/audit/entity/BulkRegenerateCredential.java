package org.cloudfoundry.credhub.audit.entity;

import org.cloudfoundry.credhub.audit.OperationDeviceAction;

public class BulkRegenerateCredential implements RequestDetails {
  private String signedBy;

  public BulkRegenerateCredential(){

  }

  public BulkRegenerateCredential(String signedBy){
    this.signedBy = signedBy;
  }


  public String getSignedBy() {
    return signedBy;
  }

  public void setSignedBy(String signedBy) {
    this.signedBy = signedBy;
  }

  @Override
  public OperationDeviceAction operation() {
    return OperationDeviceAction.BULK_REGENERATE;
  }
}
