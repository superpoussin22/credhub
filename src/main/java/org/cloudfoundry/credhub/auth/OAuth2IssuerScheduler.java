package org.cloudfoundry.credhub.auth;

//@Component
//@Profile({"prod", "dev"})
//@ConditionalOnProperty(value = "security.oauth2.enabled")
public class OAuth2IssuerScheduler {
  private OAuth2IssuerServiceImpl oAuth2IssuerService;

 // @Autowired
 // OAuth2IssuerScheduler(OAuth2IssuerServiceImpl oAuth2IssuerService) {
   // this.oAuth2IssuerService = oAuth2IssuerService;
//  }

 // @EventListener(ContextRefreshedEvent.class)
 // private void refreshIssuer() {
 //   oAuth2IssuerService.fetchIssuer();
 // }
}
