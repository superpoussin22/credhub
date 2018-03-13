package org.cloudfoundry.credhub.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudfoundry.credhub.entity.EncryptedValue;
import org.cloudfoundry.credhub.service.grpc.DecryptionRequest;
import org.cloudfoundry.credhub.service.grpc.DecryptionResponse;
import org.cloudfoundry.credhub.service.grpc.EncryptionProviderGrpc;
import org.cloudfoundry.credhub.service.grpc.EncryptionRequest;
import org.cloudfoundry.credhub.service.grpc.EncryptionResponse;

import java.security.Key;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.UUID;

public class ExternalEncryptionProvider implements EncryptionProvider {
  private static final Logger logger = LogManager.getLogger(ExternalEncryptionProvider.class.getName());
  private final ObjectMapper objectMapper;
  private final EncryptionProviderGrpc.EncryptionProviderBlockingStub blockingStub;

  public ExternalEncryptionProvider(String host, int port){
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true)
        .build());
  }

  ExternalEncryptionProvider(ManagedChannel channel){
    blockingStub = EncryptionProviderGrpc.newBlockingStub(channel);
    objectMapper = new ObjectMapper();
  }

  @Override
  public EncryptedValue encrypt(EncryptionKey key, String value) throws Exception {
  // TODO convert EncryptionKey into key value strings and call the private encrypt method
    return null;
  }

  @Override
  public EncryptedValue encrypt(UUID canaryUuid, Key key, String value) throws Exception {
    return null;
  }

  @Override
  public String decrypt(EncryptionKey key, byte[] encryptedValue, byte[] nonce) throws Exception {
    return null;
  }

  @Override
  public String decrypt(Key key, byte[] encryptedValue, byte[] nonce) throws Exception {
    return null;
  }

  @Override
  public SecureRandom getSecureRandom() {
    return null;
  }

  private void encrypt(String value, String keyId) throws JsonProcessingException {
    EncryptionRequest request = EncryptionRequest.newBuilder().setData(value).setKey(keyId).build();
    EncryptionResponse response;
    try {
      response = blockingStub.encrypt(request);
    } catch (StatusRuntimeException e) {
      logger.error("RPC failed ", e);
      return;
    }
    final HashMap<String, String> result = new HashMap<>();
    result.put("value", response.getData().toStringUtf8());
    result.put("nonce", response.getNonce().toStringUtf8());

    logger.debug("Encrypted value");
  }

  private void decrypt(String value, String keyId, String nonce) {
    DecryptionRequest request = DecryptionRequest.newBuilder().
        setData(value).
        setKey(keyId).
        setNonce(nonce).
        build();
    DecryptionResponse response;

    try {
      response = blockingStub.decrypt(request);
    } catch (StatusRuntimeException e) {
      logger.error("RPC failed ", e);
      return;
    }
    logger.debug("Decrypted value");
  }
}
