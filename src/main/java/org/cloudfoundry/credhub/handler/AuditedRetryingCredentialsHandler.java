package org.cloudfoundry.credhub.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.cloudfoundry.credhub.audit.EventAuditLogService;
import org.cloudfoundry.credhub.audit.EventAuditRecordParameters;
import org.cloudfoundry.credhub.request.BaseCredentialGenerateRequest;
import org.cloudfoundry.credhub.request.CredentialRegenerateRequest;
import org.cloudfoundry.credhub.util.StringUtil;
import org.cloudfoundry.credhub.view.CredentialView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class AuditedRetryingCredentialsHandler {


    private EventAuditLogService eventAuditLogService;
    private GenerateHandler generateHandler;
    private RegenerateHandler regenerateHandler;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuditedRetryingCredentialsHandler(EventAuditLogService eventAuditLogService,
                                             GenerateHandler generateHandler,
                                             RegenerateHandler regenerateHandler,
                                             ObjectMapper objectMapper) {
        this.eventAuditLogService = eventAuditLogService;
        this.generateHandler = generateHandler;
        this.regenerateHandler = regenerateHandler;
        this.objectMapper = objectMapper;
    }



    @Retryable(maxAttempts = 2,stateful= true, include = {JpaSystemException.class, DataIntegrityViolationException.class})
    public CredentialView handlePostRequest(InputStream inputStream) {
        return eventAuditLogService
                .auditEvents((auditRecordParameters -> deserializeAndHandlePostRequest(inputStream,auditRecordParameters)));
    }


    private CredentialView deserializeAndHandlePostRequest(
            InputStream inputStream,
            List<EventAuditRecordParameters> auditRecordParameters
    ) {
        try {
            String requestString = StringUtil.fromInputStream(inputStream);

            if (readRegenerateFlagFrom(requestString)) {
                CredentialRegenerateRequest requestBody = objectMapper.readValue(requestString, CredentialRegenerateRequest.class);
                return handleRegenerateRequest(requestBody, auditRecordParameters);
            } else {
                BaseCredentialGenerateRequest requestBody = objectMapper.readValue(requestString, BaseCredentialGenerateRequest.class);
                requestBody.validate();
                return handleGenerateRequest(auditRecordParameters, requestBody
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Retryable(maxAttempts = 2,include = {JpaSystemException.class, DataIntegrityViolationException.class})
    private CredentialView handleGenerateRequest(
            List<EventAuditRecordParameters> auditRecordParameters,
            BaseCredentialGenerateRequest requestBody
    ) {
        return generateHandler.handle(requestBody, auditRecordParameters);
    }

    @Retryable(maxAttempts = 2,include = {JpaSystemException.class, DataIntegrityViolationException.class})
    private CredentialView handleRegenerateRequest(
            CredentialRegenerateRequest requestBody, List<EventAuditRecordParameters> auditRecordParameters
    ) {
        return regenerateHandler.handleRegenerate(requestBody.getName(), auditRecordParameters);
    }

    private boolean readRegenerateFlagFrom(String requestString) {
        boolean isRegenerateRequest;
        try {
            isRegenerateRequest = JsonPath.read(requestString, "$.regenerate");
        } catch (PathNotFoundException e) {
            // could have just returned null, that would have been pretty useful
            isRegenerateRequest = false;
        }
        return isRegenerateRequest;
    }
}
