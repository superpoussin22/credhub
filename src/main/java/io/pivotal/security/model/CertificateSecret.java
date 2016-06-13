package io.pivotal.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CertificateSecret implements Secret {

  @NotNull
  private final String type = "certificate";

  @JsonProperty("certificate")
  private CertificateBody certificateBody;

  public CertificateSecret(String ca, String pub, String priv) {
    setCertificateBody(new CertificateBody(ca, pub, priv));
  }

  public CertificateSecret(String ca, String priv) {
    setCertificateBody(new CertificateBody(null, ca, priv));
  }

  @Override
  public String getType() {
    return type;
  }

  public CertificateBody getCertificateBody() {
    return certificateBody;
  }

  public void setCertificateBody(CertificateBody certificateBody) {
    this.certificateBody = certificateBody;
  }
}