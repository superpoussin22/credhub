package org.cloudfoundry.credhub.handler;


import org.cloudfoundry.credhub.audit.EventAuditRecordParameters;
import org.cloudfoundry.credhub.auth.UserContext;
import org.cloudfoundry.credhub.credential.CertificateCredentialValue;
import org.cloudfoundry.credhub.domain.CertificateCredentialVersion;
import org.cloudfoundry.credhub.domain.Encryptor;
import org.cloudfoundry.credhub.request.BaseCredentialGenerateRequest;
import org.cloudfoundry.credhub.service.CertificateService;
import org.cloudfoundry.credhub.service.PermissionCheckingService;
import org.cloudfoundry.credhub.service.PermissionedCertificateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Instant;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class CertificatesHandlerTest {
    private static final String CREDENTIAL_NAME = "/test/credential";
    private static final Instant VERSION1_CREATED_AT = Instant.ofEpochMilli(555555555);
    private static final Instant VERSION2_CREATED_AT = Instant.ofEpochMilli(777777777);
    private static final String UUID_STRING = "fake-uuid";
    private static final String USER = "darth-sirius";

    private CertificatesHandler subject;
    private PermissionedCertificateService permissionedCertificateService;
    private PermissionCheckingService permissionCheckingService;
    private CertificateService certificateService;
    private UniversalCredentialGenerator universalCredentialGenerator;
    private GenerationRequestGenerator generationRequestGenerator;

    private UserContext userContext;
    List<EventAuditRecordParameters> auditRecordParametersList;

    @Before
    public void beforeEach() {
      Encryptor encryptor = mock(Encryptor.class);

      permissionedCertificateService = mock(PermissionedCertificateService.class);
      permissionCheckingService = mock(PermissionCheckingService.class);
      certificateService = mock(CertificateService.class);
      universalCredentialGenerator = mock(UniversalCredentialGenerator.class);
      generationRequestGenerator = mock(GenerationRequestGenerator.class);
      subject = new CertificatesHandler(permissionedCertificateService, certificateService, universalCredentialGenerator, generationRequestGenerator);

      userContext = mock(UserContext.class);
      when(userContext.getActor()).thenReturn(USER);

    }

    @Test
    public void handleRegenerate_passesOnTransitionalFlagWhenRegenratingCertificate() {
      BaseCredentialGenerateRequest generateRequest = mock(BaseCredentialGenerateRequest.class);
      CertificateCredentialVersion certificate = mock(CertificateCredentialVersion.class);
      CertificateCredentialValue newValue = new CertificateCredentialValue("test", "test", "test", "test");

      when(certificate.getName()).thenReturn("test");

      when(certificateService.findByCredentialUuid(UUID_STRING, any())).thenReturn(certificate);
      when(generationRequestGenerator.createGenerateRequest(certificate, "test", any())).thenReturn(generateRequest);


    }
}
