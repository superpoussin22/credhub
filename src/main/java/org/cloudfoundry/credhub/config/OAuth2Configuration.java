package org.cloudfoundry.credhub.config;

import org.cloudfoundry.credhub.auth.OAuth2IssuerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

@Configuration
@ConditionalOnProperty(value = "security.oauth2.enabled")
public class OAuth2Configuration {

  @Bean
  public AccessTokenConverter jwtAccessTokenConverter() throws Exception {
    DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
    defaultAccessTokenConverter.setIncludeGrantType(true);
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setAccessTokenConverter(defaultAccessTokenConverter);
    jwtAccessTokenConverter.afterPropertiesSet();
    return jwtAccessTokenConverter;
  }

  @Bean
  public ResourceServerTokenServices resourceServerTokenServices() throws Exception {
   ResourceServerTokenServices tokenService = new RemoteTokenServices();
   return  tokenService;
  }
  @Bean
  public JwkTokenStore jwkTokenStore(OAuth2IssuerService issuerService) throws Exception {
    String jwkSetUrl = issuerService.getKeysURI();
    return new JwkTokenStore(jwkSetUrl, jwtAccessTokenConverter());
  }

  @Bean
  public AuthenticationManagerBuilder authenticationManagerBuilder() {
    final ObjectPostProcessor<Object> objectPostProcessor = new ObjectPostProcessor<Object>() {
      @Override
      public <O extends Object> O postProcess(O object) {
        return object;
      }
    };
    final AuthenticationManagerBuilder authenticationManagerBuilder =
        new AuthenticationManagerBuilder(objectPostProcessor);
    authenticationManagerBuilder.parentAuthenticationManager(authenticationManager());
    return authenticationManagerBuilder;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return authentication -> authentication;
  }

  @Bean
  public WebResponseExceptionTranslator webResponseExceptionTranslator() {
    return new DefaultWebResponseExceptionTranslator();
  }
}
